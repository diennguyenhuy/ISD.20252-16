import { apiClient } from './client';
import type { QRCodeResponse, PaymentStatusResponse } from '../models/payment.interface';
import type { Order } from '../models/order.interface';

const PAY_ORDER_URL = '/order/payment/vietqr';

const PayOrderService = {

    generateQRCode: async (): Promise<QRCodeResponse> => {
        const response = await apiClient.post<QRCodeResponse>(`${PAY_ORDER_URL}/qr`);
        return response.data;
    },

    checkPaymentStatus: async (): Promise<PaymentStatusResponse> => {
        const response = await apiClient.get<PaymentStatusResponse>(`${PAY_ORDER_URL}/status`);
        return response.data;
    },

    confirmPayment: async (): Promise<Order> => {
        const response = await apiClient.post<Order>(`${PAY_ORDER_URL}/confirm`);
        return response.data;
    },

    /**
     * Simulates the VietQR payment callback for sandbox / dev testing.
     * Calls POST /order/payment/vietqr/test-callback which seeds PaymentCallbackData
     * into the backend session, so the subsequent confirmPayment() call uses
     * real-shaped data instead of local fallback values.
     */
    simulateTestCallback: async (): Promise<void> => {
        await apiClient.post(`${PAY_ORDER_URL}/test-callback`);
    },
};

export default PayOrderService;
