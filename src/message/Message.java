package message;

import java.io.Serializable;
import java.util.*;

public class Message implements Serializable {
    private static final long serialVersionUID = 5034602183071924396L;

    public static final Message EMPTY = new Message(MessageHeader.EMPTY, null);
    public static final Message PING = new Message(MessageHeader.PING, null);
    public static final Message PONG = new Message(MessageHeader.PONG, null);
    public static final Message GET_GAMES_LIST = new Message(MessageHeader.GET_GAMES_LIST, null);
    public static final Message CREATE_GAME = new Message(MessageHeader.CREATE_GAME, null);
    public static final Message STOP_SESSION = new Message(MessageHeader.STOP_SESSION, null);

    private MessageHeader header;
    private Serializable data;

    public Message(MessageHeader header, Serializable data) {
        this.header = header;
        this.data = data;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public Serializable getData() {
        return data;
    }

    @Override
    public String toString() {
        return header.toString();
    }
}
