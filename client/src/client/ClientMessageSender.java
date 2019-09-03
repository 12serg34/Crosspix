package client;

import message.Message;
import message.MessageSender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientMessageSender implements MessageSender {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private Socket socket;
    private ObjectOutputStream writer;

    public ClientMessageSender(Socket socket) {
        this.socket = socket;
        initialize();
    }

    private void initialize() {
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            writer.writeObject(message);
            writer.flush();
            System.out.println("Sent - " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
