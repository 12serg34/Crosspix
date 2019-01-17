package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm {
    private JPanel field;
    private boolean value;

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
        field.setBackground(getColor(value));
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.setBackground(getColor(value = !value));
            }
        });
    }

    private static Color getColor(boolean value){
        return value? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }
}
