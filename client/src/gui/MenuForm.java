package gui;

import client.ResponseReceiver;
import entities.GameInfo;
import message.CellUpdatedNotification;
import message.MessageSender;
import message.response.ResponseNotifier;
import message.request.*;
import message.response.*;
import picture.*;

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
    private MessageSender sender;
    private StashedPicture stashedPicture;
    private List<GameInfo> gamesInfo;

    MenuForm() {
        ResponseNotifier notifier = new ResponseNotifier();
        notifier.subscribe(PongResponse.class, pong -> {
            connectLabel.setText("connected");
            sender.send(message.request.GamesInfoRequest.getInstance());
        });
        notifier.subscribe(GameCreatedResponse.class, response -> {
            stashedPicture = response.getStashedPicture();
            pictureLabel.setText("game created "
                    + stashedPicture);
        });
        notifier.subscribe(JoinedToGameResponse.class, response -> {
            stashedPicture = response.getStashedPicture();
            pictureLabel.setText("joined to game "
                    + stashedPicture);
        });
        notifier.subscribe(GamesInfoResponse.class, response -> {
            gamesInfo = response.getGamesInfo();
            gamesList.setListData(gamesInfo.toArray());
        });
        notifier.subscribe(CellDiscoveredResponse.class, response -> {
        });
        notifier.subscribe(CellUpdatedNotification.class, response -> {

        });

        connectButton.addActionListener(e -> {
            Socket socket = null;
            try {
                socket = new Socket("localhost", 14500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            createSenderAndStartReceiver(socket, notifier);
            sender.send(PingRequest.getInstance());
        });
        refreshGamesListButton.addActionListener(e -> sender.send(GamesInfoRequest.getInstance()));
        createGameButton.addActionListener(e -> {
            System.out.println("Creating game");
            sender.send(new CreateGameRequest(gameNameTextField.getText()));
        });
        startButton.addActionListener(e -> {
            GuessedPicture picture = new MultiPlayerGuessedPicture(stashedPicture, sender, notifier);
            GameForm gameForm = new GameForm(picture, new Numbers(stashedPicture, NumbersSide.LEFT),
                    new Numbers(stashedPicture, NumbersSide.TOP));
        });
        joinButton.addActionListener(e -> {
            int selectedIndex = gamesList.getSelectedIndex();
            sender.send(new JoinToGameRequest(gamesInfo.get(selectedIndex).getId()));
        });
    }

    private void createSenderAndStartReceiver(Socket socket, ResponseNotifier notifier) {
        sender = new MessageSender(socket);
        ResponseReceiver.start(socket, notifier);
    }

    public static void main(String[] args) {
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
                sender.send(StopSessionRequest.getInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
