package server;

import message.request.Request;
import message.request.StopSessionRequest;
import message.response.Response;
import message.response.SessionStoppedResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class RequestService implements Runnable {
    private final Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private ServerMessageProcessor processor;

    private RequestService(Socket socket, ServerMessageProcessor processor) {
        this.socket = socket;
        this.processor = processor;
        processor.setMessageService(this);
    }

    static RequestService start(Socket socket, ServerMessageProcessor processor) {
        RequestService service = new RequestService(socket, processor);
        new Thread(service).start();
        return service;
    }

    public void run() {
        try {
            initialize();
            Request request = waitNextRequest();
            while (!(request instanceof StopSessionRequest)) {
                processor.process(request);
                request = waitNextRequest();
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

    private Optional<Request> waitNext() throws IOException, ClassNotFoundException {
        Object object = reader.readObject();
        if (object instanceof Request) {
            System.out.println("Got - " + object);
            return Optional.of(((Request) object));
        }
        System.out.println("Error: Got not a request - " + object);
        return Optional.empty();
    }

    private Request waitNextRequest() throws IOException, ClassNotFoundException {
        Optional<Request> optional = waitNext();
        while (!optional.isPresent()) {
            optional = waitNext();
        }
        return optional.get();
    }

    void send(Response response) {
        try {
            writer.writeObject(response);
            writer.flush();
            System.out.println("Sent - " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeSession() {
        try {
            send(SessionStoppedResponse.getInstance());
            socket.close();
            System.out.println("Closed socket connection with " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
