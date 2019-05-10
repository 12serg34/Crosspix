package picture;

public class GuessedPicture {
    private final StashedPicture stashedPicture;
    private final CellState[][] field;
    private final int height;
    private final int width;

    private int amountOfSuccesses;
    private Runnable listener;

    public GuessedPicture(StashedPicture stashedPicture) {
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
        this.listener = listener;
    }

    public Answer discoverRequest(int i, int j) {
        Answer answer = Answer.NOTHING;
        if (field[i][j] == CellState.BLANK) {
            if (stashedPicture.getCell(i, j)) {
                field[i][j] = CellState.FULL;
                answer = Answer.SUCCESS;
                amountOfSuccesses++;
                checkOfComplete();
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

    private void checkOfComplete() {
        if (stashedPicture.getAmountOfFullCells() == amountOfSuccesses) {
            listener.run();
        }
    }
}
