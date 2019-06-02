package server;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private static final long serialVersionUID = -7253887370367231510L;

    private final int id;
    private final String name;

    GameInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ") " + name;
    }
}
