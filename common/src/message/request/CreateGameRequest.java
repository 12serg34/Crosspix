package message.request;

public class CreateGameRequest implements Request {
    private static final long serialVersionUID = 3968728408768908001L;

    private final String name;

    public CreateGameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
