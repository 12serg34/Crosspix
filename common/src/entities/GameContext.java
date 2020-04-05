package entities;

import java.io.Serializable;

public class GameContext implements Serializable {
    private final Field field;
    private final Numbers leftNumbers;
    private final Numbers topNumbers;


    public GameContext(Field field, Numbers leftNumbers, Numbers topNumbers) {
        this.field = field;
        this.leftNumbers = leftNumbers;
        this.topNumbers = topNumbers;
    }

    public Field getField() {
        return field;
    }

    public Numbers getLeftNumbers() {
        return leftNumbers;
    }

    public Numbers getTopNumbers() {
        return topNumbers;
    }
}
