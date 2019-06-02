package server;

import message.Message;
import picture.ServerGuessedPicture;
import picture.StashedPicture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {
    private final int id;
    private StashedPicture stashedPicture;
    private ServerGuessedPicture guessedPicture;
    private final List<Consumer<Message>> listeners;

    Game(int id) {
        this.id = id;
        listeners = new ArrayList<>(3);
    }

    void initialize(int height, int width) {
        stashedPicture = StashedPicture.generate(height, width);
        guessedPicture = new ServerGuessedPicture(stashedPicture, this);
    }

    Integer getId() {
        return id;
    }
    
    ServerGuessedPicture getGuessedPicture() {
        return guessedPicture;
    }

    StashedPicture getStashedPicture() {
        return stashedPicture;
    }

    public void subscribeToUpdateCells(Consumer<Message> listener) {
        listeners.add(listener);
    }

    public void cellsUpdated(Message message) {
        listeners.forEach(x -> x.accept(message));
    }
}
