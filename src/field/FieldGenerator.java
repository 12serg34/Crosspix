package field;

import java.util.Random;

public class FieldGenerator {
    private static final Random random = new Random();

    public static Field generate(int height, int width) {
        boolean[][] field = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = random.nextBoolean();
            }
        }
        return new Field(field);
    }
}
