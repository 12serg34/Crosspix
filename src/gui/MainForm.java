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
    private static final Dimension DEFAULT_FIELD_SIZE = new Dimension(5, 5);
    private static final Color FULL_CELL_COLOR = Color.BLACK;
    private static final Color EMPTY_CELL_COLOR = Color.GRAY;
    private static final Color MISTAKE_COLOR = Color.RED;
    private static final Color BLANK_COLOR = Color.WHITE;
    private static final int INITIAL_ATTEMPTS = 3;

    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel leftNumbersPanel;
    private JPanel topNumbersPanel;
    private Map<JPanel, Point> panelToPoint;

    private Field stashedField;
    private Field guessedField;
    private int attempts;
    private int discoveredCells;

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
        initializeLeftNumbers();
        initializeTopNumbers();
        initializeField(height, width, new FormMouseAdapter());
    }

    private void initializeField(int height, int width, MouseAdapter mouseAdapter) {
        panelToPoint = new HashMap<>();
        guessedField = new Field(height, width);
        fieldPanel.setLayout(new GridLayout(height, width));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel();
                panel.addMouseListener(mouseAdapter);
                panel.setBackground(BLANK_COLOR);
                panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                panelToPoint.put(panel, new Point(j, i));
                fieldPanel.add(panel);
            }
        }
    }

    private void initializeLeftNumbers() {
        Numbers leftNumbers = new Numbers(stashedField, NumbersSide.LEFT);
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

    private void initializeTopNumbers() {
        Numbers topNumbers = new Numbers(stashedField, NumbersSide.TOP);
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

    private boolean tryToOpenCell(int i, int j) {
        if (stashedField.getCell(i, j)) {
            guessedField.setCell(true, i, j);
            if (++discoveredCells == stashedField.getAmountOfFullCells()) {
                System.out.println("Congratulations!!!");
            }
            return true;
        } else {
            System.out.println("Bad luck, attempts left: " + --attempts);
            return false;
        }
    }

    private class FormMouseAdapter extends MouseAdapter {

        private final int leftMouseButton = MouseEvent.BUTTON1;
        private final int rightMouseButton = MouseEvent.BUTTON3;

        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel panel = (JPanel) e.getComponent();
            Point point = panelToPoint.get(panel);
            Color background = panel.getBackground();
            if (e.getButton() == leftMouseButton) {
                if (background == BLANK_COLOR) {
                    panel.setBackground(tryToOpenCell(point.y, point.x) ? FULL_CELL_COLOR : MISTAKE_COLOR);
                }
            } else if (e.getButton() == rightMouseButton) {
                if (background == BLANK_COLOR) {
                    panel.setBackground(EMPTY_CELL_COLOR);
                } else if (background == EMPTY_CELL_COLOR) {
                    panel.setBackground(BLANK_COLOR);
                }
            }
        }
    }
}
