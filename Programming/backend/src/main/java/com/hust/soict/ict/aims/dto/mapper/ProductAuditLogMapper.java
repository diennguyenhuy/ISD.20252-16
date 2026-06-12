package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.ProductAuditLogResponse;
import com.hust.soict.ict.aims.dto.response.audit.ProductEditDetailResponse;
import com.hust.soict.ict.aims.dto.response.audit.ProductLogResponse;
import com.hust.soict.ict.aims.dto.response.audit.StockAdjustLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.ProductAuditLog;
import com.hust.soict.ict.aims.models.entities.audit.ProductEditDetail;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface ProductAuditLogMapper {

    @Mapping(target = "productId", expression = "java(productAuditLog.getProduct().getId())")
    @Mapping(target = "managerId", expression = "java(productAuditLog.getManager().getId())")
    @SubclassMapping(source = ProductLog.class, target = ProductLogResponse.class)
    @SubclassMapping(source = StockAdjustLog.class, target = StockAdjustLogResponse.class)
    ProductAuditLogResponse toProductAuditLogResponse(ProductAuditLog productAuditLog);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "managerId", ignore = true)
    ProductLogResponse toProductLogResponse(ProductLog productAuditLog);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "managerId", ignore = true)
    StockAdjustLogResponse toStockAdjustLogResponse(StockAdjustLog stockAdjustLog);

    ProductEditDetailResponse toProductEditDetailResponse(ProductEditDetail productEditDetail);
}
