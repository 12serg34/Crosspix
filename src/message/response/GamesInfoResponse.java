package message.response;

import message.Message;
import message.MessageHeader;
import server.GameInfo;

import java.io.Serializable;
import java.util.List;

public final class GamesInfoResponse implements Serializable {
    private static final long serialVersionUID = -4033899657080877796L;

    private final List<GameInfo> gamesInfo;

    private GamesInfoResponse(List<GameInfo> gamesInfo) {
        this.gamesInfo = gamesInfo;
    }

    public static Message pack(List<GameInfo> gamesInfo) {
        return new Message(MessageHeader.GAMES_INFO, new GamesInfoResponse(gamesInfo));
    }

    public List<GameInfo> getGamesInfo() {
        return gamesInfo;
    }
}
