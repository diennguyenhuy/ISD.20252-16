package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.StockAdjustLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import org.springframework.stereotype.Component;

@Component
class StockAdjustLogMapper extends AbstractProductLogMapper<StockAdjustLog, StockAdjustLogResponse> {

    StockAdjustLogMapper() {
        super(StockAdjustLogResponse::new);
    }

    @Override
    public void map(StockAdjustLog entity, StockAdjustLogResponse target) {
        target.setOldStock(entity.getOldStock());
        target.setNewStock(entity.getNewStock());
        target.setReason(entity.getReason());
    }

    @Override
    public Class<StockAdjustLog> getSourceClass() {
        return StockAdjustLog.class;
    }

}
