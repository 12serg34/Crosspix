import entities.GameContext;
import entities.GameInfo;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import message.MessageSender;
import message.MessageService;
import message.Notifier;
import message.SessionStoppedNotification;
import message.request.CreateGameRequest;
import message.request.GamesInfoRequest;
import message.request.JoinToGameRequest;
import message.request.StopSessionRequest;
import message.response.CellDiscoveredResponse;
import message.response.GameCreatedResponse;
import message.response.GamesInfoResponse;
import message.response.JoinedToGameResponse;
import message.response.PongResponse;
import pictures.GuessedPicture;

public class MenuForm {
    private static final Dimension PREFERRED_FORM_SIZE = new Dimension(800, 800);

    private JPanel menu;
    private JButton connectButton;
    private JLabel connectLabel;
    private JButton createGameButton;
    private JLabel pictureLabel;
    private JButton startButton;
    private JList<GameInfo> gamesList;
    private JTextField gameNameTextField;
    private JButton refreshGamesListButton;
    private JButton joinButton;
    private MessageSender sender;
    private GameContext gameContext;
    private List<GameInfo> gamesInfo;

    MenuForm() {
        Notifier notifier = new Notifier();
        notifier.subscribe(PongResponse.class, pong -> {
            connectLabel.setText("connected");
            sender.send(GamesInfoRequest.getInstance());
        });
        notifier.subscribe(GameCreatedResponse.class, response -> gameContext = response.getGameContext());
        notifier.subscribe(JoinedToGameResponse.class, response -> gameContext = response.getGameContext());
        notifier.subscribe(GamesInfoResponse.class, response -> {
            gamesInfo = response.getGamesInfo();
            gamesList.setListData(gamesInfo.toArray(new GameInfo[]{}));
        });
        notifier.subscribe(CellDiscoveredResponse.class, response -> {
        });
        notifier.subscribe(SessionStoppedNotification.class, response -> {
        });

        Configuration configuration = new Configuration();
        configuration.readProperties();
        connectButton.addActionListener(e -> {
            sender = MessageService.connect(configuration.getHost(), configuration.getPort(), notifier);
        });

        refreshGamesListButton.addActionListener(e -> sender.send(GamesInfoRequest.getInstance()));
        createGameButton.addActionListener(e -> {
            System.out.println("Creating game");
            sender.send(new CreateGameRequest(gameNameTextField.getText(), 5, 5));
        });
        startButton.addActionListener(e -> {
            new GameForm(
                    new GuessedPicture(gameContext.getField(), sender, notifier),
                    gameContext.getLeftNumbers(),
                    gameContext.getTopNumbers());
        });
        joinButton.addActionListener(e -> {
            int selectedIndex = gamesList.getSelectedIndex();
            sender.send(new JoinToGameRequest(gamesInfo.get(selectedIndex).getId()));
        });
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
