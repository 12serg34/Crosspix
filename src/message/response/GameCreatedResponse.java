package message.response;

import message.Message;
import message.MessageHeader;
import picture.StashedPicture;

public class GameCreatedResponse {
    private final StashedPicture stashedPicture;

    public GameCreatedResponse(StashedPicture stashedPicture) {
        this.stashedPicture = stashedPicture;
    }

    public static Message encode(StashedPicture picture) {
        return new Message(MessageHeader.GAME_CREATED, picture.encode());
    }

    public static GameCreatedResponse decode(Message message) {
        return new GameCreatedResponse(StashedPicture.decode(message));
    }

    public StashedPicture getStashedPicture() {
        return stashedPicture;
    }
}
