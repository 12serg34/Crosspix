package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from: " + socket.getRemoteSocketAddress());
                ServerMessageProcessor processor = new ServerMessageProcessor(gamesPool);
                RequestService.start(socket, processor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
