package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.dto.response.order.*;
import com.hust.soict.ict.aims.models.entities.order.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    OrderDraftResponse toOrderDraftResponse(Order order);

    @Mapping(target = "productId", expression = "java(orderItem.getProduct().getId())")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    DeliveryResponse toDeliveryResponse(DeliveryInformation deliveryInformation);

    InvoiceResponse toInvoiceResponse(Invoice invoice);

    PaymentTransactionResponse toPaymentTransactionResponse(PaymentTransaction paymentTransaction);

}
