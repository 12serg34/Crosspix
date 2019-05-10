package picture;

public class LocalGuessedPicture implements GuessedPicture {
    private final StashedPicture stashedPicture;
    private final CellState[][] field;
    private final int height;
    private final int width;

    private int amountOfSuccesses;
    private Runnable listenerOfComplete;

    public LocalGuessedPicture(StashedPicture stashedPicture) {
        this.stashedPicture = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
        field = new CellState[height][width];
        initializeField();
    }

    private void initializeField() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = CellState.BLANK;
            }
        }
    }

    public void setListenerOfComplete(Runnable listener) {
        listenerOfComplete = listener;
    }

    public Answer discoverRequest(int i, int j) {
        Answer answer = Answer.NOTHING;
        if (field[i][j] == CellState.BLANK) {
            if (stashedPicture.getCell(i, j)) {
                field[i][j] = CellState.FULL;
                answer = Answer.SUCCESS;
                amountOfSuccesses++;
                tryOfComplete();
            } else {
                field[i][j] = CellState.EMPTY;
                answer = Answer.MISTAKE;
            }
        }
        return answer;
    }

    public CellState toggleEmpty(int i, int j) {
        switch (field[i][j]) {
            case BLANK:
                field[i][j] = CellState.EMPTY;
                break;
            case EMPTY:
                field[i][j] = CellState.BLANK;
        }
        return field[i][j];
    }

    private void tryOfComplete() {
        if (stashedPicture.getAmountOfFullCells() == amountOfSuccesses) {
            listenerOfComplete.run();
        }
    }
}
