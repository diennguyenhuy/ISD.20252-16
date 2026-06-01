package com.hust.soict.ict.aims.config;

import com.hust.soict.ict.aims.subsystems.vietqr.IPaymentQRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.VietQRController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration that exposes a  VietQRController instance as the
 * IPaymentQRCode bean consumed by {@code PayOrderService}.
 *
 * <p>Because  VietQRControlleris a plain Java class (not annotated with
 * {@code @Component} / {@code @Service}), it cannot be discovered by component
 * scanning. This {@code @Configuration} class bridges that gap: it reads the
 * VietQR credentials and bank account details from VietQRProperties
 * (which is populated from {@code application.yaml}) and passes them to the
 * VietQRController constructor.
 *
 * <p>Coupling level with VietQRProperties: DATA coupling — only the
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
