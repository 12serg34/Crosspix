package server;

import message.*;
import message.request.DiscoverCellRequest;
import message.response.GameCreatedResponse;
import message.response.MistakeResponse;
import message.response.SuccessResponse;
import picture.Answer;

import java.util.Collection;

public class ServerMessageProcessor {
    private MessageService service;
    private Game game;
    private GamesPool gamesPool;

    public ServerMessageProcessor(GamesPool gamesPool) {
        this.gamesPool = gamesPool;
    }

    void setMessageService(MessageService service) {
        this.service = service;
    }

    void process(Message message) {
        switch (message.getHeader()) {
            case PING:
                service.send(Message.PONG);
                break;
            case GET_GAMES_LIST:
                getGamesList();
                break;
            case CREATE_GAME:
                createGame();
                break;
            case DISCOVER_CELL:
                discoverCell((DiscoverCellRequest) message.getData());
                break;
        }
    }

    private void getGamesList() {
        Collection<Game> gamesList = gamesPool.getGamesList();
        gamesList.stream();
    }

    private void createGame() {
        if (gamesPool.isEmpty()) {
            game = gamesPool.createGame();
            int height = 5;
            int width = 5;
            game.initialize(height, width);
        } else {
            game = gamesPool.getExistGame();
        }
        game.subscribeToUpdateCells(this::sendUpdates);
        service.send(GameCreatedResponse.encode(game.getStashedPicture()));
    }

    private void discoverCell(DiscoverCellRequest request) {
        int i = request.getI();
        int j = request.getJ();
        Answer answer = game.getGuessedPicture().discoverRequest(i, j);
        Message message = null;
        switch (answer) {
            case SUCCESS:
                message = SuccessResponse.pack(i, j);
                break;
            case MISTAKE:
                message = MistakeResponse.pack(i, j);
                break;
        }
        if (message != null) {
            game.cellsUpdated(message);
        }
    }

    private void sendUpdates(Message response) {
        service.send(response);
    }
}
