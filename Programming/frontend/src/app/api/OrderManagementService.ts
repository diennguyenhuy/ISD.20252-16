import { apiClient } from "./client";
import type { Order, OrderStatus } from "../models/order.interface";
import type { Page } from "./Page";

const OrderManagementService = {
    getPendingOrders: async (page: number): Promise<Order[]> => {
        const response = await apiClient.get<Page<Order>>(`manager/orders/pending?page=${page}`);
        return response.data.content;
    },

    getOrders: async (page: number, status?: OrderStatus): Promise<Order[]> => {
        let requestURL = `manager/orders?page=${page}`;
        if (status) requestURL += `&status=${status}`;
        const response = await apiClient.get<Page<Order>>(requestURL);
        return response.data.content;
    },

    getOrder: async (id: string): Promise<Order> => {
        const response = await apiClient.get<Order>(`manager/orders/${id}`);
        return response.data;
    },

    approveOrder: async (id: string): Promise<Order> => {
        const response = await apiClient.post<Order>(`manager/orders/${id}/approve`);
        return response.data;
    },

    rejectOrder: async (id: string): Promise<Order> => {
        const response = await apiClient.post<Order>(`manager/orders/${id}/reject`);
        return response.data;
    }
}

export default OrderManagementService;