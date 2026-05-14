package com.hust.soict.ict.aims.models.dto.response;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor
@Setter
public class CartResponse {
    private Collection<CartItemResponse> items;
    private int totalQuantity;
    private long totalPrice;
}
