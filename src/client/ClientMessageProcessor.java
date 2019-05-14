package client;

import message.Message;

import java.util.function.Consumer;

public class ClientMessageProcessor {
    private Consumer<Message> startedGameListener;
    private Consumer<Message> cellUpdatedListener;

    void process(Message message) {
        switch (message.getHeader()) {
            case GAME_STARTED:
                startedGameListener.accept(message);
                break;
            case SUCCESS:
            case MISTAKE:
                cellUpdatedListener.accept(message);
                break;
        }
    }

    public void setStartedGameListener(Consumer<Message> listener) {
        startedGameListener = listener;
    }

    public void setCellUpdatedListener(Consumer<Message> cellUpdatedListener) {
        this.cellUpdatedListener = cellUpdatedListener;
    }
}
