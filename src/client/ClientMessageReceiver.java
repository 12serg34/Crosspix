package client;

import message.Message;
import message.MessageHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientMessageReceiver implements Runnable {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private Socket socket;
    private ClientMessageProcessor processor;
    private BufferedReader reader;

    private ClientMessageReceiver(Socket socket, ClientMessageProcessor processor) {
        this.socket = socket;
        this.processor = processor;
        initialize();
    }

    private void initialize() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(Socket socket, ClientMessageProcessor processor) {
        new Thread(new ClientMessageReceiver(socket, processor)).start();
    }

    public void run() {
        try {
            runImpl();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void runImpl() throws IOException {
        Message message = Message.EMPTY;
        System.out.println(message);
        while (message.getHeader() != MessageHeader.STOP_SESSION) {
            message = Message.parse(reader.readLine());
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
