package picture;

import function.Consumer;
import message.CellUpdatedNotification;
import message.MessageSender;
import message.request.DiscoverCellRequest;
import message.MessageListener;
import message.Notifier;

public class MultiPlayerGuessedPicture implements GuessedPicture {
    private final StashedPicture stashedPicture;
    private final CellState[][] field;
    private final int height;
    private final int width;
    private MessageSender sender;

    private int amountOfSuccesses;
    private Runnable completeListener;
    private Consumer<CellUpdatedNotification> updatedCellListener;

    public MultiPlayerGuessedPicture(StashedPicture stashedPicture,
                                     MessageSender sender,
                                     Notifier notifier) {
        this.sender = sender;
        this.stashedPicture = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
        notifier.subscribe(CellUpdatedNotification.class, new MessageListener<CellUpdatedNotification>() {
            @Override
            public void accept(CellUpdatedNotification response) {
                MultiPlayerGuessedPicture.this.cellUpdateReceived(response);
            }
        });
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
        updatedCellListener.accept(new CellUpdatedNotification(answer, i, j));
    }

    public void setUpdatedCellListener(Consumer<CellUpdatedNotification> updatedCellListener) {
        this.updatedCellListener = updatedCellListener;
    }
}
