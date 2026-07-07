package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderDraftResponse {
    private UUID checkoutId;
    private List<OrderItemResponse> items;
    private DeliveryResponse deliveryInformation;
    private InvoiceResponse invoice;
}
