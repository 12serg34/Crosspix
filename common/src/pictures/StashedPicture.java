package pictures;

import java.io.Serializable;
import java.util.Random;

public class StashedPicture implements Serializable {
    private static final long serialVersionUID = 6046359270249856133L;
    private static final Random RANDOM = new Random();

    private int height;
    private int width;
    private boolean[][] picture;
    private int amountOfFullCells;

    private StashedPicture() {

    }

    public StashedPicture(boolean[][] picture) {
        height = picture.length;
        width = picture[0].length;
        this.picture = picture;
        countFullCells();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getCell(int i, int j) {
        return picture[i][j];
    }

    int getAmountOfFullCells() {
        return amountOfFullCells;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                builder.append(picture[i][j] ? "1" : "0")
                        .append(" ");
            }
        }
        return builder.toString();
    }

    public static StashedPicture generate(int height, int width) {
        StashedPicture result = new StashedPicture();
        result.width = width;
        result.height = height;
        result.picture = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (RANDOM.nextBoolean()) {
                    result.picture[i][j] = true;
                    result.amountOfFullCells++;
                }
            }
        }
        return result;
    }

    private void countFullCells() {
        amountOfFullCells = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (picture[i][j]) {
                    amountOfFullCells++;
                }
            }
        }
    }

}
