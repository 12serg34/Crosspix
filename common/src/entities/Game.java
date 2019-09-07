package entities;

import function.Consumer;
import message.CellUpdatedNotification;
import picture.ServerGuessedPicture;
import picture.StashedPicture;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameInfo info;
    private StashedPicture stashedPicture;
    private ServerGuessedPicture guessedPicture;
    private List<Consumer<CellUpdatedNotification>> listeners;

    public Game(GameInfo info) {
        this.info = info;
        listeners = new ArrayList<>(4);
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

    public void subscribeToUpdatedCells(Consumer<CellUpdatedNotification> responseListener) {
        listeners.add(responseListener);
    }

    public void cellsUpdated(CellUpdatedNotification update) {
        for (Consumer<CellUpdatedNotification> listener : listeners) {
            listener.accept(update);
        }
    }
}
