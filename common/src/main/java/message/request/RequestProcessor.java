package message.request;

import message.Handler;
import message.request.Request;
import message.response.Response;

import java.util.HashMap;
import java.util.Map;

public class RequestProcessor {
    private final Map<Class<? extends Request>, Handler> handlers;

    public RequestProcessor() {
        handlers = new HashMap<>(4);
    }

    public Response process(Request request) {
        return handlers.get(request.getClass()).handle(request);
    }

    public <T extends Request> void putHandler(Class<T> requestType, Handler<T, ? extends Response> handler) {
        handlers.put(requestType, handler);
    }
}
