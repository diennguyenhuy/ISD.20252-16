export type OrderStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED' | 'REFUNDED';

export interface Order {
    readonly id: string;
    items: OrderItem[];
    status: OrderStatus;
    deliveryInformation: DeliveryInformation;
    invoice: Invoice;
    paymentTransaction: PaymentTransaction;
    readonly createdAt: string;
    readonly updatedAt: string;
}

export type OrderDraft = Pick<Order, 'items'> & Partial<Pick<Order, 'deliveryInformation' | 'invoice'>> & { checkoutId: string };

export interface OrderItem {
    readonly productId?: string;
    productName: string;
    productImage?: string;
    quantity: number;
    unitPrice: number;
    unitWeight: number;
}

export interface DeliveryInformation {
    customerName: string;
    customerEmail: string;
    phoneNumber: string;
    province: string;
    commune: string;
    address: string;
    deliveryMethod: string;
}

export interface Invoice {
    invoiceNumber: string;
    issuedAt: string;
    totalPriceWithoutVAT: number;
    totalPriceWithVAT: number;
    deliveryFee: number;
    totalAmount: number;
}

export type TransactionMethod = 'VIETQR' | 'PAYPAL';

export interface PaymentTransaction {
    readonly id: string;
    transactionContent: string;
    transactionTimestamp: string;
    transactionMethod: TransactionMethod;
    amountPaid: number;
}
