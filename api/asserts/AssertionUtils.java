package api.asserts;

import api.errors.AssertionFailedError;

import java.util.Arrays;
import java.util.function.Supplier;

public class AssertionUtils {
    static void failNotEqual(Object expected, Object actual, String message) {
        fail(buildMessage(expected, actual, message));
    }

    static void failNotEqual(Object expected, Object actual, Supplier<String> messageSupplier) {
        fail(buildMessage(expected, actual, messageSupplier != null ? messageSupplier.get() : null));
    }

    static void fail(String message) {
        throw new AssertionFailedError(message);
    }

    private static String toString(Object obj) {
        return obj instanceof Class ? getCanonicalName((Class<?>) obj) : nullSafeToString(obj);
    }

    static String getCanonicalName(Class<?> clazz) {
        String canonicalName = clazz.getCanonicalName();
        return canonicalName != null ? canonicalName : clazz.getName();
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

    static void failNotNull(Object actual, String message) {
        String stringRepresentation = actual.toString();
        if (stringRepresentation != null && !stringRepresentation.equals("null"))
            fail(buildMessage(null, actual, message));
        else
            fail(buildMessage(null, actual, message));
    }

    static void failNull(String message) {
        Assertions.fail(buildPrefix(message) + "expected: not <null>");
    }

    public static String buildPrefix(String message) {
        return message != null && !message.isBlank() ? message + " ==> " : "";
    }

    static String buildMessage(Object expected, Object actual, String message) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(buildPrefix(message));

        String expectedString = toString(expected);
        String actualString = toString(actual);
        String formattedValues = String.format("expected: <%s> but was: <%s>", expectedString, actualString);

        messageBuilder.append(formattedValues);
        return messageBuilder.toString();
    }

    static String nullSafeGet(Object messageOrSupplier) {
        if (messageOrSupplier instanceof String)
            return (String) messageOrSupplier;

        if (messageOrSupplier instanceof Supplier) {
            Object message = ((Supplier<?>) messageOrSupplier).get();
            if (message != null)
                return message.toString();
        }

        return null;
    }

    private static String nullSafeToString(Object obj) {
        if (obj == null)
            return "null";

        if (obj.getClass().isArray()) {
            if (!obj.getClass().getComponentType().isPrimitive())
                return Arrays.deepToString((Object[]) obj);

            if (obj instanceof boolean[])
                return Arrays.toString((boolean[]) obj);

            if (obj instanceof char[])
                return Arrays.toString((char[]) obj);

            if (obj instanceof short[])
                return Arrays.toString((short[]) obj);

            if (obj instanceof byte[])
                return Arrays.toString((byte[]) obj);

            if (obj instanceof int[])
                return Arrays.toString((int[]) obj);

            if (obj instanceof long[])
                return Arrays.toString((long[]) obj);

            if (obj instanceof float[])
                return Arrays.toString((float[]) obj);

            if (obj instanceof double[])
                return Arrays.toString((double[]) obj);
        }

        String result = obj.toString();
        return result != null ? result : "null";
    }
}