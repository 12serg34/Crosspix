package message.response;

public class PongResponse implements Response {
    private static final long serialVersionUID = 4401586269516294059L;
    private static final PongResponse instance;

    static {
        instance = new PongResponse();
    }

    private PongResponse() {

    }

    public static PongResponse getInstance() {
        return instance;
    }
}
