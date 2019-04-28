package picture;

import java.util.Random;

public class StashedPicture {
    private static final Random RANDOM = new Random();

    private int width;
    private int height;
    private boolean[][] picture;
    private int amountOfFullCells;

    private StashedPicture() {

    }

    public StashedPicture(boolean[][] picture) {
        this.height = picture.length;
        this.width = picture[0].length;
        this.picture = picture;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    boolean getCell(int i, int j) {
        return picture[i][j];
    }

    public int getAmountOfFullCells() {
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
            builder.append("\n");
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
}
