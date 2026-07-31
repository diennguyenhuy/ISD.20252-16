package com.hust.soict.ict.aims.dto.mapper;

import java.util.function.Supplier;

public abstract class AbstractMapper<S, T> {
    private final Supplier<T> targetSupplier;
    private final Class<S> sourceClass;
    private final Class<T> targetClass;

    protected AbstractMapper(
            Class<S> sourceClass,
            Class<T> targetClass,
            Supplier<T> targetSupplier
    ) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.targetSupplier = targetSupplier;
    }

    protected abstract void map(S source, T target);
    public final Class<S> getSourceClass() {
        return sourceClass;
    }
    public final Class<T> getTargetClass() {
        return targetClass;
    }

    public final T map(S source) {
        if (source == null) {
            return null;
        }

        T target = targetSupplier.get();
        map(source, target);
        return target;
    }
}
