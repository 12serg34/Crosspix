package message;

import message.request.Request;
import message.response.Response;

@FunctionalInterface
public interface Handler<T extends Request, U extends Response> {
    public U handle(T request);
}
