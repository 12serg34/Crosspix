package message.request;

import message.Message;
import message.MessageHeader;

import java.io.Serializable;

public final class DiscoverCellRequest implements Serializable {
    private static final long serialVersionUID = 7199493256943566238L;

    private final int i;
    private final int j;

    private DiscoverCellRequest(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static Message encode(int i, int j) {
        return new Message(MessageHeader.DISCOVER_CELL, new DiscoverCellRequest(i, j));
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
