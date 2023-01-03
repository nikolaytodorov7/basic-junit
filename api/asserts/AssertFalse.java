package api.asserts;


import static api.asserts.AssertionUtils.*;

public class AssertFalse {
    static void assertFalse(boolean condition) {
        assertFalse(condition, null);
    }

    static void assertFalse(boolean condition, String message) {
        if (condition)
            fail(buildPrefix(message) + "expected: <false> but was: <true>", false, true);
    }
}
