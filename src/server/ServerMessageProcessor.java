package server;

import message.Message;
import message.MessageHeader;
import picture.Answer;

import static java.util.Arrays.asList;

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
            case START_GAME:
                createGame();
                break;
            case DISCOVER_CELL:
                discoverCell(message);
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
        Message response = new Message(MessageHeader.GAME_STARTED, game.getStashedPicture().toRaw());
        service.send(response);
    }

    private void discoverCell(Message request) {
        int i = request.getArguments().get(0);
        int j = request.getArguments().get(1);
        Answer answer = game.getGuessedPicture().discoverRequest(i, j);
        MessageHeader header = null;
        switch (answer) {
            case SUCCESS:
                header = MessageHeader.SUCCESS;
                break;
            case MISTAKE:
                header = MessageHeader.MISTAKE;
        }
        if (header != null) {
            Message response = new Message(header, asList(i, j));
            game.cellsUpdated(response);
        }
    }

    private void sendUpdates(Message response) {
        service.send(response);
    }
}
