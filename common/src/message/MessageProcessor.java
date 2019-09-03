package message;

import message.response.MistakeResponse;
import message.response.SuccessResponse;

import java.util.function.Consumer;

public interface MessageProcessor {

    void setSuccessMessageListener(Consumer<SuccessResponse> successReceived);

    void setMistakeMessageListener(Consumer<MistakeResponse> mistakeReceived);
}
