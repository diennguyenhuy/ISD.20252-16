package com.hust.soict.ict.aims.context.provider;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.models.cart.Cart;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionCartProvider implements CartContext {
    private final HttpSession session;

    private static final String CART_SESSION_KEY = "CART_SESSION_KEY";

    @Override
    public Cart getOrCreateCart() {
        String sessionId = session.getId();
        log.debug("getOrCreateCart: sessionId={}", sessionId);

        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            log.debug("No cart found. Creating cart for sessionId={}", sessionId);
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
