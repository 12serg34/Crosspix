package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionService implements Runnable {
    private final ServerSocket serverSocket;
    private final GamesPool gamesPool;

    ConnectionService(int port, GamesPool gamesPool) throws IOException {
        serverSocket = new ServerSocket(port);
        this.gamesPool = gamesPool;
    }

    public void run() {
        while (true) {
            try {
                Socket accept = serverSocket.accept();
                System.out.println("Accepted connection from: " + accept.getRemoteSocketAddress());
                new Thread(new MessageService(accept, gamesPool)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
