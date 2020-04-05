package entities;

import java.io.Serializable;

public class Field implements Serializable {
    private final CellState[][] field;

    public Field(int height, int width) {
        field = new CellState[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = CellState.BLANK;
            }
        }
    }

    public CellState getCellState(int i, int j) {
        return field[i][j];
    }

    public void setCellState(int i, int j, CellState state) {
        field[i][j] = state;
    }
}
