package message.response;

import entities.GameInfo;

import java.util.List;

public class GamesInfoResponse extends Response {
    private static final long serialVersionUID = -4033899657080877796L;
    private final List<GameInfo> gamesInfo;

    public GamesInfoResponse(List<GameInfo> gamesInfo) {
        this.gamesInfo = gamesInfo;
    }

    public List<GameInfo> getGamesInfo() {
        return gamesInfo;
    }
}
