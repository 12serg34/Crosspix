package picture;

import message.MessageSender;
import message.response.ResponseNotifier;
import message.request.DiscoverCellRequest;
import message.CellUpdatedNotification;

import java.awt.*;
import java.util.function.BiConsumer;

public class MultiPlayerGuessedPicture implements GuessedPicture {
    private final StashedPicture stashedPicture;
    private final CellState[][] field;
    private final int height;
    private final int width;
    private ResponseNotifier notifier;
    private MessageSender sender;

    private int amountOfSuccesses;
    private Runnable completeListener;
    private BiConsumer<Answer, Point> updatedCellListener;

    public MultiPlayerGuessedPicture(StashedPicture stashedPicture,
                                     MessageSender sender,
                                     ResponseNotifier notifier) {
        this.sender = sender;
        this.stashedPicture = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
        this.notifier = notifier;
        notifier.subscribe(CellUpdatedNotification.class, this::cellUpdateReceived);
        field = new CellState[height][width];
        initializeField();
    }

    private void initializeField() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = CellState.BLANK;
            }
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setCompleteListener(Runnable listener) {
        completeListener = listener;
    }

    public Answer discoverRequest(int i, int j) {
        if (field[i][j] == CellState.BLANK) {
            sender.send(new DiscoverCellRequest(i, j));
            return Answer.WAIT;
        }
        return Answer.NOTHING;
    }

    public CellState toggleEmpty(int i, int j) {
        switch (field[i][j]) {
            case BLANK:
                field[i][j] = CellState.EMPTY;
                break;
            case EMPTY:
                field[i][j] = CellState.BLANK;
        }
        return field[i][j];
    }

    private void tryOfComplete() {
        if (stashedPicture.getAmountOfFullCells() == amountOfSuccesses) {
            completeListener.run();
        }
    }

    private void cellUpdateReceived(CellUpdatedNotification response) {
        int i = response.getI();
        int j = response.getJ();
        Answer answer = response.getAnswer();
        CellState state = CellState.BLANK;
        if (answer == Answer.SUCCESS) {
            state = CellState.FULL;
            amountOfSuccesses++;
        }
        if (answer == Answer.MISTAKE) {
            state = CellState.EMPTY;

        }
        field[i][j] = state;
        updatedCellListener.accept(answer, new Point(j, i));
    }

    public void setUpdatedCellListener(BiConsumer<Answer, Point> updatedCellListener) {
        this.updatedCellListener = updatedCellListener;
    }
}
