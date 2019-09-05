package message.response;

import message.response.Response;

@FunctionalInterface
public interface ResponseListener<U extends Response> {
    public void accept(U response);
}
