package message;

import message.response.Notifier;

import java.io.IOException;
import java.net.Socket;

public class MessageService {
    private String address;
    private int port;
    private MessageSender sender;
    private Notifier notifier;

    public MessageService(String address, int port, MessageSender sender, Notifier notifier) {
        this.sender = sender;
        this.address = address;
        this.port = port;
        this.notifier = notifier;
    }

    public static MessageSender connect(String address, int port, Notifier notifier) {
        MessageSender sender = new MessageSender();
        MessageService service = new MessageService(address, port, sender, notifier);
        new Thread(service::start).start();
        return sender;
    }

    private void start() {
        try (Socket socket = new Socket(address, port)) {
            sender.init(socket);
            new MessageReceiver(socket, notifier).start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
