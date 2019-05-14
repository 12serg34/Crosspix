package picture;

import client.ClientMessageProcessor;
import client.ClientMessageSender;
import message.Message;
import message.MessageHeader;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

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
        processor.setCellUpdatedListener(this::cellUpdated);
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
            sender.send(new Message(MessageHeader.DISCOVER_CELL, asList(i, j)));
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

    private void cellUpdated(Message message) {
        List<Integer> arguments = message.getArguments();
        int i = arguments.get(0);
        int j = arguments.get(1);
        Answer answer = null;
        switch (message.getHeader()) {
            case SUCCESS:
                field[i][j] = CellState.FULL;
                answer = Answer.SUCCESS;
                amountOfSuccesses++;
                break;
            case MISTAKE:
                field[i][j] = CellState.EMPTY;
                answer = Answer.MISTAKE;
                break;
        }
        updatedCellListener.accept(answer, new Point(j, i));
    }

    public void setUpdatedCellListener(BiConsumer<Answer, Point> updatedCellListener) {
        this.updatedCellListener = updatedCellListener;
    }
}
