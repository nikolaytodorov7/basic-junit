package api.exceptions;

public class PreconditionViolationException extends RuntimeException {
    public PreconditionViolationException(String message) {
        super(message);
    }

    public PreconditionViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
