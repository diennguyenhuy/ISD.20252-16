package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class OrderDraftResponse {
    private List<OrderItemResponse> items;
    private DeliveryResponse deliveryInformation;
    private InvoiceResponse invoice;
}
