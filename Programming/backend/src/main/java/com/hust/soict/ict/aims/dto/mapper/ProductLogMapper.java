package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import org.springframework.stereotype.Component;

@Component
class ProductLogMapper extends AbstractProductLogMapper<ProductLog, ProductLogResponse> {

    ProductLogMapper() {
        super(ProductLog.class, ProductLogResponse::new);
    }

    @Override
    protected void mapProductLog(ProductLog source, ProductLogResponse target) {
        //do nothing, already mapped in super class
    }
}
