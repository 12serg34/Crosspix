package server;

import message.Message;
import message.MessageHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MessageService implements Runnable {
    private final Socket socket;
    private GamesPool gamesPool;
    private Game game;
    private BufferedReader reader;
    private OutputStreamWriter writer;

    MessageService(Socket socket, GamesPool gamesPool) {
        this.socket = socket;
        this.gamesPool = gamesPool;
    }

    public void run() {
        try {
            initialize();
            Message message = waitNextMessage();
            while (message.getHeader() != MessageHeader.STOP_SESSION) {
                process(message);
                message = waitNextMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }

    private void initialize() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new OutputStreamWriter(socket.getOutputStream());
    }

    private Message waitNextMessage() throws IOException {
        return Message.parse(reader.readLine());
    }

    private void process(Message message) throws IOException {
        switch (message.getHeader()) {
            case START_GAME:
                if (game == null) {
                    game = gamesPool.createGame();
                    write(Message.GAME_CREATED);
                }
                break;
        }
    }

    private void write(Message message) throws IOException {
        writer.write(message + "\n");
        writer.flush();
    }

    private void closeSession() {
        try {
            write(Message.STOP_SESSION);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
