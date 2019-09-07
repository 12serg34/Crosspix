package message;

import function.Consumer;

public interface MessageListener<T extends Message> extends Consumer<T> {

}
