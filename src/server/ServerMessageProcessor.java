package server;

import message.*;
import message.request.CreateGameRequest;
import message.request.DiscoverCellRequest;
import message.response.GameCreatedResponse;
import message.response.GamesInfoResponse;
import message.response.MistakeResponse;
import message.response.SuccessResponse;
import picture.Answer;

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
            case GET_GAMES_INFO:
                getGamesInfo();
                break;
            case CREATE_GAME:
                createGame((CreateGameRequest) message.getData());
                break;
            case DISCOVER_CELL:
                discoverCell((DiscoverCellRequest) message.getData());
                break;
        }
    }

    private void getGamesInfo() {
        service.send(GamesInfoResponse.pack(gamesPool.getGamesInfo()));
    }

    private void createGame(CreateGameRequest request) {
        game = gamesPool.putNewGame(request.getName());
        int height = 5;
        int width = 5;
        game.initialize(height, width);
        game.subscribeToUpdateCells(this::sendUpdates);
        service.send(GameCreatedResponse.pack(game.getStashedPicture()));
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
