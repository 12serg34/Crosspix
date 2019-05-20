package picture;

import message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StashedPicture {
    private static final Random RANDOM = new Random();
    private static final int SIZE_SHIFT = 2;

    private int height;
    private int width;
    private boolean[][] picture;
    private int amountOfFullCells;

    private StashedPicture() {

    }

    StashedPicture(boolean[][] picture) {
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

    public List<Integer> encode() {
        ArrayList<Integer> result = new ArrayList<>(SIZE_SHIFT + height * width);
        result.add(height);
        result.add(width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.add(cellToInt(picture[i][j]));
            }
        }
        return result;
    }

    public static StashedPicture decode(Message message) {
        StashedPicture result = new StashedPicture();
        List<Integer> arguments = message.getArguments();
        result.height = arguments.get(0);
        result.width = arguments.get(1);
        result.picture = new boolean[result.height][result.width];
        for (int i = 0; i < result.height; i++) {
            for (int j = 0; j < result.width; j++) {
                if (intToCell(arguments.get(SIZE_SHIFT + i * result.width + j))) {
                    result.picture[i][j] = true;
                    result.amountOfFullCells++;
                }
            }
        }
        return result;
    }

    private int cellToInt(boolean cell) {
        return cell ? 1 : 0;
    }

    private static boolean intToCell(int value) {
        return value == 1;
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
