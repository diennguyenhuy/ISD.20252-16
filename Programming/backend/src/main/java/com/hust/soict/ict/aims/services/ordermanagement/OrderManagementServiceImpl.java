package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.dto.mapper.OrderMapper;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
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
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Page<OrderResponse> getPendingOrders(Pageable pageable) {
        return orderRepository.findAllByStatus(Order.Status.PENDING, pageable).map(orderMapper::toOrderResponse);
    }

    public Page<OrderResponse> getOrders(Order.Status status, Pageable pageable) {
        if (status == null) {
            return orderRepository.findAll(pageable).map(orderMapper::toOrderResponse);
        }
        return orderRepository.findAllByStatus(status, pageable).map(orderMapper::toOrderResponse);
    }

    public OrderResponse getOrderById(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse approveOrder(UUID id) throws OrderApprovalException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        validateOrder(order);

        order.getItems().forEach(i -> i.getProduct().updateStock(i.getProduct().getStockQuantity() - i.getQuantity()));

        order.approve();

        applicationEventPublisher.publishEvent(new OrderApprovalEvent(order));

        return orderMapper.toOrderResponse(order);
    }

    private void validateOrder(Order order) throws OrderApprovalException {
        Map<UUID, Integer> insufficientQuantity = new HashMap<>();
        List<String> missingProducts = new ArrayList<>();

        order.getItems().forEach(i -> {
            if (i.getProduct() == null || i.getProduct().getStatus() != Product.Status.ACTIVE) {
                missingProducts.add(i.getProductName());
                return;
            }

            if (i.getProduct().getStockQuantity() < i.getQuantity()) {
                insufficientQuantity.put(i.getProduct().getId(), i.getProduct().getStockQuantity());
            }
        });

        if (!insufficientQuantity.isEmpty() || !missingProducts.isEmpty()) {
            throw new OrderApprovalException(insufficientQuantity, missingProducts);
        }
    }

    @Transactional
    public OrderResponse rejectOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

        order.reject();

        applicationEventPublisher.publishEvent(new OrderRejectionEvent(order));

        return orderMapper.toOrderResponse(order);
    }
}
