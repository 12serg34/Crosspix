package pictures;

import entities.Answer;
import entities.CellState;
import entities.Field;
import entities.Game;
import function.Consumer;
import message.CellUpdatedNotification;

public class ServerGuessedPicture {
    private final StashedPicture stashedPicture;
    private final Field field;
    private final int height;
    private final int width;
    private Game game;

    private int amountOfDiscoveredCells;
    private Runnable listenerOfComplete;

    public ServerGuessedPicture(StashedPicture stashedPicture, Game game) {
        this.stashedPicture = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
        field = new Field(height, width);
        this.game = game;
    }

    public Field getField() {
        return field;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setCompleteListener(Runnable listener) {
        listenerOfComplete = listener;
    }

    public Answer discoverRequest(int i, int j) {
        if (field.getCellState(i, j) == CellState.BLANK) {
            if (stashedPicture.getCell(i, j)) {
                field.setCellState(i, j, CellState.FULL);
                amountOfDiscoveredCells++;
                tryOfComplete();
                return Answer.SUCCESS;
            } else {
                field.setCellState(i, j, CellState.EMPTY);
                return Answer.MISTAKE;
            }
        }
        return Answer.NOTHING;
    }

    public void setUpdatedCellListener(Consumer<CellUpdatedNotification> listener) {

    }

    private void tryOfComplete() {
        if (stashedPicture.getAmountOfFullCells() == amountOfDiscoveredCells) {
            if (listenerOfComplete != null) {
                listenerOfComplete.run();
            }
        }
    }
}
