package message.response;

import entities.GameContext;

public class GameCreatedResponse extends Response {
    private final GameContext gameContext;

    public GameCreatedResponse(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public GameContext getGameContext() {
        return gameContext;
    }
}
