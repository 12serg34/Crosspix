package picture;

import client.ClientMessageProcessor;
import client.ClientMessageSender;
import message.request.DiscoverCellRequest;
import message.response.MistakeResponse;
import message.response.SuccessResponse;

import java.awt.*;
import java.util.function.BiConsumer;

public class MultiPlayerGuessedPicture implements GuessedPicture {
    private final StashedPicture stashedPicture;
    private final CellState[][] field;
    private final int height;
    private final int width;
    private ClientMessageProcessor processor;
    private ClientMessageSender sender;

    private int amountOfSuccesses;
    private Runnable completeListener;
    private BiConsumer<Answer, Point> updatedCellListener;

    public MultiPlayerGuessedPicture(StashedPicture stashedPicture,
                                     ClientMessageSender sender,
                                     ClientMessageProcessor processor) {
        this.sender = sender;
        this.stashedPicture = stashedPicture;
        height = stashedPicture.getHeight();
        width = stashedPicture.getWidth();
        this.processor = processor;
        processor.setSuccessMessageListener(this::successReceived);
        processor.setMistakeMessageListener(this::mistakeReceived);
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

    public void setCompleteListener(Runnable listener) {
        completeListener = listener;
    }

    public Answer discoverRequest(int i, int j) {
        if (field[i][j] == CellState.BLANK) {
            sender.send(DiscoverCellRequest.encode(i, j));
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

    private void successReceived(SuccessResponse message) {
        int i = message.getI();
        int j = message.getJ();
        field[i][j] = CellState.FULL;
        amountOfSuccesses++;
        updatedCellListener.accept(Answer.SUCCESS, new Point(j, i));
    }

    private void mistakeReceived(MistakeResponse message) {
        int i = message.getI();
        int j = message.getJ();
        field[i][j] = CellState.EMPTY;
        updatedCellListener.accept(Answer.MISTAKE, new Point(j, i));
    }

    public void setUpdatedCellListener(BiConsumer<Answer, Point> updatedCellListener) {
        this.updatedCellListener = updatedCellListener;
    }
}
