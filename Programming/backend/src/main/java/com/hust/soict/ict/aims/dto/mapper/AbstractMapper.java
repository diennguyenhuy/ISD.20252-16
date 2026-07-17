package com.hust.soict.ict.aims.dto.mapper;

import lombok.Getter;

import java.util.function.Supplier;

public abstract class AbstractMapper<S, T> {
    private final Supplier<T> responseSupplier;

    protected AbstractMapper(Supplier<T> responseSupplier) {
        this.responseSupplier = responseSupplier;
    }

    public abstract void map(S source, T target);
    public abstract Class<S> getSourceClass();
    public abstract Class<T> getTargetClass();

    public T map(S source) {
        if (source == null) {
            return null;
        }

        T response = responseSupplier.get();
        map(source, response);
        return response;
    }
}
