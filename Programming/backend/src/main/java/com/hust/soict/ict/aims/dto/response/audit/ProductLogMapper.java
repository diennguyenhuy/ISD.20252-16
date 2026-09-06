package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.ProductAction;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class ProductLogMapper extends AbstractProductLogMapper<ProductLog, ProductLogResponse> {
    ProductLogMapper() {
        super(ProductLog.class, ProductLogResponse::new);
    }

    @Override
    protected void mapProductLog(ProductLog log, ProductLogResponse response) {
        if (log.getAction() == ProductAction.UPDATE) {
            List<AbstractProductEditDetailResponse> details = new ArrayList<>();
            log.getDetails().forEach((k, v) ->
                    details.add(new ProductEditDetailResponse(k, v.getOldValue(), v.getNewValue()))
            );
            log.getArrayDetails().forEach((k, v) ->
                    details.add(new ProductEditArrayDetailResponse(k, v.getRemovedValues(), v.getAddedValues()))
            );
            response.setDetails(details);
        }
    }
}
