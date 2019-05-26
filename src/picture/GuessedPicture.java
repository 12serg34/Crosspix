package picture;

import java.awt.*;
import java.util.function.BiConsumer;

public interface GuessedPicture {

    int getHeight();

    int getWidth();

    void setCompleteListener(Runnable listener);

    Answer discoverRequest(int i, int j);

    CellState toggleEmpty(int i, int j);

    default void setUpdatedCellListener(BiConsumer<Answer, Point> listener) {

    }
}
