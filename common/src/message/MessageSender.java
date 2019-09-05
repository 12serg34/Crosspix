package message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageSender {
    private Socket socket;
    private ObjectOutputStream writer;

    public MessageSender(Socket socket) {
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
