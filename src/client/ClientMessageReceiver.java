package client;

import message.Message;
import message.MessageHeader;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientMessageReceiver implements Runnable {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private Socket socket;
    private MessageProcessor processor;
    private ObjectInputStream reader;

    private ClientMessageReceiver(Socket socket, MessageProcessor processor) {
        this.socket = socket;
        this.processor = processor;
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

    public static void start(Socket socket, MessageProcessor processor) {
        new Thread(new ClientMessageReceiver(socket, processor)).start();
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
        Message message = Message.EMPTY;
        System.out.println(message);
        while (message.getHeader() != MessageHeader.STOP_SESSION) {
            message = (Message) reader.readObject();
            System.out.println("Got - " + message);
            processor.process(message);
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
