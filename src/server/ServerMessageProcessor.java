package server;

import message.*;
import message.request.DiscoverCellRequest;
import message.response.GameCreatedResponse;
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
            case EMPTY:
                service.send(Message.EMPTY);
                break;
            case CREATE_GAME:
                createGame();
                break;
            case DISCOVER_CELL:
                discoverCell(DiscoverCellRequest.decode(message));
                break;
        }
    }

    private void createGame() {
        if (!gamesPool.hasGame()) {
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
                message = SuccessResponse.encode(i, j);
                break;
            case MISTAKE:
                message = MistakeResponse.encode(i, j);
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
