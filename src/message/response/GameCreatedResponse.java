package message.response;

import message.Message;
import message.MessageHeader;
import picture.StashedPicture;

import java.io.Serializable;

public final class GameCreatedResponse implements Serializable {
    private static final long serialVersionUID = -5476441886572684946L;

    private final StashedPicture stashedPicture;

    private GameCreatedResponse(StashedPicture stashedPicture) {
        this.stashedPicture = stashedPicture;
    }

    public static Message encode(StashedPicture picture) {
        return new Message(MessageHeader.GAME_CREATED, new GameCreatedResponse(picture));
    }

    public StashedPicture getStashedPicture() {
        return stashedPicture;
    }
}
