package com.hust.soict.ict.aims.dto.response.order;

import com.hust.soict.ict.aims.models.entities.order.*;

public final class OrderMappers {
    private OrderMappers() {}

    public static OrderResponse map(final Order order) {
        if (order == null) {
            return null;
        }

        return new OrderResponse(
                order.getId(),
                order.getItems().stream().map(OrderMappers::map).toList(),
                order.getStatus().name(),
                order.getTotalWeight(),
                order.getTotalItemCount(),
                map(order.getDeliveryInformation()),
                map(order.getInvoice()),
                map(order.getPaymentTransaction()),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public static OrderDraftResponse map(final Order.Draft draft) {
        if (draft == null) {
            return null;
        }

        return new OrderDraftResponse(
                draft.getCheckoutId(),
                draft.getItems().stream().map(OrderMappers::map).toList(),
                map(draft.getDeliveryInformation()),
                map(draft.getInvoice())
        );
    }

    public static OrderItemResponse map(final OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        if (orderItem.getProduct() != null) {
            return new OrderItemResponse(
                    orderItem.getProduct().getId(),
                    orderItem.getId().getProductReferenceId(),
                    orderItem.getProductName(),
                    orderItem.getProduct().getImageURL(),
                    orderItem.getQuantity(),
                    orderItem.getUnitPrice(),
                    orderItem.getItemTotalPrice()
            );
        } else return new OrderItemResponse(
                orderItem.getId().getProductReferenceId(),
                orderItem.getProductName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getItemTotalPrice()
        );
    }

    public static DeliveryResponse map(final DeliveryInformation deliveryInformation) {
        if (deliveryInformation == null) {
            return null;
        }

        return new DeliveryResponse(
                deliveryInformation.getCustomerName(),
                deliveryInformation.getCustomerEmail(),
                deliveryInformation.getPhoneNumber(),
                deliveryInformation.getProvince(),
                deliveryInformation.getCommune(),
                deliveryInformation.getAddress(),
                deliveryInformation.getDeliveryMethod()
        );
    }

    public static InvoiceResponse map(final Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return new InvoiceResponse(
                invoice.getIssuedAt(),
                invoice.getTotalPriceWithoutVAT(),
                invoice.getTotalPriceWithVAT(),
                invoice.getDeliveryFee(),
                invoice.getTotalAmount()
        );
    }

    public static PaymentTransactionResponse map(final PaymentTransaction paymentTransaction) {
        if (paymentTransaction == null) {
            return null;
        }

        return new PaymentTransactionResponse(
                paymentTransaction.getId(),
                paymentTransaction.getTransactionContent(),
                paymentTransaction.getTransactionTimestamp(),
                paymentTransaction.getTransactionMethod(),
                paymentTransaction.getAmountPaid()
        );
    }
}
