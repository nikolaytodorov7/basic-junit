package api.asserts;

public class AssertTrue {
    static void assertTrue(boolean condition) {
        assertTrue(condition, null);
    }

    static void assertTrue(boolean condition, String message) {
        if (!condition) {
            String msg = String.format(AssertionUtils.buildPrefix(message) + "expected: <true> but was: <false>");
            AssertionUtils.fail(msg, true, false);
        }
    }
}
