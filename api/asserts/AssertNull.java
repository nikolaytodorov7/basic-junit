package api.asserts;

public class AssertNull {
    static void assertNull(Object actual) {
        assertNull(actual, null);
    }

    static void assertNull(Object actual, String message) {
        if (actual != null)
            failNotNull(actual, message);
    }

    private static void failNotNull(Object actual, String message) {
        String stringRepresentation = actual.toString();
        if (stringRepresentation != null && !stringRepresentation.equals("null")) {
            String msg = AssertionUtils.buildPrefix(message) + String.format("expected: <null> but was: <%s>", actual);
            AssertionUtils.fail(msg, null, actual);
        } else
            AssertionUtils.fail(AssertionUtils.format(null, actual, message), null, actual);
    }
}
