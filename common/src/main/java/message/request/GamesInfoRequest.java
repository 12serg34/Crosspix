package message.request;

public class GamesInfoRequest extends Request {
    private static final long serialVersionUID = 4401586269516294059L;
    private static final GamesInfoRequest instance;

    static {
        instance = new GamesInfoRequest();
    }

    private GamesInfoRequest() {

    }

    public static GamesInfoRequest getInstance() {
        return instance;
    }
}
