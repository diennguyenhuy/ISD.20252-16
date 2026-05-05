package com.hust.soict.ict.aims.models.mapper;

import com.hust.soict.ict.aims.exceptions.DeliveryValidationException;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.models.dto.response.order.*;
import com.hust.soict.ict.aims.models.entities.order.DeliveryInformation;
import com.hust.soict.ict.aims.models.entities.order.Invoice;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    OrderDraftResponse toOrderDraftResponse(Order order);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    DeliveryResponse toDeliveryResponse(DeliveryInformation deliveryInformation);

    InvoiceResponse toInvoiceResponse(Invoice invoice);

    DeliveryInformation toEntity(DeliveryRequest deliveryRequest, Order order);

    @ObjectFactory
    default DeliveryInformation toDeliveryInformation(DeliveryRequest deliveryRequest, Order order) throws DeliveryValidationException {
        return DeliveryInformation.of(
                order,
                deliveryRequest.getCustomerName(),
                deliveryRequest.getCustomerEmail(),
                deliveryRequest.getPhoneNumber(),
                deliveryRequest.getProvince(),
                deliveryRequest.getCommune(),
                deliveryRequest.getAddress(),
                deliveryRequest.getDeliveryMethod()
        );
    }
}
