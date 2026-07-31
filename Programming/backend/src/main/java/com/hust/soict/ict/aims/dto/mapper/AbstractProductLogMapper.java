package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;

import java.util.function.Supplier;

abstract class AbstractProductLogMapper<L extends ProductLog, R extends ProductLogResponse> extends AuditLogMapper<L, R> {
    @SuppressWarnings("unchecked")
    protected AbstractProductLogMapper(Class<L> targetClass, Supplier<R> responseSupplier) {
        super(targetClass, (Class<R>) ProductLogResponse.class, responseSupplier);
    }

    @Override
    protected final void mapAuditLog(L source, R target) {
        target.setAction(source.getAction().name());
        target.setManagerId(source.getManagerId());
        target.setManagerUsername(source.getManagerUsername());
        target.setProductId(source.getProductId());
        target.setProductTitle(source.getProductTitle());
        mapProductLog(source, target);
    }

    protected abstract void mapProductLog(L source, R target);
}
