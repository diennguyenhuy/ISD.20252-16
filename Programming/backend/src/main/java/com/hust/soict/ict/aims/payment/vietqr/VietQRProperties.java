package com.hust.soict.ict.aims.payment.vietqr;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vietqr")
record VietQRProperties(
        String username,
        String password,
        String apiBaseUrl,
        String accountNo,
        String accountName,
        String bankCode
) {}
