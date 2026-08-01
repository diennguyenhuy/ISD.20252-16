package com.hust.soict.ict.aims.dto.response.order;

import java.util.List;
import java.util.UUID;

public record OrderDraftResponse(
        UUID checkoutId,
        List<OrderItemResponse> items,
        DeliveryResponse deliveryInformation,
        InvoiceResponse invoice
) {}
