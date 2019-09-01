package message.request;

import message.Message;
import message.MessageHeader;
import server.GameInfo;

import java.io.Serializable;

public final class JoinToGameRequest implements Serializable {
    private static final long serialVersionUID = 54283151306590918L;

    private final int gameId;

    private JoinToGameRequest(int gameId) {
        this.gameId = gameId;
    }

    public static Message pack(int gameId) {
        return new Message(MessageHeader.JOIN_TO_GAME, new JoinToGameRequest(gameId));
    }

    public int getGameId() {
        return gameId;
    }
}
