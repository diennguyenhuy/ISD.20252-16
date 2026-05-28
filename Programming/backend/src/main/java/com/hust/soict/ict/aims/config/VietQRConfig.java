package com.hust.soict.ict.aims.config;

import com.hust.soict.ict.aims.IPaymentQRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.VietQRController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration that exposes a {@link VietQRController} instance as the
 * {@link IPaymentQRCode} bean consumed by {@code PayOrderService}.
 *
 * <p>Because {@link VietQRController} is a plain Java class (not annotated with
 * {@code @Component} / {@code @Service}), it cannot be discovered by component
 * scanning. This {@code @Configuration} class bridges that gap: it reads the
 * VietQR credentials and bank account details from {@link VietQRProperties}
 * (which is populated from {@code application.yaml}) and passes them to the
 * {@link VietQRController} constructor.
 *
 * <p>Coupling level with {@link VietQRProperties}: DATA coupling — only the
 * primitive String values (username, password, bankCode, accountNo, accountName,
 * apiBaseUrl) are extracted and forwarded; no composite objects or control
 * flags are passed.
 */
@Configuration
public class VietQRConfig {

    @Bean
    public IPaymentQRCode paymentQRCode(VietQRProperties props) {
        return new VietQRController(
                props.getUsername(),
                props.getPassword(),
                props.getBankCode(),
                props.getAccountNo(),
                props.getAccountName(),
                props.getApiBaseUrl()
        );
    }
}
