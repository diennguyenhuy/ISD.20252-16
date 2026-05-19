package com.hust.soict.ict.aims.models.dto.response.order;
import com.hust.soict.ict.aims.models.dto.response.order.OrderItemResponse;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
public class OrderDraftResponse {
    private List<OrderItemResponse> items;
    private DeliveryResponse deliveryInformation;
    private InvoiceResponse invoice;
}
