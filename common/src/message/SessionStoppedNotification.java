package message;

public class SessionStoppedNotification extends Message {
    private static final long serialVersionUID = 4401586269516294059L;
    private static final SessionStoppedNotification instance;

    static {
        instance = new SessionStoppedNotification();
    }

    private SessionStoppedNotification() {

    }

    public static SessionStoppedNotification getInstance() {
        return instance;
    }
}
