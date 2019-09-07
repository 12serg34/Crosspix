package message.request;

public class StopSessionRequest extends Request {
    private static final long serialVersionUID = 4401586269516294059L;
    private static final StopSessionRequest instance;

    static {
        instance = new StopSessionRequest();
    }

    private StopSessionRequest() {

    }

    public static StopSessionRequest getInstance() {
        return instance;
    }
}
