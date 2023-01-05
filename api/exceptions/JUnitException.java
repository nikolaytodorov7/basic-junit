package api.exceptions;

public class JUnitException extends Exception{
    public JUnitException() {
        super();
    }

    public JUnitException(String message) {
        super(message);
    }

    public JUnitException(String message, Throwable cause) {
        super(message, cause);
    }
}
