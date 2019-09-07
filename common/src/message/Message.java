package message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public String toString() {
        return getClass().getSimpleName();
    }
}
