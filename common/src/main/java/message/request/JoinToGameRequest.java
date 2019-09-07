package message.request;

public class JoinToGameRequest extends Request {
    private static final long serialVersionUID = 54283151306590918L;
    private final int gameId;

    public JoinToGameRequest(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }
}
