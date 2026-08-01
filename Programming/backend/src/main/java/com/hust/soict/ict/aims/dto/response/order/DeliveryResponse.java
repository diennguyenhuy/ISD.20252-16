package com.hust.soict.ict.aims.dto.response.order;

public record DeliveryResponse(
        String customerName,
        String customerEmail,
        String phoneNumber,
        String province,
        String commune,
        String address,
        String deliveryMethod
) {}
