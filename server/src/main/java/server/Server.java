package server;

import java.io.IOException;
import java.util.logging.Logger;

class Server {
    private static final int PORT = 14500;

    public static void main(String[] args) throws IOException {
        new Server().run();
    }

    private void run() throws IOException {
        GamesPool gamesPool = new GamesPool();
        new Thread(new ConnectionService(PORT, gamesPool)).start();
        Logger.getGlobal().info("Server started");
    }
}
