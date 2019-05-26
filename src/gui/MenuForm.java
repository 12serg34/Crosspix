package gui;

import client.ClientMessageProcessor;
import client.ClientMessageReceiver;
import client.ClientMessageSender;
import message.Message;
import picture.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

public class MenuForm {
    private static final Dimension PREFERRED_FORM_SIZE = new Dimension(800, 800);

    private JPanel menu;
    private JButton connectButton;
    private JLabel connectLabel;
    private JButton createGameButton;
    private JLabel createGameLabel;
    private JButton startButton;
    private ClientMessageSender sender;
    private StashedPicture stashedPicture;

    MenuForm() {
        ClientMessageProcessor processor = new ClientMessageProcessor();
        processor.setEmptyMessageListener(() -> {
            connectLabel.setText("connected");
        });
        processor.setCreatedGameListener(response -> {
            stashedPicture = response.getStashedPicture();
            createGameLabel.setText("game created "
                    + stashedPicture);
        });

        connectButton.addActionListener(e -> {
            Socket socket = null;
            try {
                socket = new Socket("localhost", 14500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            ClientMessageReceiver.start(socket, processor);

            sender = new ClientMessageSender(socket);
            sender.send(Message.EMPTY);
        });
        createGameButton.addActionListener(e -> {
            System.out.println("Creating game");
            sender.send(Message.CREATE_GAME);
        });
        startButton.addActionListener(e -> {
            GuessedPicture picture = new MultiPlayerGuessedPicture(stashedPicture, sender, processor);
            GameForm gameForm = new GameForm(picture, new Numbers(stashedPicture, NumbersSide.LEFT),
                    new Numbers(stashedPicture, NumbersSide.TOP));
        });
    }

    public static void main(String[] args) throws Exception {
        MenuForm menuForm = new MenuForm();

        JFrame frame = new JFrame("MenuForm");
        frame.setContentPane(menuForm.menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(PREFERRED_FORM_SIZE);
        frame.addWindowListener(menuForm.new FrameWindowListener());
        frame.pack();
        frame.setVisible(true);
    }

    private class FrameWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            sender.send(Message.STOP_SESSION);
        }
    }
}