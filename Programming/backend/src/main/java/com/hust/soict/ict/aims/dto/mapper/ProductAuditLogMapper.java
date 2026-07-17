package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductAuditLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductAuditLog;

import java.util.function.Supplier;

abstract class ProductAuditLogMapper<L extends ProductAuditLog, R extends ProductAuditLogResponse> extends AuditLogMapper<L, R> {

    protected ProductAuditLogMapper(Supplier<R> responseSupplier) {
        super(responseSupplier);
    }

    @Override
    public R map(L source) {
        R response = super.map(source);

        response.setManagerId(source.getManagerId());
        response.setManagerUsername(source.getManagerUsername());
        response.setProductId(source.getProductId());
        response.setProductTitle(source.getProductTitle());

        return response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<R> getTargetClass() {
        return (Class<R>) ProductAuditLogResponse.class;
    }
}
