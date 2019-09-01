package message.response;

import message.Message;
import message.MessageHeader;
import picture.StashedPicture;

import java.io.Serializable;

public class JoinToGameResponse implements Serializable {
    private static final long serialVersionUID = 7873540238231988321L;

    private final StashedPicture stashedPicture;

    private JoinToGameResponse(StashedPicture stashedPicture) {
        this.stashedPicture = stashedPicture;
    }

    public static Message pack(StashedPicture picture) {
        return new Message(MessageHeader.JOINED_TO_GAME, new JoinToGameResponse(picture));
    }

    public StashedPicture getStashedPicture() {
        return stashedPicture;
    }
}
