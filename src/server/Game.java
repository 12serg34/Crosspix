package server;

import message.Message;
import picture.ServerGuessedPicture;
import picture.StashedPicture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {
    private GameInfo info;
    private StashedPicture stashedPicture;
    private ServerGuessedPicture guessedPicture;
    private final List<Consumer<Message>> listeners;

    Game(GameInfo info) {
        this.info = info;
        listeners = new ArrayList<>(3);
    }

    void initialize(int height, int width) {
        stashedPicture = StashedPicture.generate(height, width);
        guessedPicture = new ServerGuessedPicture(stashedPicture, this);
    }

    GameInfo getInfo() {
        return info;
    }
    
    ServerGuessedPicture getGuessedPicture() {
        return guessedPicture;
    }

    StashedPicture getStashedPicture() {
        return stashedPicture;
    }

    void subscribeToUpdateCells(Consumer<Message> listener) {
        listeners.add(listener);
    }

    void cellsUpdated(Message message) {
        listeners.forEach(x -> x.accept(message));
    }
}
