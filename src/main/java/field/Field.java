package field;

public class Field {
    private int width;
    private int height;
    private boolean[][] field;

    Field(boolean[][] field) {
        this.height = field.length;
        this.width = field[0].length;
        this.field = field;
    }

    public boolean getCell(int i, int j) {
        return field[i][j];
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                builder.append(field[i][j] ? "1" : "0")
                        .append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
