package com.hust.soict.ict.aims.controllers.order;

import com.hust.soict.ict.aims.models.dto.response.CartResponse;
import com.hust.soict.ict.aims.services.order.CartService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Provide API endpoints for cart screen
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    @GetMapping
    public @ResponseBody CartResponse getOrCreateCart() {
        return cartService.getOrCreateCart();
    }

    @PostMapping("/items")
    public @ResponseBody CartResponse addItemToCart(
            @RequestParam(name = "productId") UUID productId,
            @RequestParam(name = "quantity") @Positive int quantity
    ) {
        return cartService.addItem(productId, quantity);
    }

    @PutMapping("/items")
    public @ResponseBody CartResponse updateItem(
            @RequestParam(name = "productId") UUID productId,
            @RequestParam(name = "quantity") @PositiveOrZero int quantity
    ) {
        return cartService.updateItem(productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public @ResponseBody CartResponse deleteItem(
            @PathVariable UUID productId
    ) {
        return cartService.removeItem(productId);
    }

    @DeleteMapping
    public @ResponseBody CartResponse clearCart() {
        return cartService.clearCart();
    }
}
