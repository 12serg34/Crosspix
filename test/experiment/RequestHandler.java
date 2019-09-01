package experiment;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RequestHandler<T> {
    private Class<T> requestType;

    RequestHandler(Class<T> requestType) {
        this.requestType = requestType;
    }

    Class<T> getRequestType() {
        return requestType;
    }

    public void handle(T request) {
        throw new UnsupportedOperationException();
    }
}
