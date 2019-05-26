package client;

import message.response.GameCreatedResponse;
import message.Message;
import message.response.MistakeResponse;
import message.response.SuccessResponse;

import java.util.function.Consumer;

public class ClientMessageProcessor {
    private Runnable emptyMessageListener;
    private Consumer<GameCreatedResponse> startedGameListener;
    private Consumer<SuccessResponse> successMessageListener;
    private Consumer<MistakeResponse> mistakeMessageListener;

    void process(Message message) {
        switch (message.getHeader()) {
            case EMPTY:
                emptyMessageListener.run();
                break;
            case GAME_CREATED:
                startedGameListener.accept(GameCreatedResponse.decode(message));
                break;
            case SUCCESS:
                successMessageListener.accept(SuccessResponse.decode(message));
                break;
            case MISTAKE:
                mistakeMessageListener.accept(MistakeResponse.decode(message));
                break;
        }
    }

    public void setCreatedGameListener(Consumer<GameCreatedResponse> listener) {
        startedGameListener = listener;
    }

    public void setSuccessMessageListener(Consumer<SuccessResponse> listener) {
        successMessageListener = listener;
    }

    public void setMistakeMessageListener(Consumer<MistakeResponse> listener) {
        mistakeMessageListener = listener;
    }

    public void setEmptyMessageListener(Runnable listener) {
        emptyMessageListener = listener;
    }
}
