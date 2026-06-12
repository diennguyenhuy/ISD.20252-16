package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.cart.Cart;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockValidatorTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartContext cartContext;

    @InjectMocks
    private StockValidator stockValidator;

    @Test
    @DisplayName("UT001: Test Cart Empty")
    void shouldThrowWhenCartIsEmpty() {
        Cart cart = new Cart();
        when(cartContext.getOrCreateCart()).thenReturn(cart);


        assertThrows(EmptyCartException.class, () -> stockValidator.checkStockAvailability(cart));

        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("UT002: Test Valid Stock")
    void shouldReturnCartWhenValidStock() {
        UUID productId = UUID.randomUUID();

        Product product = Mockito.mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getStockQuantity()).thenReturn(10);

        Cart cart = new Cart();
        when(cartContext.getOrCreateCart()).thenReturn(cart);

        cart.addItem(product, 2);

        when(productRepository.findAllByIdInAndStatus(any(), eq(Product.Status.ACTIVE))).thenReturn(List.of(product));

        Cart result = stockValidator.checkStockAvailability(cart);

        assertNotNull(result);

        assertEquals(1, result.getItems().size());
    }

    @Test
    @DisplayName("UT003: Test Insufficient Stock product")
    void shouldThrowWhenStockInsufficientQuantity() {
        UUID productId = UUID.randomUUID();

        Product product = Mockito.mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getStockQuantity()).thenReturn(1);

        Cart cart = new Cart();
        when(cartContext.getOrCreateCart()).thenReturn(cart);
        cart.addItem(product, 5);

        when(productRepository.findAllByIdInAndStatus(any(), eq(Product.Status.ACTIVE))).thenReturn(List.of(product));

        NotEnoughStockException x = assertThrows(NotEnoughStockException.class, () -> stockValidator.checkStockAvailability(cart));

        assertTrue(x.getInsufficientQuantity().containsKey(productId));
    }

    @Test
    @DisplayName("UT004: Test vanished product")
    void shouldRemoveProductAndThrowWhenVanishedProduct() {
        UUID productId = UUID.randomUUID();

        Product product = Mockito.mock(Product.class);
        when(product.getId()).thenReturn(productId);

        Cart cart = new Cart();
        when(cartContext.getOrCreateCart()).thenReturn(cart);
        cart.addItem(product, 2);

        when(productRepository.findAllByIdInAndStatus(any(), eq(Product.Status.ACTIVE))).thenReturn(List.of());

        ProductNotFoundException x = assertThrows(ProductNotFoundException.class, () -> stockValidator.checkStockAvailability(cart));

        //assertEquals(productId, x.getProductId());

        assertTrue(cart.isEmpty());
    }
}
