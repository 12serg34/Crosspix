package client;

import message.Message;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientMessageSender {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private Socket socket;
    private OutputStreamWriter writer;

    public ClientMessageSender(Socket socket) {
        this.socket = socket;
        initialize();
    }

    private void initialize() {
        try {
            writer = new OutputStreamWriter(socket.getOutputStream(), CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            writer.write(message + "\n");
            writer.flush();
            System.out.println("Sent - " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
