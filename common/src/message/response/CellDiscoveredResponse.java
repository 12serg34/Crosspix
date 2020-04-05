package message.response;

import entities.Answer;

public class CellDiscoveredResponse extends Response {
    private static final long serialVersionUID = -5476441886572684946L;
    private final Answer answer;

    public CellDiscoveredResponse(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }
}
