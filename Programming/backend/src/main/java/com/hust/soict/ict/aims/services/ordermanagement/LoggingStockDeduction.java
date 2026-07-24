package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.exceptions.OrderApprovalException;
import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class LoggingStockDeduction implements OrderApprovalService {
    private final OrderApprovalService orderApprovalService;

    private final ProductLogRepository productLogRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    @Transactional
    public OrderResponse approveOrder(UUID id) throws OrderApprovalException {
        Order order = orderRepository.findByIdAndStatus(id, Order.Status.PENDING)
                .orElseThrow(() -> new OrderNotFoundException(id, Order.Status.PENDING.name()));

        record FormerStockAndRequestedQuantity(Product product, int formerStock, int requestedQuantity) {}

        List<FormerStockAndRequestedQuantity> formerStocksAndRequestedQuantity = order.getItems().stream()
                .map(o -> new FormerStockAndRequestedQuantity(
                        o.getProduct(),
                        o.getProduct().getStockQuantity(),
                        o.getQuantity()
                )).toList();

        var result = orderApprovalService.approveOrder(id);

        List<StockAdjustLog> stockAdjustLogs = new ArrayList<>();

        formerStocksAndRequestedQuantity.forEach(i ->
                stockAdjustLogs.add(new StockAdjustLog(
                        authenticationFacade.getCurrentUser(),
                        i.product,
                        i.formerStock,
                        i.formerStock - i.requestedQuantity,
                        "Approved order #" + order.getId()
                        )
                )
        );

        productLogRepository.saveAll(stockAdjustLogs);

        return result;
    }
}
