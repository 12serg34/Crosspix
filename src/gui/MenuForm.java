package gui;

import client.MessageProcessor;
import client.ClientMessageReceiver;
import client.ClientMessageSender;
import message.Message;
import message.request.CreateGameRequest;
import message.request.JoinToGameRequest;
import picture.*;
import server.GameInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class MenuForm {
    private static final Dimension PREFERRED_FORM_SIZE = new Dimension(800, 800);

    private JPanel menu;
    private JButton connectButton;
    private JLabel connectLabel;
    private JButton createGameButton;
    private JLabel pictureLabel;
    private JButton startButton;
    private JList gamesList;
    private JTextField gameNameTextField;
    private JButton refreshGamesListButton;
    private JButton joinButton;
    private ClientMessageSender sender;
    private StashedPicture stashedPicture;
    private List<GameInfo> gamesInfo;

    MenuForm() {
        MessageProcessor processor = new MessageProcessor();
        processor.setPongMessageListener(() -> {
            connectLabel.setText("connected");
            sender.send(Message.GET_GAMES_INFO);
        });
        processor.setCreatedGameListener(response -> {
            stashedPicture = response.getStashedPicture();
            pictureLabel.setText("game created "
                    + stashedPicture);
        });
        processor.setJoinedToGameListener(response -> {
            stashedPicture = response.getStashedPicture();
            pictureLabel.setText("joined to game "
                    + stashedPicture);
        });
        processor.setGamesInfoListener(response -> {
            gamesInfo = response.getGamesInfo();
            gamesList.setListData(gamesInfo.toArray());
        });

        connectButton.addActionListener(e -> {
            Socket socket = null;
            try {
                socket = new Socket("localhost", 14500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            createSenderAndStartReceiver(socket, processor);
            sender.send(Message.PING);
        });
        refreshGamesListButton.addActionListener(e -> {
            sender.send(Message.GET_GAMES_INFO);
        });
        createGameButton.addActionListener(e -> {
            System.out.println("Creating game");
            sender.send(CreateGameRequest.pack(gameNameTextField.getText()));
        });
        startButton.addActionListener(e -> {
            GuessedPicture picture = new MultiPlayerGuessedPicture(stashedPicture, sender, processor);
            GameForm gameForm = new GameForm(picture, new Numbers(stashedPicture, NumbersSide.LEFT),
                    new Numbers(stashedPicture, NumbersSide.TOP));
        });
        joinButton.addActionListener(e -> {
            int selectedIndex = gamesList.getSelectedIndex();
            sender.send(JoinToGameRequest.pack(gamesInfo.get(selectedIndex).getId()));
        });
    }

    private void createSenderAndStartReceiver(Socket socket, MessageProcessor processor) {
        sender = new ClientMessageSender(socket);
        ClientMessageReceiver.start(socket, processor);
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
            try {
                sender.send(Message.STOP_SESSION);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
