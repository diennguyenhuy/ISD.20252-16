
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
