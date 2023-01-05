package api.errors;

public class AssertionFailedError extends AssertionError {
    public AssertionFailedError(String message) {
        super(message);
    }

    public AssertionFailedError(String message, Throwable throwable) {
        super(message, throwable);
    }

    public String toString() {
        String className = this.getClass().getName();
        String message = this.getLocalizedMessage();
        return "".equals(message) ? className : className + ": " + message;
    }
}