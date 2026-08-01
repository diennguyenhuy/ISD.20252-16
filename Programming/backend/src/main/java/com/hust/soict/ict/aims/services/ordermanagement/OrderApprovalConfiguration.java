package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class OrderApprovalConfiguration {
    @Primary @Bean
    public OrderApprovalService orderApprovalService(
            OrderManagementServiceImpl orderManagementServiceImpl,
            ProductLogRepository productLogRepository,
            OrderRepository orderRepository,
            AuthenticationFacade authenticationFacade
    ) {
        return new LoggingStockDeduction(
                orderManagementServiceImpl,
                productLogRepository,
                orderRepository,
                authenticationFacade
        );
    }
}
