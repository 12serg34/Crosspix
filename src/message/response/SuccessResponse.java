package message.response;

import message.Message;
import message.MessageHeader;

import java.util.List;

import static java.util.Arrays.asList;

public class SuccessResponse {
    private int i;
    private int j;

    public SuccessResponse(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static Message encode(int i, int j) {
        return new Message(MessageHeader.SUCCESS, asList(i, j));
    }

    public static SuccessResponse decode(Message message) {
        List<Integer> arguments = message.getArguments();
        return new SuccessResponse(arguments.get(0), arguments.get(1));
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
