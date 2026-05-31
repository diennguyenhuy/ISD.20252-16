import React, {createContext, useContext, useState, useEffect, type ReactNode} from 'react';
import CartService from '../api/cartService';
import type {Cart} from '../models/cart.interface';

interface CartContextType {
    cart: Cart | null;
    loading: boolean;
    totalQuantity: number;
    fetchCart: () => Promise<void>;
    addToCart: (productId: string, quantity: number) => Promise<void>;
    updateQuantity: (productId: string, quantity: number) => Promise<void>;
    removeFromCart: (productId: string) => Promise<void>;
    clearCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

const extractErrorMessage = (error: any, fallback: string): string => {
    if (error.response) {
        if (typeof error.response.data === 'string' && error.response.data.length < 200) {
            return error.response.data;
        }
        if (error.response.data?.message) {
            return error.response.data.message;
        }
        if (error.response.status === 400) return "Invalid input. Please check your request.";
        if (error.response.status === 404) return "The requested item was not found on the server.";
    }
    return fallback;
};

export function CartProvider({children}: { children: ReactNode }) {
    const [cart, setCart] = useState<Cart | null>(null);
    const [loading, setLoading] = useState(true);

    const fetchCart = async () => {
        setLoading(true);
        try {
            const data = await CartService.getCart();
            setCart(data);
        } catch (error) {
            console.error("Failed to fetch cart", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCart();
    }, []);

    const addToCart = async (productId: string, quantity: number) => {
        try {
            const updatedCart = await CartService.addToCart(productId, quantity);
            setCart(updatedCart);
        } catch (e) {
            throw new Error(extractErrorMessage(e, "Failed to add item to cart"));
        }
    };

    const updateQuantity = async (productId: string, quantity: number) => {
        try {
            const updatedCart = await CartService.updateCartItem(productId, quantity);
            setCart(updatedCart);
        } catch (e) {
            throw new Error(extractErrorMessage(e, "Failed to update item to cart"));
        }
    };

    const removeFromCart = async (productId: string) => {
        const updatedCart = await CartService.removeFromCart(productId);
        setCart(updatedCart);
    };

    const clearCart = async () => {
        await CartService.clearCart();
        setCart(null);
    };

    // Pre-calculate the total quantity so components don't have to
    const totalQuantity = cart?.totalQuantity || 0;

    return (
        <CartContext.Provider
            value={{cart, loading, totalQuantity, fetchCart, addToCart, updateQuantity, removeFromCart, clearCart}}>
            {children}
        </CartContext.Provider>
    );
}

// Custom hook for easy access
export function useCart() {
    const context = useContext(CartContext);
    if (!context) {
        throw new Error("useCart must be used within a CartProvider");
    }
    return context;
}