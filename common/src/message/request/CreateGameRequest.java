package message.request;

import message.Message;
import message.MessageHeader;

import java.io.Serializable;

public final class CreateGameRequest implements Serializable {
    private static final long serialVersionUID = 3968728408768908001L;

    private final String name;

    private CreateGameRequest(String name) {
        this.name = name;
    }

    public static Message pack(String name) {
        return new Message(MessageHeader.CREATE_GAME, new CreateGameRequest(name));
    }

    public String getName() {
        return name;
    }
}
