import {apiClient} from './client';
import type { Cart, CartItem } from "../models/cart.interface";

const CART_URL = '/cart';

const CartService = {
    getCart: async (): Promise<Cart> => {
        const response = await apiClient.get<Cart>(CART_URL);
        return response.data;
    },

    addToCart: async (productId: string, quantity: number): Promise<Cart> => {
        const response = await apiClient.post<Cart>(`${CART_URL}/items/${productId}`, null, {
            params: { quantity },
        });
        return response.data;
    },

    updateCartItem: async (productId: string, quantity: number): Promise<Cart> => {
        const response = await apiClient.put<Cart>(`${CART_URL}/items/${productId}`, null, {
            params: { quantity },
        });
        return response.data;
    },

    removeFromCart: async (productId: string): Promise<Cart> => {
        const response = await apiClient.delete<Cart>(`${CART_URL}/items/${productId}`);
        return response.data;
    },

    clearCart: async (): Promise<void> => {
        await apiClient.delete<Cart>(CART_URL);
    }
}

export default CartService;