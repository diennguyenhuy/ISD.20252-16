package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.AuditLog;

import java.util.function.Supplier;

public abstract class AuditLogMapper<L extends AuditLog, R extends AuditLogResponse> {
    private final Supplier<R> responseSupplier;
    final Class<L> logClass;

    public AuditLogMapper(Class<L> logClass, Supplier<R> responseSupplier) {
        this.logClass = logClass;
        this.responseSupplier = responseSupplier;
    }

    public final R map(L log) {
        if (log == null) {
            return null;
        }

        R response = responseSupplier.get();
        response.setId(log.getId());
        response.setTimestamp(log.getTimestamp());
        map(log, response);

        return response;
    }

    protected abstract void map(L log, R response);
}
