package field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Numbers {
    private int size;
    private int depth;
    private int[][] numbers;

    public Numbers(Field field, NumbersSide side) {
        count(field, side);
    }

    public int getSize() {
        return size;
    }

    public int getDepth() {
        return depth;
    }

    public int[] getVector(int index) {
        return Arrays.copyOf(numbers[index], numbers[index].length);
    }

    private void count(Field field, NumbersSide side) {
        switch (side) {
            case TOP:
                countTop(field);
                break;
            case LEFT:
                countLeft(field);
                break;
        }
    }

    private void countTop(Field field) {
        depth = 0;
        size = field.getWidth();
        numbers = new int[size][];
        for (int j = 0; j < size; j++) {
            int amount = 0;
            List<Integer> column = new ArrayList<>();

            for (int i = 0; i < field.getHeight(); i++) {
                if (field.getCell(i, j)) {
                    amount++;
                } else {
                    if (amount > 0) {
                        column.add(amount);
                        amount = 0;
                    }
                }
            }
            if (amount > 0) {
                column.add(amount);
            }
            numbers[j] = listToArray(column);
            if (numbers[j].length > depth) {
                depth = numbers[j].length;
            }
        }
    }

    private void countLeft(Field field) {
        depth = 0;
        size = field.getHeight();
        numbers = new int[size][];
        for (int i = 0; i < size; i++) {
            int amount = 0;
            List<Integer> row = new ArrayList<>();

            for (int j = 0; j < field.getWidth(); j++) {
                if (field.getCell(i, j)) {
                    amount++;
                } else {
                    if (amount > 0) {
                        row.add(amount);
                        amount = 0;
                    }
                }
            }
            if (amount > 0) {
                row.add(amount);
            }
            numbers[i] = listToArray(row);
            if (numbers[i].length > depth) {
                depth = numbers[i].length;
            }
        }
    }

    private static int[] listToArray(List<Integer> list) {
        int size = list.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                builder.append(numbers[i][j])
                        .append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
