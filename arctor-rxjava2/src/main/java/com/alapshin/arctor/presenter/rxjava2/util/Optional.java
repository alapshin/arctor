package com.alapshin.arctor.presenter.rxjava2.util;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import java.util.NoSuchElementException;

public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<Object>();

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? Optional.<T>empty() : of(value);
    }

    private final T value;

    private Optional() {
        this.value = null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    private Optional(T value) {
        this.value = requireNonNull(value);
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<? super T> consumer) throws Exception {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public Optional<T> filter(Predicate<? super T> predicate) throws Exception {
        requireNonNull(predicate);
        if (isPresent()) {
            return predicate.test(value) ? this : Optional.<T>empty();
        }
        return this;
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) throws Exception {
        requireNonNull(mapper);
        if (isPresent()) {
            return Optional.ofNullable(mapper.apply(value));
        }
        return empty();
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) throws Exception {
        requireNonNull(mapper);
        if (isPresent()) {
            return requireNonNull(mapper.apply(value));
        }
        return empty();
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value == null) {
            throw exceptionSupplier.get();
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Optional)) return false;
        Optional<?> other = (Optional<?>) o;
        return equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return hashCode(value);
    }

    @Override
    public String toString() {
        return value == null ? "Optional.empty" : "Optional[" + value + ']';
    }

    static <T> T requireNonNull(T o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o;
    }

    static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
