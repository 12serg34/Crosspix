package message.response;

import message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifier {
    private Map<Class, List<MessageListener>> listeners;

    public Notifier() {
        listeners = new HashMap<>(4);
    }

    public void notify(Message message) {
        listeners.get(message.getClass()).forEach(messageListener -> messageListener.accept(message));
    }

    public <U extends Message> void subscribe(Class<U> messageType, MessageListener<U> listener) {
        listeners.computeIfAbsent(messageType, type -> new ArrayList<>(4)).add(listener);
    }
}
