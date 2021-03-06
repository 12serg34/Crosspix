import entities.Answer;
import entities.CellState;
import entities.Numbers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumMap;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import message.CellUpdatedNotification;
import pictures.GuessedPicture;

public class GameForm {
    private static final Font DEFAULT_FONT = new Font("TimesNewRoman", Font.PLAIN, 32);
    private static final Dimension PREFERRED_FORM_SIZE = new Dimension(800, 800);
    private static final int LEFT_MOUSE_BUTTON = MouseEvent.BUTTON1;
    private static final int RIGHT_MOUSE_BUTTON = MouseEvent.BUTTON3;
    private static final EnumMap<CellState, Color> stateToColor;
    private static final EnumMap<Answer, Color> answerToColor;
    private static final Color WAIT_COLOR = Color.BLUE;

    static {
        stateToColor = new EnumMap<>(CellState.class);
        stateToColor.put(CellState.FULL, Color.BLACK);
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

    private final GuessedPicture guessedPicture;
    private final int height;
    private final int width;
    private final Numbers leftNumbers;
    private final Numbers topNumbers;
    private JPanel[][] cells;
    private HashMap<JPanel, Point> cellToPoint;

    GameForm(GuessedPicture picture, Numbers leftNumbers, Numbers topNumbers) {
        guessedPicture = picture;
        guessedPicture.setUpdatedCellListener(this::updateCell);
        height = leftNumbers.getSize();
        width = topNumbers.getSize();
        this.leftNumbers = leftNumbers;
        this.topNumbers = topNumbers;
        initializeLeftNumbers();
        initializeTopNumbers();
        initializeField();

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(mainPanel);
        frame.setPreferredSize(PREFERRED_FORM_SIZE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initializeField() {
        cells = new JPanel[height][width];
        cellToPoint = new HashMap<>(height * width);
        fieldPanel.setLayout(new GridLayout(height, width));
        MouseListener mouseAdapter = new FormMouseAdapter();
        Border lineBorder = BorderFactory.createLineBorder(Color.YELLOW);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel cell = new JPanel();
                cell.addMouseListener(mouseAdapter);
                cell.setBackground(stateToColor.get(guessedPicture.getCellState(i, j)));
                cell.setBorder(lineBorder);
                cells[i][j] = cell;
                cellToPoint.put(cell, new Point(j, i));
                fieldPanel.add(cell);
            }
        }
    }

    private void initializeLeftNumbers() {
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

    private void updateCell(CellUpdatedNotification notification) {
        Answer answer = notification.getAnswer();
        if (answer != Answer.NOTHING) {
            cells[notification.getI()][notification.getJ()].setBackground(answerToColor.get(answer));
        }
    }

    private class FormMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel cell = (JPanel) e.getComponent();
            Point point = cellToPoint.get(cell);
            Color cellColor = cell.getBackground();
            if (e.getButton() == LEFT_MOUSE_BUTTON) {
                if (guessedPicture.getCellState(point.y, point.x) == CellState.BLANK) {
                    guessedPicture.discoverRequest(point.y, point.x);
                    cellColor = WAIT_COLOR;
                }
            } else if (e.getButton() == RIGHT_MOUSE_BUTTON) {
                CellState cellState = guessedPicture.toggleEmpty(point.y, point.x);
                cellColor = stateToColor.get(cellState);
            }
            cell.setBackground(cellColor);
        }
    }
}
