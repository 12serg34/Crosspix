package entities;

import message.CellUpdatedNotification;
import pictures.ServerGuessedPicture;
import pictures.StashedPicture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {
    private GameInfo info;
    private StashedPicture stashedPicture;
    private GameContext context;
    private ServerGuessedPicture guessedPicture;
    private List<Consumer<CellUpdatedNotification>> listeners;

    public Game(GameInfo info) {
        this.info = info;
        listeners = new ArrayList<>(4);
    }

    public void initialize(int height, int width) {
        stashedPicture = StashedPicture.generate(height, width);
        guessedPicture = new ServerGuessedPicture(stashedPicture, this);
        context = new GameContext(
                guessedPicture.getField(),
                new Numbers(stashedPicture, NumbersSide.LEFT),
                new Numbers(stashedPicture, NumbersSide.TOP));
    }

    public GameInfo getInfo() {
        return info;
    }

    public ServerGuessedPicture getGuessedPicture() {
        return guessedPicture;
    }

    public GameContext getContext() {
        return context;
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
