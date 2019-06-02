package server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class GamesPool {
    private final HashMap<Integer, Game> pool = new HashMap<>(4);
    private int size;

    Game createGame() {
        int id = getNextId();
        Game game = new Game(id);
        pool.put(id, game);
        return game;
    }

    boolean isEmpty() {
        return size == 0;
    }

    public Game getExistGame() {
        return pool.get(0);
    }

    Collection<Game> getGamesList() {
        return Collections.unmodifiableCollection(pool.values());
    }

    private synchronized int getNextId() {
        return size++;
    }
}
