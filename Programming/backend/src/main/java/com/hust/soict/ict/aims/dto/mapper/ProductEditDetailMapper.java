package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductEditDetailResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductEditDetail;
import org.springframework.stereotype.Component;

@Component
class ProductEditDetailMapper extends AbstractMapper<ProductEditDetail, ProductEditDetailResponse> {
    ProductEditDetailMapper() {
        super(ProductEditDetail.class, ProductEditDetailResponse.class, ProductEditDetailResponse::new);
    }

    @Override
    protected void map(ProductEditDetail source, ProductEditDetailResponse target) {
        target.setFieldName(source.getFieldName());
        target.setOldValue(source.getOldValue());
        target.setNewValue(source.getNewValue());
    }
}
