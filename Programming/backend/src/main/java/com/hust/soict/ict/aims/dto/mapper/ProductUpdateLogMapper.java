package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductUpdateLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductUpdateLog;
import org.springframework.stereotype.Component;

@Component
class ProductUpdateLogMapper extends AbstractProductLogMapper<ProductUpdateLog, ProductUpdateLogResponse> {
    private final ProductEditDetailMapper productEditDetailMapper;

    ProductUpdateLogMapper(ProductEditDetailMapper productEditDetailMapper) {
        super(ProductUpdateLog.class, ProductUpdateLogResponse::new);
        this.productEditDetailMapper = productEditDetailMapper;
    }

    @Override
    protected void mapProductLog(ProductUpdateLog source, ProductUpdateLogResponse target) {
        target.setDetails(source.getDetails().stream().map(productEditDetailMapper::map).toList());
    }
}
