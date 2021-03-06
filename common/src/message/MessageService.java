package message;

import java.io.IOException;
import java.net.Socket;

public class MessageService {
    private final String address;
    private final int port;
    private final MessageSender sender;
    private final Notifier notifier;

    public MessageService(String address, int port, MessageSender sender, Notifier notifier) {
        this.sender = sender;
        this.address = address;
        this.port = port;
        this.notifier = notifier;
    }

    public static MessageSender connect(String address, int port, Notifier notifier) {
        MessageSender sender = new MessageSender();
        final MessageService service = new MessageService(address, port, sender, notifier);
        new Thread(new Runnable() {
            @Override
            public void run() {
                service.start();
            }
        }).start();
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
