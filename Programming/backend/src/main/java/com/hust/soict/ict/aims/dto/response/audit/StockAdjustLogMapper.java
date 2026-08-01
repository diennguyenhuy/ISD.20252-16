package com.hust.soict.ict.aims.dto.response.audit;

import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import org.springframework.stereotype.Component;

@Component
class StockAdjustLogMapper extends AbstractProductLogMapper<StockAdjustLog, StockAdjustLogResponse> {
    StockAdjustLogMapper() {
        super(StockAdjustLog.class, StockAdjustLogResponse::new);
    }

    @Override
    protected void mapProductLog(StockAdjustLog log, StockAdjustLogResponse response) {
        response.setOldStock(log.getOldStock());
        response.setNewStock(log.getNewStock());
        response.setReason(log.getReason());
    }
}
