package com.hust.soict.ict.aims.services.context.provider;

import com.hust.soict.ict.aims.services.context.CartContext;
import com.hust.soict.ict.aims.models.cart.Cart;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionCartProvider implements CartContext {
    private final HttpSession session;

    private static final String CART_SESSION_KEY = "CART_SESSION_KEY";

    @Override
    public Cart getOrCreateCart() {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new Cart();
            setCart(cart);
        }

        return cart;
    }

    @Override
    public void setCart(Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }
}
