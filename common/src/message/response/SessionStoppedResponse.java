package message.response;

public class SessionStoppedResponse implements Response {
    private static final long serialVersionUID = 4401586269516294059L;
    private static final SessionStoppedResponse instance;

    static {
        instance = new SessionStoppedResponse();
    }

    private SessionStoppedResponse() {

    }

    public static SessionStoppedResponse getInstance() {
        return instance;
    }
}
