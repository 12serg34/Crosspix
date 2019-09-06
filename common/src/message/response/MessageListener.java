package message.response;

import message.Message;

@FunctionalInterface
public interface MessageListener<U extends Message> {
    public void accept(U message);
}
