package com.hust.soict.ict.aims.models.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private long totalPrice;
}
