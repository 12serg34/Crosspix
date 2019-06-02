package message;

import java.io.Serializable;
import java.util.*;

import static java.util.Collections.emptyList;

public class Message implements Serializable {
    public static final Message EMPTY = new Message(MessageHeader.EMPTY, emptyList());
    public static final Message PING = new Message(MessageHeader.PING, emptyList());
    public static final Message PONG = new Message(MessageHeader.PONG, emptyList());
    public static final Message GET_GAMES_LIST = new Message(MessageHeader.GET_GAMES_LIST, emptyList());
    public static final Message CREATE_GAME = new Message(MessageHeader.CREATE_GAME, emptyList());
    public static final Message STOP_SESSION = new Message(MessageHeader.STOP_SESSION, emptyList());

    private MessageHeader header;
    private List<Integer> arguments;

    public Message(MessageHeader header, List<Integer> arguments) {
        this.header = header;
        this.arguments = arguments;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public List<Integer> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public static Message parse(String line) {
        String[] parts = line.split(" ");
        int length = parts.length;
        if (length == 0) {
            return EMPTY;
        }
        MessageHeader header = MessageHeader.valueOf(parts[0]);
        ArrayList<Integer> arguments = new ArrayList<>(length - 1);
        for (int i = 1; i < length; i++) {
            arguments.add(Integer.parseInt(parts[i]));
        }
        return new Message(header, arguments);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(header).append(" ");
        arguments.forEach(x -> builder.append(x).append(" "));
        return builder.toString();
    }
}
