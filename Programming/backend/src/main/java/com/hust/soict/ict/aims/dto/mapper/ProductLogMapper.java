package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import org.springframework.stereotype.Component;

@Component
class ProductLogMapper extends AbstractProductLogMapper<ProductLog, ProductLogResponse> {

    ProductLogMapper() {
        super(ProductLogResponse::new);
    }

    @Override
    public void map(ProductLog source, ProductLogResponse target) {
        //do nothing, already mapped in super class
    }

    @Override
    public Class<ProductLog> getSourceClass() {
        return ProductLog.class;
    }
}
