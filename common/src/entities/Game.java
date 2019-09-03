package entities;

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

    public Game(GameInfo info) {
        this.info = info;
        listeners = new ArrayList<>(3);
    }

    public void initialize(int height, int width) {
        stashedPicture = StashedPicture.generate(height, width);
        guessedPicture = new ServerGuessedPicture(stashedPicture, this);
    }

    public GameInfo getInfo() {
        return info;
    }

    public ServerGuessedPicture getGuessedPicture() {
        return guessedPicture;
    }

    public StashedPicture getStashedPicture() {
        return stashedPicture;
    }

    public void subscribeToUpdateCells(Consumer<Message> listener) {
        listeners.add(listener);
    }

    public void cellsUpdated(Message message) {
        listeners.forEach(x -> x.accept(message));
    }
}
