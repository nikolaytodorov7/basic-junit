package api.annotations.function;

public interface ThrowingSupplier<T> {
    T get() throws Throwable;
}