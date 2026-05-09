package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class DeliveryResponse {
    private String customerName;
    private String customerEmail;
    private String phoneNumber;
    private String province;
    private String commune;
    private String address;
    private String deliveryMethod;
}
