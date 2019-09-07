package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class MessageReceiver {
    private Socket socket;
    private ObjectInputStream reader;
    private Notifier notifier;

    MessageReceiver(Socket socket, Notifier notifier) {
        this.socket = socket;
        this.notifier = notifier;
        initialize();
    }

    private void initialize() {
        try {
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void start() {
        try {
            startImpl();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void startImpl() throws IOException, ClassNotFoundException {
        Message message = (Message) reader.readObject();
        System.out.println("Got - " + message);
        notifier.notify(message);
        while (!(message instanceof SessionStoppedNotification)) {
            message = (Message) reader.readObject();
            System.out.println("Got - " + message);
            notifier.notify(message);
        }
    }

    private void close() {
        try {
            socket.close();
            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
