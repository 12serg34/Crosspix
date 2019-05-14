package server;

import message.Message;
import message.MessageHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MessageService implements Runnable {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final Socket socket;
    private ServerMessageProcessor processor;
    private BufferedReader reader;
    private OutputStreamWriter writer;

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }

    private void initialize() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), CHARSET));
        writer = new OutputStreamWriter(socket.getOutputStream(), CHARSET);
    }

    private Message waitNextMessage() throws IOException {
        Message message = Message.parse(reader.readLine());
        System.out.println("Got - " + message);
        return message;
    }

    void send(Message message) {
        try {
            writer.write(message + "\n");
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
