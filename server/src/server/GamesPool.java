package server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import entities.Game;
import entities.GameInfo;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class GamesPool {
    private final HashMap<Integer, Game> pool = new HashMap<>(4);
    private int size;

    @NotNull
    Game putNewGame(String name) {
        int id = getNextId();
        Game game = new Game(new GameInfo(id, name));
        pool.put(id, game);
        return game;
    }

    @Nullable
    Game get(int gameId) {
        return pool.get(gameId);
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
