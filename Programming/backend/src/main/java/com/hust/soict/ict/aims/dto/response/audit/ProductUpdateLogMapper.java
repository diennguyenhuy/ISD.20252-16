package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductUpdateLog;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class ProductUpdateLogMapper extends AbstractProductLogMapper<ProductUpdateLog, ProductUpdateLogResponse> {
    ProductUpdateLogMapper() {
        super(ProductUpdateLog.class, ProductUpdateLogResponse::new);
    }

    @Override
    protected void mapProductLog(ProductUpdateLog log, ProductUpdateLogResponse response) {
        Map<String, AbstractProductEditDetailResponse> details = new HashMap<>();
        log.getDetails().forEach((k, v) -> details.put(k, new ProductEditDetailResponse(v.getOldValue(), v.getNewValue())));
        log.getArrayDetails().forEach((k, v) -> details.put(k, new ProductEditArrayDetailResponse(v. getRemovedValues(), v.getAddedValues())));
        response.setDetails(details);
    }
}
