package message.response;

import entities.GameContext;

public class JoinedToGameResponse extends Response {
    private final GameContext gameContext;

    public JoinedToGameResponse(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public GameContext getGameContext() {
        return gameContext;
    }
}
