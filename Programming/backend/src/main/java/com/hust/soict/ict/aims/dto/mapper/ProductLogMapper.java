package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import org.springframework.stereotype.Component;

@Component
class ProductLogMapper extends ProductAuditLogMapper<ProductLog, ProductLogResponse> {
    private final ProductEditDetailMapper productEditDetailMapper;

    ProductLogMapper(ProductEditDetailMapper productEditDetailMapper) {
        super(ProductLogResponse::new);
        this.productEditDetailMapper = productEditDetailMapper;
    }

    @Override
    public void map(ProductLog entity, ProductLogResponse target) {
        target.setAction(entity.getAction().name());
        target.setDetails(entity.getDetails().stream().map(productEditDetailMapper::map).toList());
    }

    @Override
    public Class<ProductLog> getSourceClass() {
        return ProductLog.class;
    }
}
