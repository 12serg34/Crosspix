package client;

import message.Message;
import message.MessageHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientMessageReader implements Runnable {
    private Socket socket;
    private ClientMessageProcessor proccessor;
    private BufferedReader reader;

    private ClientMessageReader(Socket socket, ClientMessageProcessor processor) {
        this.socket = socket;
        this.proccessor = processor;
        initializeReader();
    }

    private void initializeReader() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(Socket socket, ClientMessageProcessor processor) {
        new Thread(new ClientMessageReader(socket, processor)).start();
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
            System.out.println(message);
            proccessor.process(message);
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
