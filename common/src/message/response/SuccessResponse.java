package message.response;

import message.Message;
import message.MessageHeader;

import java.io.Serializable;

public final class SuccessResponse implements Serializable {
    private static final long serialVersionUID = -7724755614117162193L;

    private final int i;
    private final int j;

    private SuccessResponse(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static Message pack(int i, int j) {
        return new Message(MessageHeader.SUCCESS, new SuccessResponse(i, j));
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
