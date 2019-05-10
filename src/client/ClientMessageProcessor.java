package client;

import message.Message;
import picture.StashedPicture;

import java.util.function.Consumer;

public class ClientMessageProcessor {
    private Consumer<StashedPicture> stashedPictureConsumer;

    void process(Message message) {
        switch (message.getHeader()) {
            case GAME_STARTED:
                stashedPictureConsumer.accept(StashedPicture.parse(message));
        }
    }

    public void setCreateGameListener(Consumer<StashedPicture> listener) {
        stashedPictureConsumer = listener;
    }
}
