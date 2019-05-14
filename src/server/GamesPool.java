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

    public boolean hasGame() {
        return lastId == 0;
    }

    public Game getExistGame() {
        return pool.get(0);
    }

    private synchronized int getNextId() {
        return ++lastId;
    }
}
