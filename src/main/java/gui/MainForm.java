package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainForm {
    private JPanel field;
    private JPanel[][] cells;
    private Map<JPanel, Boolean> values;

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.init();

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(mainForm.field);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void init(){

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = (JPanel)e.getComponent();
                boolean revertValue = !values.get(panel);
                values.put(panel, revertValue);
                panel.setBackground(getColor(revertValue));
            }
        };

        int width = 5;
        int height = 5;
        values = new HashMap<>();
        field.setLayout(new GridLayout(height, width));
        cells = new JPanel[height][width];
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JPanel panel = new JPanel();
                panel.addMouseListener(mouseAdapter);
                boolean value = random.nextBoolean();
                values.put(panel, value);
                panel.setBackground(getColor(value));
                field.add(panel);
                cells[i][j] = panel;
            }
        }

    }

    private static Color getColor(boolean value){
        return value? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }
}
