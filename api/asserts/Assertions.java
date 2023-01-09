package api.asserts;

import api.annotations.function.Executable;
import api.annotations.function.ThrowingSupplier;
import api.errors.AssertionFailedError;
import api.errors.MultipleFailuresError;
import api.exceptions.PreconditionViolationException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static api.asserts.AssertionUtils.*;

public class Assertions {
    public static void assertEquals(byte expected, byte actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(byte expected, byte actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(byte expected, byte actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(short expected, short actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(short expected, short actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(short expected, short actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(int expected, int actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(int expected, int actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(int expected, int actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(long expected, long actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(long expected, long actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(long expected, long actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(float expected, float actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(float expected, float actual, String message) {
        if (!floatsAreEqual(expected, actual))
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(float expected, float actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(float expected, float actual, float delta) {
        assertEquals(expected, actual, delta, (String) null);
    }

    public static void assertEquals(float expected, float actual, float delta, String message) {
        if (!floatsAreEqual(expected, actual, delta))
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(float expected, float actual, float delta, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, delta, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(double expected, double actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(double expected, double actual, String message) {
        if (!doublesAreEqual(expected, actual))
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(double expected, double actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(double expected, double actual, double delta) {
        assertEquals(expected, actual, delta, (String) null);
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        assertValidDelta(delta);
        if (!doublesAreEqual(expected, actual, delta))
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(double expected, double actual, double delta, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, delta, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(char expected, char actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(char expected, char actual, String message) {
        if (expected != actual)
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(char expected, char actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertEquals(String expected, String actual) {
        assertEquals(expected, actual, (String) null);
    }

    public static void assertEquals(String expected, String actual, String message) {
        if (!Objects.equals(expected, actual))
            failNotEqual(expected, actual, message);
    }

    public static void assertEquals(String expected, String actual, Supplier<String> messageSupplier) {
        assertEquals(expected, actual, getSupplierMessage(messageSupplier));
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, (String) null);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            String msg = String.format(buildMessage(true, false, message));
            throw new AssertionFailedError(msg);
        }
    }

    public static void assertTrue(boolean condition, Supplier<String> messageSupplier) {
        assertTrue(condition, getSupplierMessage(messageSupplier));
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, (String) null);
    }

    public static void assertFalse(boolean condition, String message) {
        if (!condition)
            return;

        String msg = buildMessage(false, true, message);
        throw new AssertionFailedError(msg);
    }

    public static void assertFalse(boolean condition, Supplier<String> messageSupplier) {
        assertFalse(condition, getSupplierMessage(messageSupplier));
    }

    public static void assertNull(Object actual) {
        assertNull(actual, (String) null);
    }

    public static void assertNull(Object actual, String message) {
        if (actual == null)
            return;

        String stringRepresentation = actual.toString();
        if (stringRepresentation != null && !stringRepresentation.equals("null")) {
            String msg = buildMessage(null, actual, message);
            throw new AssertionFailedError(msg);
        }
    }

    public static void assertNull(Object actual, Supplier<String> messageSupplier) {
        assertNull(actual, getSupplierMessage(messageSupplier));
    }

    public static void assertNotNull(Object actual) {
        assertNotNull(actual, (String) null);
    }

    public static void assertNotNull(Object actual, String message) {
        if (actual != null)
            return;

        String msg = buildPrefix(message) + "expected: not <null>";
        throw new AssertionFailedError(msg);
    }

    public static void assertNotNull(Object actual, Supplier<String> messageSupplier) {
        assertNotNull(actual, getSupplierMessage(messageSupplier));
    }

    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
        return assertThrows(expectedType, executable, (Object) null);
    }

    public static <T extends Throwable> T
    assertThrows(Class<T> expectedType, Executable executable, String message) {
        return assertThrows(expectedType, executable, (Object) message);
    }

    public static <T extends Throwable> T
    assertThrows(Class<T> expectedType, Executable executable, Supplier<String> messageSupplier) {
        return assertThrows(expectedType, executable, (Object) messageSupplier);
    }

    private static <T extends Throwable> T
    assertThrows(Class<T> expectedType, Executable executable, Object messageOrSupplier) {
        try {
            executable.execute();
        } catch (Throwable throwable) {
            if (expectedType.isInstance(throwable))
                return (T) throwable;

            String message = buildMessage(expectedType, throwable.getClass(), "Unexpected exception type thrown");
            throw new AssertionFailedError(message, throwable);
        }

        String message = String.format("%sExpected %s to be thrown, but nothing was thrown.",
                buildPrefix(AssertionUtils.nullSafeGet(messageOrSupplier)),
                AssertionUtils.getCanonicalName(expectedType));
        throw new AssertionFailedError(message);
    }

    public static void assertTimeout(Duration timeout, Executable executable) {
        assertTimeout(timeout, executable, (String) null);
    }

    public static void assertTimeout(Duration timeout, Executable executable, String message) {
        assertTimeout(timeout, () -> {
            executable.execute();
            return null;
        }, message);
    }

    public static void assertTimeout(Duration timeout, Executable executable, Supplier<String> messageSupplier) {
        assertTimeout(timeout, () -> {
            executable.execute();
            return null;
        }, messageSupplier);
    }

    public static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier) {
        return assertTimeout(timeout, supplier, (Object) null);
    }

    public static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier, String message) {
        return assertTimeout(timeout, supplier, (Object) message);
    }

    public static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier, Supplier<String> messageSupplier) {
        return assertTimeout(timeout, supplier, (Object) messageSupplier);
    }

    private static <T> T assertTimeout(Duration timeout, ThrowingSupplier<T> supplier, Object messageOrSupplier) {
        long timeoutInMillis = timeout.toMillis();
        long start = System.currentTimeMillis();
        T result = null;
        try {
            result = supplier.get();
        } catch (Throwable throwable) {
//            throw throwable; //todo throw throwable without unhandled
        }

        long timeElapsed = System.currentTimeMillis() - start;
        if (timeElapsed > timeoutInMillis) {
            String prefix = buildPrefix(nullSafeGet(messageOrSupplier));
            String message = String.format("execution exceeded timeout of %s ms by %s ms", timeoutInMillis, timeElapsed - timeoutInMillis);
            throw new AssertionFailedError(prefix + message);
        }

        return result;
    }

    public static void assertTimeoutPreemptively(Duration timeout, Executable executable) {
        assertTimeoutPreemptively(timeout, executable, (String) null);
    }

    public static void assertTimeoutPreemptively(Duration timeout, Executable executable, String message) {
        assertTimeoutPreemptively(timeout, () -> {
            executable.execute();
            return null;
        }, message);
    }

    public static void assertTimeoutPreemptively(Duration timeout, Executable
            executable, Supplier<String> messageSupplier) {
        assertTimeoutPreemptively(timeout, () -> {
            executable.execute();
            return null;
        }, messageSupplier);
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier) {
        return assertTimeoutPreemptively(timeout, supplier, (Object) null);
    }

    public static <T> T
    assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier, String message) {
        return assertTimeoutPreemptively(timeout, supplier, (Object) message);
    }

    public static <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier, Supplier<String> messageSupplier) {
        return assertTimeoutPreemptively(timeout, supplier, (Object) messageSupplier);
    }

    private static <T> T assertTimeoutPreemptively(Duration timeout, ThrowingSupplier<T> supplier, Object messageOrSupplier) {
        T[] arr = (T[]) new Object[1];
        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<Boolean> future = pool.submit(() -> {
                try {
                    arr[0] = supplier.get();
                } catch (Throwable throwable) {
//            throw throwable; //todo throw throwable without unhandled
                }
            }, true);

            future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            pool.shutdownNow();
        } catch (Exception e) {
            String prefix = buildPrefix(nullSafeGet(messageOrSupplier));
            String message = String.format("execution timeout of %s ms has passed.", timeout.toMillis());
            throw new AssertionFailedError(prefix + message);
        }

        return arr[0];
    }

    public static void assertAll(Executable... executables) throws MultipleFailuresError {
        assertAll(null, executables);
    }

    public static void assertAll(String heading, Executable... executables) throws MultipleFailuresError {
        if (executables == null || executables.length == 0)
            throw new PreconditionViolationException("executables array must not be null or empty");

        Arrays.stream(executables).forEach((object) -> {
            if (object == null)
                throw new PreconditionViolationException("individual executables must not be null");
        });

        assertAll(heading, Arrays.stream(executables));
    }

    public static void assertAll(Collection<Executable> executables) throws MultipleFailuresError {
        assertAll(null, executables);
    }

    public static void assertAll(String heading, Collection<Executable> executables) throws
            MultipleFailuresError {
        if (executables == null)
            throw new PreconditionViolationException("executables collection must not be null");

        executables.forEach((object) -> {
            if (object == null)
                throw new PreconditionViolationException("individual executables must not be null");
        });

        assertAll(heading, executables.stream());
    }

    public static void assertAll(Stream<Executable> executables) throws MultipleFailuresError {
        assertAll(null, executables);
    }

    public static void assertAll(String heading, Stream<Executable> executables) throws MultipleFailuresError {
        if (executables == null)
            throw new PreconditionViolationException("executables stream must not be null");

        List<Throwable> failures = getFailures(executables);

        if (!failures.isEmpty()) {
            MultipleFailuresError multipleFailuresError = new MultipleFailuresError(heading, failures);
            failures.forEach(multipleFailuresError::addSuppressed);
            throw multipleFailuresError;
        }
    }

    private static List<Throwable> getFailures(Stream<Executable> executables) {
        return executables.map((executable) -> {
                    if (executable == null)
                        throw new PreconditionViolationException("individual executables must not be null");

                    try {
                        executable.execute();
                        return null;
                    } catch (Throwable throwable) {
                        return throwable;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
