package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.AuditLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.AuditLog;

import java.util.function.Supplier;

abstract class AuditLogMapper<L extends AuditLog, R extends AuditLogResponse> extends AbstractMapper<L, R> {

    protected AuditLogMapper(
            Class<L> sourceClass,
            Class<R> targetClass,
            Supplier<R> targetSupplier
    ) {
        super(sourceClass, targetClass, targetSupplier);
    }

    @Override
    protected final void map(L source, R target) {
        target.setId(source.getId());
        target.setTimestamp(source.getTimestamp());
        mapAuditLog(source, target);
    }

    protected abstract void mapAuditLog(L source, R target);
}
