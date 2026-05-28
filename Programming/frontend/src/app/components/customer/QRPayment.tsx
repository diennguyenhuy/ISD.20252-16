import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router';
import { CreditCard, X, Smartphone, CheckCircle2, Loader2, AlertCircle, RefreshCw } from 'lucide-react';
import QRCode from 'qrcode';
import PayOrderService from '../../api/payOrderService';
import OrderService from '../../api/orderService';
import type { QRCodeResponse, PaymentStatusResponse } from '../../models/payment.interface';

/** How often (ms) to automatically poll payment status while the screen is open */
const POLL_INTERVAL_MS = 5000;

export default function QRPayment() {
    const navigate = useNavigate();

    // ── QR generation state ──────────────────────────────────────────────────
    const [qrData, setQrData] = useState<QRCodeResponse | null>(null);
    const [qrImageDataUrl, setQrImageDataUrl] = useState<string | null>(null);
    const [qrLoading, setQrLoading] = useState(true);
    const [qrError, setQrError] = useState<string | null>(null);

    // ── Payment / confirm state ──────────────────────────────────────────────
    const [paymentStatus, setPaymentStatus] = useState<PaymentStatusResponse | null>(null);
    const [confirming, setConfirming] = useState(false);
    const [confirmError, setConfirmError] = useState<string | null>(null);
    const [confirmCancel, setConfirmCancel] = useState(false);

    const pollRef = useRef<ReturnType<typeof setInterval> | null>(null);

    // ── Generate QR on mount ─────────────────────────────────────────────────
    useEffect(() => {
        generateQR();
        return () => stopPolling();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);


    useEffect(() => {
        if (!qrData?.qrCode) {
            setQrImageDataUrl(null);
            return;
        }
        QRCode.toDataURL(qrData.qrCode, {
            width: 220,
            margin: 1,
            color: { dark: '#000000', light: '#ffffff' },
        })
            .then(setQrImageDataUrl)
            .catch(() => setQrImageDataUrl(null));
    }, [qrData]);

    // ── Auto-navigate once status is COMPLETED ───────────────────────────────
    useEffect(() => {
        if (paymentStatus?.status === 'COMPLETED') {
            stopPolling();
            handleConfirmPayment();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [paymentStatus]);

    // ────────────────────────────────────────────────────────────────────────

    function stopPolling() {
        if (pollRef.current) {
            clearInterval(pollRef.current);
            pollRef.current = null;
        }
    }

    function startPolling() {
        stopPolling();
        pollRef.current = setInterval(async () => {
            try {
                const status = await PayOrderService.checkPaymentStatus();
                setPaymentStatus(status);
            } catch {
                // silently ignore individual poll failures
            }
        }, POLL_INTERVAL_MS);
    }

    async function generateQR() {
        setQrLoading(true);
        setQrError(null);
        try {
            const data = await PayOrderService.generateQRCode();
            setQrData(data);
            startPolling();
        } catch (err: any) {
            const msg =
                err?.response?.data?.message ||
                err?.response?.data ||
                'Failed to generate QR code. Please try again.';
            setQrError(typeof msg === 'string' ? msg : 'Failed to generate QR code.');
        } finally {
            setQrLoading(false);
        }
    }

    async function handleConfirmPayment() {
        if (confirming) return;
        setConfirming(true);
        setConfirmError(null);
        stopPolling();
        try {
            const order = await PayOrderService.confirmPayment();
            navigate(`/checkout/success/${order.id}`);
        } catch (err: any) {
            const msg =
                err?.response?.data?.message ||
                err?.response?.data ||
                'Payment could not be confirmed. Please check your status and try again.';
            setConfirmError(typeof msg === 'string' ? msg : 'Confirmation failed.');
            setConfirming(false);
            startPolling(); // resume polling so status can update
        }
    }

    async function handleCancelOrder() {
        stopPolling();
        try {
            await OrderService.cancelOrderPlacement();
        } catch {
            // best-effort cancel
        }
        navigate('/cart');
    }


    function getQRImageSrc(): string | null {
        return qrImageDataUrl ?? null;
    }

    const isPaid = paymentStatus?.status === 'COMPLETED';
    const qrSrc = getQRImageSrc();

    // ────────────────────────────────────────────────────────────────────────
    return (
        <div className="max-w-lg mx-auto px-4 py-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
            {/* Progress */}
            <div className="flex items-center gap-2 mb-8 text-sm font-medium overflow-x-auto pb-2 whitespace-nowrap hide-scrollbar">
                <span className="text-muted-foreground">Cart</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-muted-foreground">Delivery Information</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-muted-foreground">Invoice</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-primary px-3 py-1.5 bg-primary/10 rounded-md shadow-sm">QR Payment</span>
            </div>

            <div className="bg-card rounded-3xl border border-border shadow-2xl p-6 sm:p-8 text-center relative overflow-hidden">
                {/* Subtle background cinematic glow */}
                <div className="absolute top-0 right-0 w-64 h-64 bg-primary/5 rounded-full blur-3xl -mr-32 -mt-32 pointer-events-none" />

                <div className="inline-flex items-center justify-center p-3.5 bg-primary/10 rounded-2xl mb-4 text-primary relative z-10 shadow-inner">
                    <Smartphone size={28} />
                </div>
                <h1 className="text-2xl sm:text-3xl text-foreground font-bold tracking-tight mb-2 relative z-10">
                    QR Code Payment
                </h1>
                <p className="text-muted-foreground text-sm mb-8 font-medium relative z-10">
                    Open your banking app and scan the QR code below to pay
                </p>

                {/* ── QR Code area ── */}
                <div className="flex justify-center mb-8 relative z-10">
                    <div className="absolute inset-0 bg-primary/20 blur-2xl rounded-full scale-75 pointer-events-none" />
                    <div className="p-4 bg-white rounded-[2rem] border-4 border-muted shadow-xl relative z-10 flex items-center justify-center"
                         style={{ minWidth: 228, minHeight: 228 }}>

                        {qrLoading && (
                            <div className="flex flex-col items-center gap-3 p-8">
                                <Loader2 size={40} className="animate-spin text-primary" />
                                <p className="text-xs text-muted-foreground font-medium">Generating QR code…</p>
                            </div>
                        )}

                        {!qrLoading && qrError && (
                            <div className="flex flex-col items-center gap-3 p-6">
                                <AlertCircle size={36} className="text-destructive" />
                                <p className="text-xs text-destructive font-semibold text-center">{qrError}</p>
                                <button
                                    onClick={generateQR}
                                    className="flex items-center gap-1.5 text-xs text-primary font-bold hover:underline mt-1"
                                >
                                    <RefreshCw size={13} /> Retry
                                </button>
                            </div>
                        )}

                        {!qrLoading && !qrError && qrSrc && (
                            <img
                                src={qrSrc}
                                alt="VietQR Payment QR Code"
                                width={220}
                                height={220}
                                className="rounded-lg shadow-sm"
                            />
                        )}
                    </div>
                </div>

                {/* ── Bank info (from real API response) ── */}
                {!qrLoading && qrData && (
                    <div className="bg-muted/30 border border-border rounded-2xl p-5 mb-8 text-left space-y-3.5 text-sm z-10 relative">
                        <div className="flex justify-between items-center border-b border-border/50 pb-3">
                            <span className="text-muted-foreground font-medium">Bank</span>
                            <span className="text-foreground font-bold">{qrData.bankName || '—'}</span>
                        </div>
                        <div className="flex justify-between items-center border-b border-border/50 pb-3">
                            <span className="text-muted-foreground font-medium">Account Number</span>
                            <span className="text-foreground font-bold tracking-widest">{qrData.bankAccount || '—'}</span>
                        </div>
                        <div className="flex justify-between items-start pt-1">
                            <span className="text-muted-foreground font-medium whitespace-nowrap mr-4">Payment Status</span>
                            <span className={`font-bold text-right px-2 py-1 rounded-md border text-xs uppercase ${
                                isPaid
                                    ? 'bg-green-500/10 border-green-500/30 text-green-600'
                                    : paymentStatus?.status === 'FAILED'
                                    ? 'bg-destructive/10 border-destructive/30 text-destructive'
                                    : 'bg-background border-border text-muted-foreground'
                            }`}>
                                {paymentStatus?.status ?? 'Awaiting scan…'}
                            </span>
                        </div>
                    </div>
                )}

                {/* ── Placeholder bank info while loading ── */}
                {(qrLoading || (!qrData && !qrError)) && (
                    <div className="bg-muted/30 border border-border rounded-2xl p-5 mb-8 text-left space-y-3.5 text-sm z-10 relative animate-pulse">
                        <div className="h-4 bg-muted rounded w-3/4 mx-auto" />
                        <div className="h-4 bg-muted rounded w-1/2 mx-auto" />
                        <div className="h-4 bg-muted rounded w-2/3 mx-auto" />
                    </div>
                )}

                {/* ── Confirm error message ── */}
                {confirmError && (
                    <div className="bg-destructive/10 border border-destructive/20 rounded-xl p-4 mb-4 flex items-start gap-2 text-left animate-in fade-in duration-200">
                        <AlertCircle size={18} className="text-destructive shrink-0 mt-0.5" />
                        <p className="text-sm text-destructive font-medium">{confirmError}</p>
                    </div>
                )}

                <div className="space-y-4 relative z-10">
                    {/* Confirm / Paid button */}
                    <button
                        id="btn-confirm-payment"
                        onClick={handleConfirmPayment}
                        disabled={isPaid || confirming || qrLoading || !!qrError}
                        className={`w-full py-4 rounded-xl font-bold text-base transition-all flex items-center justify-center gap-2 shadow-md ${
                            isPaid
                                ? 'bg-primary/20 text-primary border-2 border-primary shadow-primary/20 cursor-default'
                                : confirming
                                ? 'bg-primary/50 text-primary-foreground cursor-wait'
                                : qrLoading || !!qrError
                                ? 'bg-muted text-muted-foreground cursor-not-allowed'
                                : 'bg-primary text-primary-foreground hover:bg-accent hover:text-accent-foreground hover:shadow-primary/30 hover:-translate-y-0.5'
                        }`}
                    >
                        {isPaid ? (
                            <><CheckCircle2 size={20} className="animate-in zoom-in" /> Paid</>
                        ) : confirming ? (
                            <><Loader2 size={20} className="animate-spin" /> Confirming…</>
                        ) : (
                            'I have successfully paid'
                        )}
                    </button>

                    {/* Switch to PayPal */}
                    <button
                        id="btn-switch-paypal"
                        onClick={() => navigate('/checkout/payment/paypal')}
                        className="w-full py-3.5 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2 shadow-sm"
                    >
                        <CreditCard size={18} />
                        Switch to PayPal Payment
                    </button>

                    {/* Cancel */}
                    {!confirmCancel ? (
                        <button
                            id="btn-cancel-order"
                            onClick={() => setConfirmCancel(true)}
                            className="w-full py-3.5 rounded-xl border border-destructive/30 text-destructive font-semibold hover:bg-destructive/10 transition-colors flex items-center justify-center gap-2"
                        >
                            <X size={18} />
                            Cancel Order
                        </button>
                    ) : (
                        <div className="bg-destructive/10 border border-destructive/20 rounded-2xl p-5 animate-in fade-in zoom-in-95 duration-200">
                            <p className="text-sm text-destructive font-bold mb-4">
                                Are you sure you want to cancel the order?
                            </p>
                            <div className="flex gap-3">
                                <button
                                    id="btn-cancel-no"
                                    onClick={() => setConfirmCancel(false)}
                                    className="flex-1 py-2.5 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors"
                                >
                                    No
                                </button>
                                <button
                                    id="btn-cancel-confirm"
                                    onClick={handleCancelOrder}
                                    className="flex-1 py-2.5 rounded-xl bg-destructive text-destructive-foreground font-bold hover:opacity-90 transition-opacity shadow-lg shadow-destructive/20"
                                >
                                    Confirm Cancellation
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}