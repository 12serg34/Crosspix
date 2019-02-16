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
    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel leftNumbersPanel;
    private Map<JPanel, Boolean> values;

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
        Field field = FieldGenerator.generate(width, height);
        values = new HashMap<>();
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

        Numbers leftNumbers = new Numbers(field, NumbersSide.Left);
        int size = leftNumbers.getSize();
        int depth = leftNumbers.getDepth();
        leftNumbersPanel.setLayout(new GridLayout(size, depth));
        JPanel[][] grid = new JPanel[size][depth];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < depth; j++) {
                leftNumbersPanel.add(grid[i][j] = new JPanel(new GridLayout(1, 1)));
            }
        }

        for (int s = 0; s < size; s++) {
            int[] vector = leftNumbers.getVector(s);
            for (int l = 0; l < vector.length; l++) {
                JLabel label = new JLabel(String.valueOf(vector[l]));
                label.setFont(new Font("TimesNewRoman", Font.PLAIN, 32));
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                grid[s][depth - vector.length + l].add(label);
            }
        }
    }

    private static Color getColor(boolean value) {
        return value ? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }
}
