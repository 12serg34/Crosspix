package message.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseNotifier {
    private Map<Class, List<ResponseListener>> listeners;

    public ResponseNotifier() {
        listeners = new HashMap<>(4);
    }

    public void notify(Response response) {
        listeners.get(response.getClass()).forEach(responseListener -> responseListener.accept(response));
    }

    public <U extends Response> void subscribe(Class<U> responseType, ResponseListener<U> responseListener) {
        listeners.computeIfAbsent(responseType, type -> new ArrayList<>(4)).add(responseListener);
    }
}
