package message.request;

import message.Message;
import message.MessageHeader;

import java.util.List;

import static java.util.Arrays.asList;

public class DiscoverCellRequest {
    int i;
    int j;

    public DiscoverCellRequest(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static Message encode(int i, int j) {
        return new Message(MessageHeader.DISCOVER_CELL, asList(i, j));
    }

    public static DiscoverCellRequest decode(Message message) {
        List<Integer> arguments = message.getArguments();
        return new DiscoverCellRequest(arguments.get(0), arguments.get(1));
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
