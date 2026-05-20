package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.dto.response.CartResponse;
import com.hust.soict.ict.aims.services.order.CartService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
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

    /**
     * GET /cart<br>
     * Get the current session's cart. If not exist, a new cart will be created.
     * @return the response of cart instance
     */
    @GetMapping
    public CartResponse getOrCreateCart() {
        return cartService.getOrCreateCart();
    }

    /**
     * POST /cart/items/{productId}?quantity={?} <br>
     * Add a product with ID productId with specified quantity to cart.
     * @param productId the requested productId
     * @param quantity the requested amount
     * @return the response of cart instance
     * @throws ProductNotFoundException if product is not found in store
     */
    @PostMapping("/items/{productId}")
    public CartResponse addItemToCart(
            @PathVariable UUID productId,
            @RequestParam(name = "quantity") @Positive int quantity
    ) throws ProductNotFoundException, IllegalArgumentException {
        return cartService.addItem(productId, quantity);
    }

    /**
     * PUT /cart/items/{productId}?quantity={?} <br>
     * Update a product with specified quantity in cart
     * @param productId the requested productId
     * @param quantity the requested amount
     * @return the response of cart instance
     * @throws NoSuchElementException if the item does not exist in cart
     */
    @PutMapping("/items/{productId}")
    public CartResponse updateItem(
            @PathVariable UUID productId,
            @RequestParam(name = "quantity") @PositiveOrZero int quantity
    ) throws NoSuchElementException, IllegalArgumentException {
        return cartService.updateItem(productId, quantity);
    }

    /**
     * DELETE /cart/items/{productId} <br>
     * Remove product from cart
     * @param productId the requested productId
     * @return the response of cart instance
     */
    @DeleteMapping("/items/{productId}")
    public CartResponse deleteItem(
            @PathVariable UUID productId
    ) {
        return cartService.removeItem(productId);
    }

    /**
     * DELETE /cart/items <br>
     * Clear cart
     * @return the response of cart instance
     */
    @DeleteMapping("/items")
    public CartResponse clearCart() {
        return cartService.clearCart();
    }
}
