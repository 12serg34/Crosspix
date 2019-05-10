package picture;

public interface GuessedPicture {

    void setListenerOfComplete(Runnable listener);

    Answer discoverRequest(int i, int j);

    CellState toggleEmpty(int i, int j);
}
