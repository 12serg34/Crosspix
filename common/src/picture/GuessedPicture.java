package picture;

import function.Consumer;
import message.CellUpdatedNotification;

public interface GuessedPicture {

    int getHeight();

    int getWidth();

    void setCompleteListener(Runnable listener);

    Answer discoverRequest(int i, int j);

    CellState toggleEmpty(int i, int j);

    void setUpdatedCellListener(Consumer<CellUpdatedNotification> listener);
}
