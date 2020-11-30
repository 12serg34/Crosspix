package server;

import com.sun.istack.internal.NotNull;
import entities.Game;
import message.CellUpdatedNotification;
import message.Handler;
import message.request.Request;
import message.request.RequestProcessor;
import message.response.Response;
import message.request.*;
import message.response.*;
import entities.Answer;

public class ServerMessageProcessor {
    private RequestService service;
    private Game game;
    private final GamesPool gamesPool;
    private final RequestProcessor processor;

    public ServerMessageProcessor(GamesPool gamesPool) {
        this.gamesPool = gamesPool;
        processor = new RequestProcessor();
        initHandlers();
    }

    private void initHandlers() {
        processor.putHandler(
                PingRequest.class,
                (Handler<PingRequest, PongResponse>) this::ping
        );
        processor.putHandler(
                GamesInfoRequest.class,
                (Handler<GamesInfoRequest, GamesInfoResponse>) this::getGamesInfo
        );
        processor.putHandler(
                CreateGameRequest.class,
                (Handler<CreateGameRequest, GameCreatedResponse>) this::createGame
        );
        processor.putHandler(
                DiscoverCellRequest.class,
                (Handler<DiscoverCellRequest, CellDiscoveredResponse>) this::discoverCell
        );
        processor.putHandler(
                JoinToGameRequest.class,
                (Handler<JoinToGameRequest, JoinedToGameResponse>) this::jointToGame
        );
    }

    private PongResponse ping(PingRequest request) {
        return PongResponse.getInstance();
    }

    @NotNull
    private GamesInfoResponse getGamesInfo(GamesInfoRequest request) {
        return new GamesInfoResponse(gamesPool.getGamesInfo());
    }

    private GameCreatedResponse createGame(CreateGameRequest request) {
        game = gamesPool.putNewGame(request.getName());
        game.initialize(request.getHeight(), request.getWidth());
        game.subscribeToUpdatedCells(update -> service.send(update));
        return new GameCreatedResponse(game.getContext());
    }

    private CellDiscoveredResponse discoverCell(DiscoverCellRequest request) {
        int i = request.getI();
        int j = request.getJ();
        Answer answer = game.getGuessedPicture().discoverRequest(i, j);
        CellDiscoveredResponse response = new CellDiscoveredResponse(answer);
        if (answer != Answer.NOTHING) {
            game.cellsUpdated(new CellUpdatedNotification(answer, i, j));
        }
        return response;
    }

    private JoinedToGameResponse jointToGame(JoinToGameRequest request) {
        game = gamesPool.get(request.getGameId());
        game.subscribeToUpdatedCells(update -> service.send(update));
        return new JoinedToGameResponse(game.getContext());
    }

    void process(Request request) {
        sendResponse(processor.process(request));
    }

    void setMessageService(RequestService service) {
        this.service = service;
    }

    private void sendResponse(Response response) {
        service.send(response);
    }
}
