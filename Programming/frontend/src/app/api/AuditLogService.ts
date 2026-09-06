import { apiClient } from "./client";
import type { ProductLog, AdminLog, OrderLog } from "../models"

export const ProductAuditLogService = {
    getProductLogs: async (): Promise<ProductLog[]> => {
        const result = await apiClient.get<ProductLog[]>("manager/products/logs");
        return result.data;
    },
    getProductLogsByProductId: async (productId: string): Promise<ProductLog[]> => {
        const result = await apiClient.get<ProductLog[]>(`manager/products/logs/${productId}`);
        return result.data;
    }
}

export const AdminAuditLogService = {
    getAdminLogs: async (): Promise<AdminLog[]> => {
        const response = await apiClient.get<AdminLog[]>(`/admin/logs`);
        return response.data;
    },
    getAdminLogsByUserId: async (userId: string): Promise<AdminLog[]> => {
        const response = await apiClient.get<AdminLog[]>(`/admin/logs/${userId}`);
        return response.data;
    }
}

export const OrderAuditLogService = {
    getOrderLogs: async (): Promise<OrderLog[]> => {
        const response = await apiClient.get<OrderLog[]>(`manager/orders/logs`);
        return response.data;
    },
    getOrderLogsByOrderId: async (orderId: string): Promise<OrderLog[]> => {
        const response = await apiClient.get<OrderLog[]>(`order/history/${orderId}`);
        return response.data;
    }
}