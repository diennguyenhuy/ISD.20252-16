package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.audit.StockAdjustLogResponse;
import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import org.springframework.stereotype.Component;

@Component
class StockAdjustLogMapper extends AbstractProductLogMapper<StockAdjustLog, StockAdjustLogResponse> {

    StockAdjustLogMapper() {
        super(StockAdjustLog.class, StockAdjustLogResponse::new);
    }

    @Override
    protected void mapProductLog(StockAdjustLog source, StockAdjustLogResponse target) {
        target.setOldStock(source.getOldStock());
        target.setNewStock(source.getNewStock());
        target.setReason(source.getReason());
    }
}
