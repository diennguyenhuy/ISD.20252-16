import { apiClient } from '../client';
import type { Order } from '../../models';

export interface QRCodeResponse {
    qrCode: string;
    qrLink: string;
    bankName: string;
    bankAccount: string;
    userBankName: string;
    content: string;
    amount: number | null;
}


export interface PaymentStatusResponse {
    status: 'PENDING' | 'COMPLETED' | 'FAILED' | 'CANCELLED' | string;
    message: string;
}

const PAY_ORDER_URL = '/order/payment/vietqr';

export const PayOrderService = {

    generateQRCode: async (): Promise<QRCodeResponse> => {
        const response = await apiClient.post<QRCodeResponse>(`${PAY_ORDER_URL}`);
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
