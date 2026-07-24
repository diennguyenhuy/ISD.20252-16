package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductUpdateLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductUpdateLog;
import org.springframework.stereotype.Component;

@Component
class ProductUpdateLogMapper extends AbstractProductLogMapper<ProductUpdateLog, ProductUpdateLogResponse> {
    private final ProductEditDetailMapper productEditDetailMapper;

    ProductUpdateLogMapper(ProductEditDetailMapper productEditDetailMapper) {
        super(ProductUpdateLogResponse::new);
        this.productEditDetailMapper = productEditDetailMapper;
    }

    @Override
    public void map(ProductUpdateLog entity, ProductUpdateLogResponse target) {
        target.setDetails(entity.getDetails().stream().map(productEditDetailMapper::map).toList());
    }

    @Override
    public Class<ProductUpdateLog> getSourceClass() {
        return ProductUpdateLog.class;
    }
}
