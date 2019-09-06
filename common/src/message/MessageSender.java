package message;

import message.request.PingRequest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageSender {
    private ObjectOutputStream writer;

    void init(Socket socket) {
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            send(PingRequest.getInstance());
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
