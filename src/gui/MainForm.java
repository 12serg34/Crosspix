package gui;

import client.ClientMessageProcessor;
import client.ClientMessageReceiver;
import client.ClientMessageSender;
import message.response.GameCreatedResponse;
import message.Message;
import picture.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
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
        answerToColor.put(Answer.WAIT, Color.BLUE);
    }

    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel leftNumbersPanel;
    private JPanel topNumbersPanel;

    private JPanel[][] cells;
    private HashMap<JPanel, Point> cellToPoint;
    private StashedPicture stashedField;
    private MultiPlayerGuessedPicture guessedPicture;
    private int height;
    private int width;
    private ClientMessageSender sender;
    private ClientMessageProcessor processor;

    public static void main(String[] args) throws Exception {
        MainForm mainForm = new MainForm();
        mainForm.connectToServer();
        mainForm.initialize();

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(PREFERRED_FORM_SIZE);
        frame.addWindowListener(mainForm.new FrameWindowListener());
        frame.pack();
        frame.setVisible(true);
    }

    private void connectToServer() throws Exception {
        System.out.println("Client started");

        processor = new ClientMessageProcessor();
        processor.setStartedGameListener(this::startedGameListener);

        Socket socket = new Socket("localhost", 14500);
        ClientMessageReceiver.start(socket, processor);

        sender = new ClientMessageSender(socket);
        sender.send(Message.START_GAME);
        Thread.sleep(5000);
    }

    private void initialize() {
//        height = DEFAULT_FIELD_SIZE.height;
//        width = DEFAULT_FIELD_SIZE.width;
//        stashedField = StashedPicture.generate(height, width);
//        guessedPicture = new LocalGuessedPicture(stashedField);
        guessedPicture = new MultiPlayerGuessedPicture(stashedField, sender, processor);
        guessedPicture.setCompleteListener(this::complete);
        guessedPicture.setUpdatedCellListener(this::cellUpdatedListener);
        initializeLeftNumbers();
        initializeTopNumbers();
        initializeField();
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
                cell.setBackground(Color.WHITE);
                cell.setBorder(lineBorder);
                cells[i][j] = cell;
                cellToPoint.put(cell, new Point(j, i));
                fieldPanel.add(cell);
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

    private void complete() {
        System.out.println("Congratulations!!!");
    }

    private void startedGameListener(GameCreatedResponse response) {
        StashedPicture stashedPicture = response.getStashedPicture();
        stashedField = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
    }

    private void cellUpdatedListener(Answer answer, Point point) {
        cells[point.y][point.x].setBackground(answerToColor.get(answer));
    }

    private class FormMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel cell = (JPanel) e.getComponent();
            Point point = cellToPoint.get(cell);
            Color cellColor = cell.getBackground();
            if (e.getButton() == LEFT_MOUSE_BUTTON) {
                Answer answer = guessedPicture.discoverRequest(point.y, point.x);
                if (answer != Answer.NOTHING) {
                    cellColor = answerToColor.get(answer);
                }
            } else if (e.getButton() == RIGHT_MOUSE_BUTTON) {
                CellState cellState = guessedPicture.toggleEmpty(point.y, point.x);
                cellColor = stateToColor.get(cellState);
            }
            cell.setBackground(cellColor);
        }
    }

    private class FrameWindowListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            sender.send(Message.STOP_SESSION);
        }
    }
}
