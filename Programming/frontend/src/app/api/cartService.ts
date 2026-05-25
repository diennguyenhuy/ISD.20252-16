import {apiClient} from './client';
import type { Cart, CartItem } from "../models/cart.interface";

const CartService = {
    getCart: async (): Promise<Cart> => {
        const response = await apiClient.get<Cart>('cart');
        return response.data;
    },

    addToCart: async (productId: string, quantity: number): Promise<Cart> => {
        const response = await apiClient.post<Cart>(`cart/items/${productId}`, null, {
            params: { quantity },
        });
        return response.data;
    },

    updateCartItem: async (productId: string, quantity: number): Promise<Cart> => {
        const response = await apiClient.put<Cart>(`cart/items/${productId}`, null, {
            params: { quantity },
        });
        return response.data;
    },

    removeFromCart: async (productId: string): Promise<Cart> => {
        const response = await apiClient.delete<Cart>(`cart/items/${productId}`);
        return response.data;
    },

    clearCart: async (): Promise<void> => {
        await apiClient.delete<Cart>('cart/items');
    }
}

export default CartService;