package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.repositories.AdminLogRepository;
import com.hust.soict.ict.aims.repositories.UserRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class AdminConfiguration {

    @Bean @Primary
    public AdminService adminService(
            AdminServiceImpl adminService,
            UserRepository userRepository,
            AuthenticationFacade authenticationFacade,
            AdminLogRepository adminLogRepository
    ) {
        return new LoggingAdminService(
                adminService,
                userRepository,
                authenticationFacade,
                adminLogRepository
        );
    }
}
