package message;

import java.util.function.Consumer;

public interface MessageListener<T extends Message> extends Consumer<T> {

}
