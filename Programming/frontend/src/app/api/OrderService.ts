import {apiClient} from './client';
import type {DeliveryInformation, Invoice, Order, OrderDraft} from "../models/order.interface";

const OrderService = {
    placeOrder: async (): Promise<OrderDraft> => {
        const response = await apiClient.post<OrderDraft>('/order')
        return response.data;
    },

    submitDeliveryForm: async (deliveryForm: DeliveryInformation): Promise<DeliveryInformation> => {
        const response = await apiClient.post<DeliveryInformation>(`order/delivery`, deliveryForm);
        return response.data;
    },

    getInvoice: async (): Promise<Invoice> => {
        const response = await apiClient.get<Invoice>(`order/invoice`);
        return response.data;
    },

    finalizeOrder: async (): Promise<Order> => {
        const response = await apiClient.post<Order>(`order/finalize`);
        return response.data;
    },

    cancelOrderPlacement: async (): Promise<void> => {
        await apiClient.delete<void>('/order');
    },

    cancelOrder: async (orderId: string): Promise<void> => {
        await apiClient.post<void>(`order/${orderId}/cancel`);
    },

    getOrder: async (orderId: string): Promise<Order> => {
        const response = await apiClient.get<Order>(`order/${orderId}`);
        return response.data;
    }
};

export default OrderService;