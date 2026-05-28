
export interface QRCodeResponse {

    qrCode: string;

    qrLink: string;

    bankName: string;

    bankAccount: string;
}


export interface PaymentStatusResponse {
    status: 'PENDING' | 'COMPLETED' | 'FAILED' | 'CANCELLED' | string;
    message: string;
}
