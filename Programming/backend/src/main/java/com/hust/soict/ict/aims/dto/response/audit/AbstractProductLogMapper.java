package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductLog;

import java.util.function.Supplier;

public abstract class AbstractProductLogMapper<L extends ProductLog, R extends ProductLogResponse> extends AuditLogMapper<L, R> {

    protected AbstractProductLogMapper(Class<L> logClass, Supplier<R> responseSupplier) {
        super(logClass, responseSupplier);
    }

    @Override
    protected final void map(L log, R response) {
        response.setAction(log.getAction().name());
        response.setManagerId(log.getManagerId());
        response.setProductId(log.getProductId());
        response.setProductTitle(log.getProductTitle());
        response.setManagerUsername(log.getManagerUsername());
        mapProductLog(log, response);
    }

    protected abstract void mapProductLog(L log, R response);
}
