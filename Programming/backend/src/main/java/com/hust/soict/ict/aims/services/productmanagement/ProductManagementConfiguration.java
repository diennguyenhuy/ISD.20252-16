package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class ProductManagementConfiguration {
    @Bean @Primary
    public ProductManagementService productManagementService(
            ProductManagementServiceImpl productManagementService,
            ProductRepository productRepository,
            AuthenticationFacade authenticationFacade,
            ProductLogRepository productLogRepository
    ) {
        return new LoggingProductManagementService(
                productManagementService,
                productRepository,
                authenticationFacade,
                productLogRepository
        );
    }
}
