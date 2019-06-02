package client;

import message.Message;
import message.response.GameCreatedResponse;
import message.response.GamesInfoResponse;
import message.response.MistakeResponse;
import message.response.SuccessResponse;

import java.io.Serializable;
import java.util.function.Consumer;

public class MessageProcessor {
    private Runnable pongMessageListener;
    private Consumer<GamesInfoResponse> gamesInfoListener;
    private Consumer<GameCreatedResponse> startedGameListener;
    private Consumer<SuccessResponse> successMessageListener;
    private Consumer<MistakeResponse> mistakeMessageListener;

    void process(Message message) {
        Serializable data = message.getData();
        switch (message.getHeader()) {
            case PONG:
                pongMessageListener.run();
                break;
            case GAMES_INFO:
                gamesInfoListener.accept((GamesInfoResponse) data);
                break;
            case GAME_CREATED:
                startedGameListener.accept((GameCreatedResponse) data);
                break;
            case SUCCESS:
                successMessageListener.accept((SuccessResponse) data);
                break;
            case MISTAKE:
                mistakeMessageListener.accept(((MistakeResponse) data));
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

    public void setPongMessageListener(Runnable listener) {
        pongMessageListener = listener;
    }

    public void setGamesInfoListener(Consumer<GamesInfoResponse> listener) {
        gamesInfoListener = listener;
    }
}