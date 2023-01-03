package api.asserts;

import java.util.Objects;

public class AssertionUtils {
    static void failNotEqual(Object expected, Object actual, String message) {
        fail(message, expected, actual);
    }

    static void fail(String message) {
        throw new AssertionFailedError(message);
    }

    static void fail(String message, Object expected, Object actual) {
        throw new AssertionFailedError(message, expected, actual);
    }

    static String format(Object expected, Object actual, String message) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(buildPrefix(message));

        String expectedString = toString(expected);
        String actualString = toString(actual);
        String formattedValues = String.format("expected: <%s> but was: <%s>", expectedString, actualString);

        messageBuilder.append(formattedValues);
        return messageBuilder.toString();
    }

    private static String toString(Object obj) {
        return obj instanceof Class ? getCanonicalName((Class) obj) : StringUtils.nullSafeToString(obj);
    }

    private static String getCanonicalName(Class<?> clazz) {
        String canonicalName = clazz.getCanonicalName();
        return canonicalName != null ? canonicalName : clazz.getName();
    }

    public static boolean objectsAreEqual(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    static boolean floatsAreEqual(float value1, float value2) {
        return Float.floatToIntBits(value1) == Float.floatToIntBits(value2);
    }

    static boolean floatsAreEqual(float value1, float value2, float delta) {
        assertValidDelta(delta);
        return floatsAreEqual(value1, value2) || Math.abs(value1 - value2) <= delta;
    }

    public static boolean doublesAreEqual(double value1, double value2) {
        return Double.doubleToLongBits(value1) == Double.doubleToLongBits(value2);
    }

    public static boolean doublesAreEqual(double value1, double value2, double delta) {
        assertValidDelta(delta);
        return doublesAreEqual(value1, value2) || Math.abs(value1 - value2) <= delta;
    }

    static void assertValidDelta(double delta) {
        if (Double.isNaN(delta) || delta < 0.0)
            failIllegalDelta(String.valueOf(delta));
    }

    private static void failIllegalDelta(String delta) {
        fail(String.format("positive delta expected but was: <%s>", delta));
    }

    public static String buildPrefix(String message) {
        return message != null && !message.isBlank() ? message + " ==> " : "";
    }
}
