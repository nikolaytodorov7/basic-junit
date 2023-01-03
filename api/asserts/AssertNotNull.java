package api.asserts;

public class AssertNotNull {
    static void assertNotNull(Object actual) {
        assertNotNull(actual, null);
    }

    static void assertNotNull(Object actual, String message) {
        if (actual == null)
            failNull(message);
    }

    private static void failNull(String message) {
        Assertions.fail(AssertionUtils.buildPrefix(message) + "expected: not <null>");
    }
}
