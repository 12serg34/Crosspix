package message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifier {
    private Map<Class, List<MessageListener>> listeners;

    public Notifier() {
        listeners = new HashMap<>(4);
    }

    public void notify(final Message message) {
        List<MessageListener> listenerList = listeners.get(message.getClass());
        for (MessageListener listener : listenerList) {
            listener.accept(message);
        }
    }

    public <U extends Message> void subscribe(Class<U> messageType, MessageListener<U> listener) {
        List<MessageListener> listenerList = listeners.get(messageType);
        if (listenerList == null) {
            listenerList = new ArrayList<>(4);
            listeners.put(messageType, listenerList);
        }
        listenerList.add(listener);
    }
}
