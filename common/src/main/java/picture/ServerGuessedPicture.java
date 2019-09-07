package picture;

import entities.Game;

public class ServerGuessedPicture implements GuessedPicture {
    private final StashedPicture stashedPicture;
    private final CellState[][] field;
    private final int height;
    private final int width;
    private Game game;

    private int amountOfDiscoveredCells;
    private Runnable listenerOfComplete;

    public ServerGuessedPicture(StashedPicture stashedPicture, Game game) {
        this.stashedPicture = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
        this.game = game;
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

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setCompleteListener(Runnable listener) {
        listenerOfComplete = listener;
    }

    public Answer discoverRequest(int i, int j) {
        if (field[i][j] == CellState.BLANK) {
            if (stashedPicture.getCell(i, j)) {
                field[i][j] = CellState.FULL;
                amountOfDiscoveredCells++;
                tryOfComplete();
                return Answer.SUCCESS;
            } else {
                field[i][j] = CellState.EMPTY;
                return Answer.MISTAKE;
            }
        }
        return Answer.NOTHING;
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
        if (stashedPicture.getAmountOfFullCells() == amountOfDiscoveredCells) {
            if (listenerOfComplete != null) {
                listenerOfComplete.run();
            }
        }
    }
}
