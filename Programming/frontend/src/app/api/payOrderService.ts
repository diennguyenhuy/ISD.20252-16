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
};

export default PayOrderService;
