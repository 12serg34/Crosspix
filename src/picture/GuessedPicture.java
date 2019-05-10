package picture;

public class GuessedPicture {
    private int width;
    private int height;
    private CellState[][] field;
    private int amountOfSuccesses;
    private int amountOfMistakes;
    private StashedPicture stashedPicture;

    public GuessedPicture(StashedPicture stashedPicture) {
        this.height = stashedPicture.getHeight();
        this.width = stashedPicture.getWidth();
        this.stashedPicture = stashedPicture;
        this.field = new CellState[height][width];
        initializeField();
    }

    private void initializeField() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = CellState.BLANK;
            }
        }
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    public int getAmountOfSuccesses() {
        return amountOfSuccesses;
    }

    public CellState getCell(int i, int j) {
        return field[i][j];
    }

    public Answer discoverRequest(int i, int j) {
        Answer answer = Answer.NOTHING;
        if (field[i][j] == CellState.BLANK) {
            if (stashedPicture.getCell(i, j)) {
                field[i][j] = CellState.FULL;
                answer = Answer.SUCCESS;
                amountOfSuccesses++;
            } else {
                field[i][j] = CellState.EMPTY;
                answer = Answer.MISTAKE;
                amountOfMistakes++;
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
}
