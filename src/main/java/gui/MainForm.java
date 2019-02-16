package gui;

import field.Field;
import field.FieldGenerator;
import field.Numbers;
import field.NumbersSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainForm {
    private static final Font DEFAULT_FONT = new Font("TimesNewRoman", Font.PLAIN, 32);
    private static final Dimension PREFERRED_FORM_SIZE = new Dimension(800, 800);
    private static final Dimension DEFAULT_FIELD_SIZE = new Dimension(10, 10);
    private static final Color ERROR_COLOR = Color.RED;
    private static final int INITIAL_ATTEMPTS = 3;

    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel leftNumbersPanel;
    private JPanel topNumbersPanel;
    private Map<JPanel, Point> panelToPoint;

    private Field stashedField;
    private Field guessedField;
    private int attempts;
    private int cellsToOpen;

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.initialize();

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(PREFERRED_FORM_SIZE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initialize() {
        attempts = INITIAL_ATTEMPTS;
        int width = DEFAULT_FIELD_SIZE.width;
        int height = DEFAULT_FIELD_SIZE.height;
        stashedField = FieldGenerator.generate(height, width);
        countCellsToOpen();
        initializeField(height, width, new FormMouseAdapter());
        initializeLeftNumbers(stashedField);
        initializeTopNumbers(stashedField);
    }

    private void countCellsToOpen() {
        cellsToOpen = 0;
        for (int i = 0; i < stashedField.getHeight(); i++) {
            for (int j = 0; j < stashedField.getWidth(); j++) {
                if (stashedField.getCell(i, j)) {
                    cellsToOpen++;
                }
            }
        }
    }

    private void initializeTopNumbers(Field field) {
        Numbers topNumbers = new Numbers(field, NumbersSide.TOP);
        int size = topNumbers.getSize();
        int depth = topNumbers.getDepth();
        JPanel[][] grid = new JPanel[depth][size];
        topNumbersPanel.setLayout(new GridLayout(depth, size));

        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new JPanel(new GridLayout(1, 1));
                topNumbersPanel.add(grid[i][j]);
            }
        }

        for (int s = 0; s < size; s++) {
            int[] vector = topNumbers.getVector(s);
            int shift = depth - vector.length;
            for (int d = 0; d < vector.length; d++) {
                JLabel label = new JLabel(String.valueOf(vector[d]));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setFont(DEFAULT_FONT);
                grid[d + shift][s].add(label);
            }
        }
    }

    private void initializeLeftNumbers(Field field) {
        Numbers leftNumbers = new Numbers(field, NumbersSide.LEFT);
        int size = leftNumbers.getSize();
        int depth = leftNumbers.getDepth();
        JPanel[][] grid = new JPanel[size][depth];
        leftNumbersPanel.setLayout(new GridLayout(size, depth));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < depth; j++) {
                grid[i][j] = new JPanel(new GridLayout(1, 1));
                leftNumbersPanel.add(grid[i][j]);
            }
        }

        for (int s = 0; s < size; s++) {
            int[] vector = leftNumbers.getVector(s);
            int shift = depth - vector.length;
            for (int d = 0; d < vector.length; d++) {
                JLabel label = new JLabel(String.valueOf(vector[d]));
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(DEFAULT_FONT);
                grid[s][d + shift].add(label);
            }
        }
    }

    private void initializeField(int height, int width, MouseAdapter mouseAdapter) {
        panelToPoint = new HashMap<>();
        guessedField = new Field(height, width);
        fieldPanel.setLayout(new GridLayout(height, width));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel();
                panel.addMouseListener(mouseAdapter);
                panel.setBackground(getColor(guessedField.getCell(i, j)));
                panelToPoint.put(panel, new Point(j, i));
                fieldPanel.add(panel);
            }
        }
    }

    private boolean validate(int i, int j) {
        boolean guessedValue = guessedField.getCell(i, j);
        boolean stashedValue = stashedField.getCell(i, j);
        if (guessedValue == stashedValue) {
            if (--cellsToOpen == 0) {
                System.out.println("Congratulations!!!");
            }
            return true;
        } else {
            System.out.println("Bad luck, attempts left: " + --attempts);
            return false;
        }
    }

    private static Color getColor(boolean value) {
        return value ? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }

    private class FormMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel panel = (JPanel) e.getComponent();
            Point point = panelToPoint.get(panel);
            int i = point.y, j = point.x;
            boolean revertValue = !guessedField.getCell(i, j);
            guessedField.setCell(revertValue, i, j);
            if (validate(i, j)) {
                panel.setBackground(getColor(revertValue));
            } else {
                panel.setBackground(ERROR_COLOR);
            }
        }
    }
}
