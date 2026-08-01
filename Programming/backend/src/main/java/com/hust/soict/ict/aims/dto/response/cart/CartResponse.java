package com.hust.soict.ict.aims.dto.response.cart;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        int totalQuantity,
        long totalPrice
) {}
