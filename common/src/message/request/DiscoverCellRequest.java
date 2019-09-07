package message.request;

public class DiscoverCellRequest extends Request {
    private static final long serialVersionUID = 7199493256943566238L;

    private final int i;
    private final int j;

    public DiscoverCellRequest(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
