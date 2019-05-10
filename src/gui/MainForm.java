package gui;

import picture.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.EnumMap;
import java.util.HashMap;

public class MainForm {
    private static final Font DEFAULT_FONT = new Font("TimesNewRoman", Font.PLAIN, 32);
    private static final Dimension PREFERRED_FORM_SIZE = new Dimension(800, 800);
    private static final Dimension DEFAULT_FIELD_SIZE = new Dimension(5, 5);
    private static final int LEFT_MOUSE_BUTTON = MouseEvent.BUTTON1;
    private static final int RIGHT_MOUSE_BUTTON = MouseEvent.BUTTON3;
    private static final EnumMap<CellState, Color> stateToColor;
    private static final EnumMap<Answer, Color> answerToColor;

    static {
        stateToColor = new EnumMap<>(CellState.class);
        stateToColor.put(CellState.BLANK, Color.WHITE);
        stateToColor.put(CellState.EMPTY, Color.GRAY);

        answerToColor = new EnumMap<>(Answer.class);
        answerToColor.put(Answer.SUCCESS, Color.BLACK);
        answerToColor.put(Answer.MISTAKE, Color.RED);
    }

    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel leftNumbersPanel;
    private JPanel topNumbersPanel;

    private HashMap<JPanel, Point> cellToPoint;
    private StashedPicture stashedField;
    private GuessedPicture guessedPicture;

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
        int width = DEFAULT_FIELD_SIZE.width;
        int height = DEFAULT_FIELD_SIZE.height;
        stashedField = StashedPicture.generate(height, width);
        guessedPicture = new GuessedPicture(stashedField);
        initializeLeftNumbers();
        initializeTopNumbers();
        initializeField(height, width);
    }

    private void initializeField(int height, int width) {
        cellToPoint = new HashMap<>(height * width);
        fieldPanel.setLayout(new GridLayout(height, width));
        MouseListener mouseAdapter = new FormMouseAdapter();
        Border lineBorder = BorderFactory.createLineBorder(Color.YELLOW);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel();
                panel.addMouseListener(mouseAdapter);
                panel.setBackground(Color.WHITE);
                panel.setBorder(lineBorder);
                cellToPoint.put(panel, new Point(j, i));
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
        GridLayout layout = new GridLayout(1, 1);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < depth; j++) {
                grid[i][j] = new JPanel(layout);
                leftNumbersPanel.add(grid[i][j]);
            }
        }

        for (int i = 0; i < size; i++) {
            int[] vector = leftNumbers.getVector(i);
            int length = vector.length;
            int shift = depth - length;
            for (int j = 0; j < length; j++) {
                JLabel label = new JLabel(String.valueOf(vector[j]));
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(DEFAULT_FONT);
                grid[i][j + shift].add(label);
            }
        }
    }

    private void initializeTopNumbers() {
        Numbers topNumbers = new Numbers(stashedField, NumbersSide.TOP);
        int size = topNumbers.getSize();
        int depth = topNumbers.getDepth();
        JPanel[][] grid = new JPanel[depth][size];
        topNumbersPanel.setLayout(new GridLayout(depth, size));
        GridLayout layout = new GridLayout(1, 1);
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new JPanel(layout);
                topNumbersPanel.add(grid[i][j]);
            }
        }

        for (int i = 0; i < size; i++) {
            int[] vector = topNumbers.getVector(i);
            int length = vector.length;
            int shift = depth - length;
            for (int j = 0; j < length; j++) {
                JLabel label = new JLabel(String.valueOf(vector[j]));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setFont(DEFAULT_FONT);
                grid[j + shift][i].add(label);
            }
        }
    }

    private void tryToComplete(Answer answer) {
        if (answer == Answer.SUCCESS && guessedPicture.getAmountOfSuccesses() == stashedField.getAmountOfFullCells()) {
            System.out.println("Congratulations!!!");
        }
    }

    private class FormMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel cell = (JPanel) e.getComponent();
            Point point = cellToPoint.get(cell);
            Color cellColor = cell.getBackground();
            if (e.getButton() == LEFT_MOUSE_BUTTON) {
                Answer answer = guessedPicture.discoverRequest(point.y, point.x);
                cellColor = answerToColor.get(answer);
                tryToComplete(answer);
            } else if (e.getButton() == RIGHT_MOUSE_BUTTON) {
                CellState cellState = guessedPicture.toggleEmpty(point.y, point.x);
                cellColor = stateToColor.get(cellState);
            }
            cell.setBackground(cellColor);
        }
    }
}
