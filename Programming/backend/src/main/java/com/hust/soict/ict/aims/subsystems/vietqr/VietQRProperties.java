package com.hust.soict.ict.aims.subsystems.vietqr;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vietqr")
@Getter @Setter
class VietQRProperties {
    private String username;
    private String password;
    private String apiBaseUrl;
    private String accountNo;
    private String accountName;
    private String bankCode;

}
