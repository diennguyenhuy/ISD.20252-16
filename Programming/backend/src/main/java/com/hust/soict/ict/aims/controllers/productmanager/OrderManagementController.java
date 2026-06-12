package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.ordermanagement.OrderManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("manager/orders")
@RequiredArgsConstructor
public class OrderManagementController {
    private final OrderManagementService orderManagementService;

    @GetMapping("/pending")
    public Page<OrderResponse> getPendingOrders(
            @PageableDefault(size = 30,
                    sort = "createdAt",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return orderManagementService.getPendingOrders(pageable);
    }

    @GetMapping
    public Page<OrderResponse> getOrders(
            @RequestParam(required = false) Order.Status status,
            @PageableDefault(size = 30,
                    sort = "updatedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return orderManagementService.getOrders(status, pageable);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable UUID orderId) {
        return orderManagementService.getOrderById(orderId);
    }

    @PostMapping("/{orderId}/approve")
    public OrderResponse approveOrder(@PathVariable UUID orderId) {
        return orderManagementService.approveOrder(orderId);
    }

    @PostMapping("/{orderId}/reject")
    public OrderResponse rejectOrder(@PathVariable UUID orderId) {
        return orderManagementService.rejectOrder(orderId);
    }
}
