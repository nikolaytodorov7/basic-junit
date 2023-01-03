package api.asserts;

public class Assertions {
    public static <V> V fail(String message) {
        AssertionUtils.fail(message);
        return null;
    }

    public static void assertEquals(byte expected, byte actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(byte expected, byte actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(short expected, short actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(short expected, short actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(int expected, int actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(int expected, int actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(long expected, long actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(long expected, long actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(float expected, float actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(float expected, float actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(double expected, double actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(double expected, double actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(double expected, double actual, double delta) {
        AssertEquals.assertEquals(expected, actual, delta);
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        AssertEquals.assertEquals(expected, actual, delta, message);
    }

    public static void assertEquals(char expected, char actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(char expected, char actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertEquals(String expected, String actual) {
        AssertEquals.assertEquals(expected, actual);
    }

    public static void assertEquals(String expected, String actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }

    public static void assertTrue(boolean condition) {
        AssertTrue.assertTrue(condition);
    }

    public static void assertTrue(boolean condition, String message) {
        AssertTrue.assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition) {
        AssertFalse.assertFalse(condition);
    }

    public static void assertFalse(boolean condition, String message) {
        AssertFalse.assertFalse(condition, message);
    }

    public static void assertNull(Object actual) {
        AssertNull.assertNull(actual);
    }

    public static void assertNull(Object actual, String message) {
        AssertNull.assertNull(actual, message);
    }

    public static void assertNotNull(Object actual) {
        AssertNotNull.assertNotNull(actual);
    }

    public static void assertNotNull(Object actual, String message) {
        AssertNotNull.assertNotNull(actual, message);
    }
}
