package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.AuditLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.AuditLog;

import java.util.function.Supplier;

abstract class AuditLogMapper<L extends AuditLog, R extends AuditLogResponse> extends AbstractMapper<L, R> {

    protected AuditLogMapper(Supplier<R> responseSupplier) {
        super(responseSupplier);
    }

    @Override
    public R map(L source) {
        R response = super.map(source);
        if (response == null) {
            return null;
        }

        response.setId(source.getId());
        response.setTimestamp(source.getTimestamp());
        return response;
    }
}
