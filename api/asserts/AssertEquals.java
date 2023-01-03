package api.asserts;

import static api.asserts.AssertionUtils.*;

class AssertEquals {
    static void assertEquals(byte expected, byte actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(byte expected, byte actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(char expected, char actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(char expected, char actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(double expected, double actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(double expected, double actual, String message) {
        if (!doublesAreEqual(expected, actual))
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(double expected, double actual, double delta) {
        assertEquals(expected, actual, delta, null);
    }

    static void assertEquals(double expected, double actual, double delta, String message) {
        if (!doublesAreEqual(expected, actual, delta))
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(float expected, float actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(float expected, float actual, String message) {
        if (!floatsAreEqual(expected, actual))
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(float expected, float actual, float delta) {
        assertEquals(expected, actual, delta, null);
    }

    static void assertEquals(float expected, float actual, float delta, String message) {
        if (!floatsAreEqual(expected, actual, delta))
            failNotEqual(expected, actual, message);
    }


    static void assertEquals(short expected, short actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(short expected, short actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }


    static void assertEquals(int expected, int actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(int expected, int actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }


    static void assertEquals(long expected, long actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(long expected, long actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(String expected, String actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(String expected, String actual, String message) {
        if (!objectsAreEqual(expected, actual))
            failNotEqual(expected, actual, message);
    }

    static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, null);
    }

    static void assertEquals(Object expected, Object actual, String message) {
        if (!objectsAreEqual(expected, actual))
            failNotEqual(expected, actual, message);
    }
}
