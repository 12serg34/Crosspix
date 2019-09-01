package experiment;

public class RequestAHandler extends RequestHandler<RequestA> {
    RequestAHandler() {
        this(RequestA.class);
    }

    private RequestAHandler(Class<RequestA> requestType) {
        super(requestType);
    }

    @Override
    public void handle(RequestA request) {
        System.out.println("Handle A request: ...");
    }
}
