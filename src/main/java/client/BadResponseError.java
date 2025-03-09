package client;

public class BadResponseError extends RuntimeException {
    public BadResponseError(String message) {
        super(message);
    }
}
