package message;

import entities.Answer;

public class CellUpdatedNotification extends Message {
    private static final long serialVersionUID = -5476441886572684946L;
    private final Answer answer;
    private int i;
    private int j;

    public CellUpdatedNotification(Answer answer, int i, int j) {
        this.answer = answer;
        this.i = i;
        this.j = j;
    }

    public Answer getAnswer() {
        return answer;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
