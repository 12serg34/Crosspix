package message.response;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ResponseReceiver implements Runnable {
    private Socket socket;
    private ObjectInputStream reader;
    private ResponseNotifier notifier;

    private ResponseReceiver(Socket socket, ResponseNotifier notifier) {
        this.socket = socket;
        this.notifier = notifier;
        initialize();
    }

    private void initialize() {
        try {
            InputStream inputStream = socket.getInputStream();
            reader = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(Socket socket, ResponseNotifier notifier) {
        new Thread(new ResponseReceiver(socket, notifier)).start();
    }

    public void run() {
        try {
            runImpl();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void runImpl() throws IOException, ClassNotFoundException {
        Response response = (Response) reader.readObject();
        System.out.println("Got - " + response);
        notifier.notify(response);
        while (!(response instanceof SessionStoppedResponse)) {
            response = (Response) reader.readObject();
            System.out.println("Got - " + response);
            notifier.notify(response);
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
