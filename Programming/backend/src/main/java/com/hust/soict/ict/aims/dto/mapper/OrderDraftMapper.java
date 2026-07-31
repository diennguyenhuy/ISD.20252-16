package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.OrderDraftResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.stereotype.Component;

@Component
class OrderDraftMapper extends AbstractMapper<Order.Draft, OrderDraftResponse> {
    private final OrderItemMapper orderItemMapper;
    private final DeliveryInformationMapper deliveryInformationMapper;
    private final InvoiceMapper invoiceMapper;

    OrderDraftMapper(
            OrderItemMapper orderItemMapper,
            DeliveryInformationMapper deliveryInformationMapper,
            InvoiceMapper invoiceMapper
    ) {
        super(Order.Draft.class, OrderDraftResponse.class, OrderDraftResponse::new);
        this.orderItemMapper = orderItemMapper;
        this.deliveryInformationMapper = deliveryInformationMapper;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    protected void map(Order.Draft source, OrderDraftResponse target) {
        target.setCheckoutId(source.getCheckoutId());
        target.setItems(source.getItems().stream().map(orderItemMapper::map).toList());
        target.setDeliveryInformation(deliveryInformationMapper.map(source.getDeliveryInformation()));
        target.setInvoice(invoiceMapper.map(source.getInvoice()));
    }
}
