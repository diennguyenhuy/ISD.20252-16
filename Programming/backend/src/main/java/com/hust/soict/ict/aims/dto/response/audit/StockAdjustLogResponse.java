package com.hust.soict.ict.aims.dto.response.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockAdjustLogResponse extends ProductLogResponse {
    private int oldStock;
    private int newStock;
    private String reason;
}
