package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.exceptions.OrderApprovalException;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.services.ordermanagement.event.OrderApprovalEvent;
import com.hust.soict.ict.aims.services.ordermanagement.event.OrderRejectionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
class OrderManagementServiceImpl implements OrderQueryService, OrderManagementService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getPendingOrders(Pageable pageable) {
        return orderRepository.findAllByStatus(Order.Status.PENDING, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getOrders(Order.Status status, Pageable pageable) {
        if (status == null) {
            return orderRepository.findAll(pageable);
        }
        return orderRepository.findAllByStatus(status, pageable);
    }

    @Override
    @Transactional
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @Transactional
    public Order approveOrder(UUID id) throws OrderApprovalException {
        Order order = orderRepository.findByIdAndStatus(id, Order.Status.PENDING)
                .orElseThrow(() -> new OrderNotFoundException(id, Order.Status.PENDING.name()));

        order.approve(this::validateOrder);
        order.getItems().forEach(i -> i.getProduct().updateStock(i.getProduct().getStockQuantity() - i.getQuantity()));

        applicationEventPublisher.publishEvent(new OrderApprovalEvent(order));

        return order;
    }

    private void validateOrder(Order order) throws OrderApprovalException {
        record Quantity(String productName, int requestedQuantity, int actualQuantity) {}

        Map<UUID, Record> insufficientQuantity = new HashMap<>();
        Map<UUID, String> missingProducts = new HashMap<>();

        order.getItems().forEach(i -> {
            if (i.getProduct() == null || i.getProduct().getStatus() != Product.Status.ACTIVE) {
                missingProducts.put(i.getId().getProductReferenceId(), i.getProductName());
                return;
            }

            if (i.getProduct().getStockQuantity() < i.getQuantity()) {
                insufficientQuantity.put(i.getProduct().getId(), new Quantity(i.getProductName(), i.getQuantity(), i.getProduct().getStockQuantity()));
            }
        });

        if (!insufficientQuantity.isEmpty() || !missingProducts.isEmpty()) {
            throw new OrderApprovalException(insufficientQuantity, missingProducts);
        }
    }

    @Override
    @Transactional
    public Order rejectOrder(UUID id) {
        Order order = orderRepository.findByIdAndStatus(id, Order.Status.PENDING)
                .orElseThrow(() -> new OrderNotFoundException(id, Order.Status.PENDING.name()));

        order.reject();

        applicationEventPublisher.publishEvent(new OrderRejectionEvent(order));

        return order;
    }

    @Override
    public Order refundOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.refund();
        return order;
    }
}
