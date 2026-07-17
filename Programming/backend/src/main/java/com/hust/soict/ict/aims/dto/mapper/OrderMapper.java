package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.stereotype.Component;

@Component
class OrderMapper extends AbstractMapper<Order, OrderResponse> {
    private final OrderItemMapper orderItemMapper;
    private final DeliveryInformationMapper deliveryInformationMapper;
    private final InvoiceMapper invoiceMapper;
    private final PaymentTransactionMapper paymentTransactionMapper;

    OrderMapper(
            OrderItemMapper orderItemMapper,
            DeliveryInformationMapper deliveryInformationMapper,
            InvoiceMapper invoiceMapper,
            PaymentTransactionMapper paymentTransactionMapper
    ) {
        super(OrderResponse::new);
        this.orderItemMapper = orderItemMapper;
        this.deliveryInformationMapper = deliveryInformationMapper;
        this.invoiceMapper = invoiceMapper;
        this.paymentTransactionMapper = paymentTransactionMapper;
    }

    @Override
    public void map(Order source, OrderResponse target) {
        target.setId(source.getId());
        target.setItems(source.getItems().stream().map(orderItemMapper::map).toList());
        target.setStatus(source.getStatus().name());
        target.setTotalWeight(source.getTotalWeight());
        target.setTotalItemCount(source.getTotalItemCount());
        target.setDeliveryInformation(deliveryInformationMapper.map(source.getDeliveryInformation()));
        target.setInvoice(invoiceMapper.map(source.getInvoice()));
        target.setPaymentTransaction(paymentTransactionMapper.map(source.getPaymentTransaction()));
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    @Override
    public Class<Order> getSourceClass() {
        return Order.class;
    }

    @Override
    public Class<OrderResponse> getTargetClass() {
        return OrderResponse.class;
    }
}
