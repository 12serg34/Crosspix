package message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class Message {
    public static final Message EMPTY = new Message(MessageHeader.EMPTY, emptyList());
    public static final Message START_GAME = new Message(MessageHeader.START_GAME, emptyList());
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
