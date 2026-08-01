package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductUpdateLog;
import org.springframework.stereotype.Component;

@Component
class ProductUpdateLogMapper extends AbstractProductLogMapper<ProductUpdateLog, ProductUpdateLogResponse> {
    ProductUpdateLogMapper() {
        super(ProductUpdateLog.class, ProductUpdateLogResponse::new);
    }

    @Override
    protected void mapProductLog(ProductUpdateLog log, ProductUpdateLogResponse response) {
        response.setDetails(log.getDetails().stream().map(d -> new ProductEditDetailResponse(d.getFieldName(), d.getOldValue(), d.getNewValue())).toList());
    }
}
