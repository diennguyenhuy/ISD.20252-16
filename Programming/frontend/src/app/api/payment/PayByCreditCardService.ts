import { apiClient } from '../client';
import type { Order } from '../../models';

export interface PayPalCreateResponse {
    approvalUrl: string;
    providerOrderId: string;
}

const PAYPAL_URL = '/order/payment/paypal';

/**
 * PayPal (credit-card) payment service — counterpart of PayOrderService (VietQR).
 *
 * Flow:
 *   1. createPayPalPayment()  → backend creates a PayPal order, returns the approval URL
 *   2. (frontend opens approvalUrl; PayPal redirects back with ?token=...)
 *   3. capturePayPalPayment(token) → backend captures, finalizes the order, returns Order
 *   4. cancelPayPalPayment()  → best-effort notify backend the customer cancelled
 *
 * Kept in its own file (not PayOrderService.ts) to preserve single-responsibility:
 * PayOrderService stays VietQR-only, this stays PayPal-only.
 */
export const PayByCreditCardService = {
    createPayPalPayment: async (): Promise<PayPalCreateResponse> => {
        const response = await apiClient.post<PayPalCreateResponse>(`${PAYPAL_URL}`);
        return response.data;
    },

    capturePayPalPayment: async (token: string): Promise<Order> => {
        const response = await apiClient.post<Order>(`${PAYPAL_URL}/capture?token=${token}`);
        return response.data;
    },

    cancelPayPalPayment: async (): Promise<void> => {
        await apiClient.post(`${PAYPAL_URL}/cancel`);
    },
};

export default PayByCreditCardService;
