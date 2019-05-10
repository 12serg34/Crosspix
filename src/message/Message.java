package message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class Message {
    public static final Message EMPTY = new Message(MessageHeader.EMPTY, emptyList());
    public static final Message START_GAME = new Message(MessageHeader.START_GAME, emptyList());
    public static final Message GAME_CREATED = new Message(MessageHeader.GAME_CREATED, emptyList());
    public static final Message STOP_SESSION = new Message(MessageHeader.STOP_SESSION, emptyList());

    private MessageHeader header;
    private List<Integer> arguments;

    private Message(MessageHeader header, List<Integer> arguments) {
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
        if (parts.length == 0) {
            return EMPTY;
        }
        MessageHeader header = MessageHeader.valueOf(parts[0]);
        ArrayList<Integer> arguments = new ArrayList<>(2);
        switch (header) {
            case FILL_CELL:
                arguments.add(Integer.parseInt(parts[1]));
                arguments.add(Integer.parseInt(parts[2]));
                break;
        }
        return new Message(header, arguments);
    }

    @Override
    public String toString() {
        return header + " " + arguments.toString();
    }
}
