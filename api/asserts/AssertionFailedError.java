package api.asserts;

import static api.asserts.AssertionUtils.buildPrefix;
import static api.asserts.AssertionUtils.format;

public class AssertionFailedError extends AssertionError {
    public AssertionFailedError(String message) {
        super(buildPrefix(message));
    }

    public AssertionFailedError(String message, Object expected, Object actual) {
        super(buildPrefix(message) + format(expected, actual, message));
    }

    public String toString() {
        String className = this.getClass().getName();
        String message = this.getLocalizedMessage();
        return "".equals(message) ? className : className + ": " + message;
    }
}