package com.hust.soict.ict.aims.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@Data
public class CartResponse {
    private Collection<CartItemResponse> items;
    private int totalQuantity;
    private long totalPrice;
}
