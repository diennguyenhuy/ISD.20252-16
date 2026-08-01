package com.hust.soict.ict.aims.dto.request;

import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class UpdateFieldRequest<T> {
    public static final UpdateFieldRequest<?> UNDEFINED = new UpdateFieldRequest<>();

    private final T newValue;
    @Getter
    private final boolean defined;

    public boolean isUndefined() {
        return !defined;
    }

    public <X extends Throwable> T getOrThrow(Supplier<X> exceptionSupplier) throws X {
        if (isDefined()) {
            return newValue;
        } else throw exceptionSupplier.get();
    }

    public T get() {
        if (isDefined()) {
            return newValue;
        }
        throw new NoSuchElementException("No new value defined here");
    }

    public void ifDefined(Consumer<? super T> action) {
        if (isDefined()) {
            action.accept(newValue);
        }
    }

    public void ifDefinedOrElse(Consumer<? super T> action, Runnable undefinedAction) {
        if (isDefined()) {
            action.accept(newValue);
        } else {
            undefinedAction.run();
        }
    }

    public <U> UpdateFieldRequest<U> map(Function<? super T, ? extends U> mapper) {
        if (isDefined()) {
            return defined(mapper.apply(newValue));
        } else return undefined();
    }

    private UpdateFieldRequest(T newValue) {
        this.newValue = newValue;
        this.defined = true;
    }

    private UpdateFieldRequest() {
        this.newValue = null;
        this.defined = false;
    }

    public static <T> UpdateFieldRequest<T> defined(T newValue) {
        return new UpdateFieldRequest<>(newValue);
    }

    public static <T> UpdateFieldRequest<T> undefined() {
        return new UpdateFieldRequest<>();
    }

    @Override
    public String toString() {
        final String className = getClass().getSimpleName();
        return isDefined() ? className + ".Defined[" + newValue + "]" : className + ".Undefined";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof UpdateFieldRequest<?> that) {
            if (isDefined() == that.isDefined()) return Objects.equals(newValue, that.newValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(newValue, defined);
    }
}
