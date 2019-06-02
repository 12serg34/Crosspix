package server;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class GamesPool {
    private final HashMap<Integer, Game> pool = new HashMap<>(4);
    private int size;

    Game putNewGame(String name) {
        int id = getNextId();
        Game game = new Game(new GameInfo(id, name));
        pool.put(id, game);
        return game;
    }

    List<GameInfo> getGamesInfo() {
        return pool.values().stream()
                .map(Game::getInfo)
                .collect(Collectors.toList());
    }

    private synchronized int getNextId() {
        return size++;
    }
}
