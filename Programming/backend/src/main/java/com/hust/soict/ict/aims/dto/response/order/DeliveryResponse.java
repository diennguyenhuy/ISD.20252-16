package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeliveryResponse {
    private String customerName;
    private String customerEmail;
    private String phoneNumber;
    private String province;
    private String commune;
    private String address;
    private String deliveryMethod;
}
