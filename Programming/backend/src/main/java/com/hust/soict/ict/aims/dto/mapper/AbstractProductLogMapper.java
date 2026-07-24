package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;

import java.util.function.Supplier;

abstract class AbstractProductLogMapper<L extends ProductLog, R extends ProductLogResponse> extends AuditLogMapper<L, R> {
    protected AbstractProductLogMapper(Supplier<R> responseSupplier) {
        super(responseSupplier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Class<R> getTargetClass() {
        return (Class<R>) ProductLogResponse.class;
    }

    @Override
    public R map(L source) {
        R response = super.map(source);
        if (response == null) {
            return null;
        }

        response.setAction(source.getAction().name());
        response.setManagerId(source.getManagerId());
        response.setManagerUsername(source.getManagerUsername());
        response.setProductId(source.getProductId());
        response.setProductTitle(source.getProductTitle());

        return response;
    }
}
