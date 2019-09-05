package message.request;

public class PingRequest implements Request {
    private static final long serialVersionUID = 4401586269516294059L;
    private static final PingRequest instance;

    static {
        instance = new PingRequest();
    }

    private PingRequest() {

    }

    public static PingRequest getInstance() {
        return instance;
    }
}
