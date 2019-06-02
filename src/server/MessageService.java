package server;

import message.Message;
import message.MessageHeader;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MessageService implements Runnable {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final Socket socket;
    private ServerMessageProcessor processor;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    MessageService(Socket socket, ServerMessageProcessor processor) {
        this.socket = socket;
        this.processor = processor;
        processor.setMessageService(this);
    }

    static MessageService start(Socket socket, ServerMessageProcessor processor) {
        MessageService service = new MessageService(socket, processor);
        new Thread(service).start();
        return service;
    }

    public void run() {
        try {
            initialize();
            Message message = waitNextMessage();
            while (message.getHeader() != MessageHeader.STOP_SESSION) {
                processor.process(message);
                message = waitNextMessage();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }

    private void initialize() throws IOException {
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());
    }

    private Message waitNextMessage() throws IOException, ClassNotFoundException {
        Message message = (Message) reader.readObject();
        System.out.println("Got - " + message);
        return message;
    }

    void send(Message message) {
        try {
            writer.writeObject(message);
            writer.flush();
            System.out.println("Sent - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeSession() {
        try {
            send(Message.STOP_SESSION);
            socket.close();
            System.out.println("Closed socket connection with " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
