package server;

import java.util.HashMap;

public class GamesPool {
    private final HashMap<Integer, Game> pool = new HashMap<>(4);
    private int lastId = -1;

    public Game createGame() {
        int id = getNextId();
        Game game = new Game(id);
        pool.put(id, game);
        return game;
    }

    boolean hasGame(Game game) {
        return pool.containsKey(game.getId());
    }

    private synchronized int getNextId() {
        return ++lastId;
    }
}
