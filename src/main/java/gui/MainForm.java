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
    private static final int FONT_SIZE = 32;
    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel leftNumbersPanel;
    private JPanel topNumbersPanel;

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.init();

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void init() {
        final Map<JPanel, Boolean> values = new HashMap<>();

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = (JPanel) e.getComponent();
                boolean revertValue = !values.get(panel);
                values.put(panel, revertValue);
                panel.setBackground(getColor(revertValue));
            }
        };

        int width = 5;
        int height = 5;
        Field field = FieldGenerator.generate(height, width);
        fieldPanel.setLayout(new GridLayout(height, width));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel();
                panel.addMouseListener(mouseAdapter);
                boolean value = field.getCell(i, j);
                values.put(panel, value);
                panel.setBackground(getColor(value));
                fieldPanel.add(panel);
            }
        }

        Numbers leftNumbers = new Numbers(field, NumbersSide.LEFT);
        int size = leftNumbers.getSize();
        int depth = leftNumbers.getDepth();
        leftNumbersPanel.setLayout(new GridLayout(size, depth));
        JPanel[][] grid = new JPanel[size][depth];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < depth; j++) {
                grid[i][j] = new JPanel(new GridLayout(1, 1));
                leftNumbersPanel.add(grid[i][j]);
            }
        }

        for (int s = 0; s < size; s++) {
            int[] vector = leftNumbers.getVector(s);
            for (int d = 0; d < vector.length; d++) {
                JLabel label = new JLabel(String.valueOf(vector[d]));
                label.setFont(new Font("TimesNewRoman", Font.PLAIN, FONT_SIZE));
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                grid[s][depth - vector.length + d].add(label);
            }
        }

        Numbers topNumbers = new Numbers(field, NumbersSide.TOP);
        size = topNumbers.getSize();
        depth = topNumbers.getDepth();
        topNumbersPanel.setLayout(new GridLayout(depth, size));
        grid = new JPanel[depth][size];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new JPanel(new GridLayout(1, 1));
                topNumbersPanel.add(grid[i][j]);
            }
        }

        for (int s = 0; s < size; s++) {
            int[] vector = topNumbers.getVector(s);
            for (int d = 0; d < vector.length; d++) {
                JLabel label = new JLabel(String.valueOf(vector[d]));
                label.setFont(new Font("TimesNewRoman", Font.PLAIN, FONT_SIZE));
                label.setVerticalAlignment(SwingConstants.BOTTOM);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                grid[depth - vector.length + d][s].add(label);
            }
        }
    }

    private static Color getColor(boolean value) {
        return value ? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }
}
