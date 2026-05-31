import { apiClient } from './client';
import type { Order, OrderStatus } from '../models/order.interface';

export const ManagerOrderService = {
    getAllOrders: async (): Promise<Order[]> => {
        const response = await apiClient.get<Order[]>('manager/orders');
        return response.data;
    },

    updateOrderStatus: async (id: string, status: OrderStatus): Promise<Order> => {
        const response = await apiClient.patch<Order>(`manager/orders/${id}/status`, { status });
        return response.data;
    }
};