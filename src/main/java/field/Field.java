package field;

public class Field {
    private int width;
    private int height;
    private boolean[][] field;

    public Field(int height, int width) {
        this.height = height;
        this.width = width;
        this.field = new boolean[height][width];
    }

    Field(boolean[][] field) {
        this.height = field.length;
        this.width = field[0].length;
        this.field = field;
    }

    public boolean getCell(int i, int j) {
        return field[i][j];
    }

    public void setCell(boolean value, int i, int j) {
        field[i][j] = value;
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
