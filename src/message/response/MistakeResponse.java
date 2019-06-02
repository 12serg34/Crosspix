package message.response;

import message.Message;
import message.MessageHeader;

import java.io.Serializable;

public final class MistakeResponse implements Serializable {
    private static final long serialVersionUID = 2748669154366163017L;

    private final int i;
    private final int j;

    private MistakeResponse(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static Message pack(int i, int j) {
        return new Message(MessageHeader.MISTAKE, new MistakeResponse(i, j));
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
