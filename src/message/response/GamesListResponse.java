package message.response;

import message.Message;
import message.MessageHeader;
import picture.StashedPicture;

public class GamesListResponse {
    private final int gameId;
    private final String gameName;

    private GamesListResponse(int gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public static Message encode(int gameId, String gameName) {
        return new Message(MessageHeader.GAMES_LIST, null);
    }

    public static GamesListResponse decode(Message message) {
        return new GamesListResponse(0, "");
    }

    public int getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }
}
