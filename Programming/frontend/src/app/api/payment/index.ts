export type { PayPalCreateResponse } from './PayByCreditCardService';
export type { QRCodeResponse, PaymentStatusResponse } from './PayOrderService';
export { PayOrderService } from './PayOrderService';
export { PayByCreditCardService } from './PayByCreditCardService';
import { apiClient } from "../client";

export async function getAllPaymentMethods() {
    const res = await apiClient.get<string[]>('order/payment');
    return res.data;
}

export async function supportRefund(paymentMethod: string) {
    const res = await apiClient.get<boolean>(`order/payment/${paymentMethod}`);
    return res.data;
}