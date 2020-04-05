package pictures;

import entities.Answer;
import entities.CellState;
import entities.Field;
import function.Consumer;
import message.CellUpdatedNotification;
import message.MessageListener;
import message.MessageSender;
import message.Notifier;
import message.request.DiscoverCellRequest;

public class GuessedPicture {
    private final Field field;
    private MessageSender sender;
    private Consumer<CellUpdatedNotification> updatedCellListener;

    public GuessedPicture(Field field,
                          MessageSender sender,
                          Notifier notifier) {
        this.sender = sender;
        this.field = field;
        notifier.subscribe(CellUpdatedNotification.class, new MessageListener<CellUpdatedNotification>() {
            @Override
            public void accept(CellUpdatedNotification response) {
                GuessedPicture.this.cellUpdateReceived(response);
            }
        });
    }

    public CellState getCellState(int i, int j) {
        return field.getCellState(i, j);
    }

    public void discoverRequest(int i, int j) {
        sender.send(new DiscoverCellRequest(i, j));
    }

    public CellState toggleEmpty(int i, int j) {
        switch (field.getCellState(i, j)) {
            case BLANK:
                field.setCellState(i, j, CellState.EMPTY);
                break;
            case EMPTY:
                field.setCellState(i, j, CellState.BLANK);
        }
        return field.getCellState(i, j);
    }

    private void cellUpdateReceived(CellUpdatedNotification response) {
        int i = response.getI();
        int j = response.getJ();
        Answer answer = response.getAnswer();
        CellState state = CellState.BLANK;
        if (answer == Answer.SUCCESS) {
            state = CellState.FULL;
        }
        if (answer == Answer.MISTAKE) {
            state = CellState.EMPTY;
        }
        field.setCellState(i, j, state);
        updatedCellListener.accept(new CellUpdatedNotification(answer, i, j));
    }

    public void setUpdatedCellListener(Consumer<CellUpdatedNotification> updatedCellListener) {
        this.updatedCellListener = updatedCellListener;
    }
}
