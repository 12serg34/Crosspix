package message.request;

public class CreateGameRequest extends Request {
    private static final long serialVersionUID = 3968728408768908001L;

    private final String name;
    private final int height;
    private final int width;

    public CreateGameRequest(String name, int height, int width) {
        this.name = name;
        this.height = height;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
