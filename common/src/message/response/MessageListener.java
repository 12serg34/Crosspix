package message.response;

import function.Consumer;
import message.Message;

public interface MessageListener<T extends Message> extends Consumer<T> {

}
