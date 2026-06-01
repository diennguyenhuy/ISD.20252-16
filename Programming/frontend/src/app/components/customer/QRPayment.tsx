import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router';
import { CreditCard, X, Smartphone, CheckCircle2, Loader2, AlertCircle, RefreshCw } from 'lucide-react';
import QRCode from 'qrcode';
import PayOrderService from '../../api/payOrderService';
import OrderService from '../../api/orderService';
import type { QRCodeResponse } from '../../models/payment.interface';

type ConfirmState =
    | 'idle'        // waiting for user to click
    | 'loading'     // test-callback + confirm in progress
    | 'success'     // payment confirmed
    | 'error';      // confirm failed

export default function QRPayment() {
    const navigate = useNavigate();

    // ── QR generation state ──────────────────────────────────────────────────
    const [qrData, setQrData] = useState<QRCodeResponse | null>(null);
    const [qrImageDataUrl, setQrImageDataUrl] = useState<string | null>(null);
    const [qrLoading, setQrLoading] = useState(true);
    const [qrError, setQrError] = useState<string | null>(null);
    const [timeLeft, setTimeLeft] = useState(180);

    // ── Confirm button state ─────────────────────────────────────────────────
    const [confirmState, setConfirmState] = useState<ConfirmState>('idle');
    const [confirmError, setConfirmError] = useState<string | null>(null);

    // ── Cancel confirmation dialog ───────────────────────────────────────────
    const [confirmCancel, setConfirmCancel] = useState(false);

    const qrFetchedRef = useRef(false);

    // ── Generate QR on mount ─────────────────────────────────────────────────
    useEffect(() => {
        if (qrFetchedRef.current) return;  // StrictMode double-invoke guard
        qrFetchedRef.current = true;
        generateQR();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // ── Render QR image from EMV string ──────────────────────────────────────
    useEffect(() => {
        if (!qrData?.qrCode) { setQrImageDataUrl(null); return; }
        QRCode.toDataURL(qrData.qrCode, {
            width: 220, margin: 1,
            color: { dark: '#000000', light: '#ffffff' },
        })
            .then(setQrImageDataUrl)
            .catch(() => setQrImageDataUrl(null));
    }, [qrData]);

    // ── Countdown Timer ──────────────────────────────────────────────────────
    useEffect(() => {
        if (qrLoading || qrError || confirmState === 'success' || timeLeft <= 0) return;
        const timerId = setInterval(() => {
            setTimeLeft((prev) => prev - 1);
        }, 1000);
        return () => clearInterval(timerId);
    }, [qrLoading, qrError, confirmState, timeLeft]);

    // ────────────────────────────────────────────────────────────────────────

    async function generateQR() {
        setQrLoading(true);
        setQrError(null);
        setTimeLeft(180);
        try {
            const data = await PayOrderService.generateQRCode();
            setQrData(data);
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

    /**
     * Called when the user clicks "I have successfully paid".
     *
     * Flow:
     *   1. simulateTestCallback() — seeds PaymentCallbackData into backend session
     *      (simulates VietQR pushing the payment notification to our callback endpoint)
     *   2. confirmPayment()       — reads the callback data, creates PaymentTransaction,
     *      finalizes the order via PlaceOrderService
     *   3. On success → show green banner → navigate to success screen after 1.5 s
     *   4. On error   → show error message, re-enable the button for retry
     */
    async function handleConfirmPayment() {
        if (confirmState === 'loading' || confirmState === 'success') return;

        setConfirmState('loading');
        setConfirmError(null);

        try {
            // confirmPayment() calls VietQR's test-callback API internally for
            // verification, then creates the PaymentTransaction and finalizes the order.
            await PayOrderService.confirmPayment();
            // Step 3: success
            setConfirmState('success');
            const order = await OrderService.finalizeOrder();
            setTimeout(() => navigate(`/checkout/success/${order.id}`, {
                state: {
                    placedOrder: order,
                }
            }), 1500);
        } catch (err: any) {
            const msg =
                err?.response?.data?.message ||
                err?.response?.data ||
                'Payment could not be confirmed. Please try again.';
            setConfirmError(typeof msg === 'string' ? msg : 'Confirmation failed.');
            setConfirmState('error');
        }
    }

    async function handleCancelOrder() {
        try { await OrderService.cancelOrderPlacement(); } catch { /* best-effort */ }
        navigate('/cart');
    }

    const isSuccess = confirmState === 'success';
    const isLoading = confirmState === 'loading';
    const qrSrc = qrImageDataUrl ?? null;

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
                {/* Subtle background glow */}
                <div className="absolute top-0 right-0 w-64 h-64 bg-primary/5 rounded-full blur-3xl -mr-32 -mt-32 pointer-events-none" />

                <h1 className="text-2xl sm:text-3xl text-foreground font-bold tracking-tight mb-2 relative z-10">
                    QR Code Payment
                </h1>
                <div className="flex flex-col sm:flex-row items-center justify-center gap-2 mb-8 relative z-10">
                    <p className="text-muted-foreground text-sm font-medium">
                        Open your banking app and scan the QR code below to pay
                    </p>
                </div>

                {/* ── QR Code area ── */}
                <div className="flex flex-col items-center justify-center mb-8 relative z-10">
                    <div className="p-3 bg-white rounded-2xl border border-border shadow-sm relative z-10 flex items-center justify-center mb-4 transition-all"
                         style={{ minWidth: 244, minHeight: 244 }}>

                        {qrLoading && (
                            <div className="flex flex-col items-center gap-3 p-8">
                                <Loader2 size={32} className="animate-spin text-primary/70" />
                                <p className="text-xs text-muted-foreground font-medium">Generating…</p>
                            </div>
                        )}

                        {!qrLoading && qrError && (
                            <div className="flex flex-col items-center gap-3 p-6">
                                <AlertCircle size={32} className="text-destructive/80" />
                                <p className="text-xs text-destructive font-medium text-center">{qrError}</p>
                                <button
                                    onClick={generateQR}
                                    className="flex items-center gap-1.5 text-xs text-primary font-bold hover:underline mt-1"
                                >
                                    <RefreshCw size={13} /> Retry
                                </button>
                            </div>
                        )}

                        {!qrLoading && !qrError && qrSrc && (
                            <div className="relative group rounded-xl overflow-hidden">
                                <img
                                    src={qrSrc}
                                    alt="VietQR Payment QR Code"
                                    width={220} height={220}
                                    className={`transition-all duration-500 ${timeLeft === 0 ? 'blur-md opacity-30 grayscale' : ''}`}
                                />
                                {timeLeft === 0 && (
                                    <div className="absolute inset-0 flex flex-col items-center justify-center bg-white/20 animate-in fade-in duration-300">
                                        <button
                                            onClick={generateQR}
                                            className="p-3.5 bg-white border border-border rounded-full shadow-sm hover:scale-105 active:scale-95 transition-transform text-primary hover:text-primary/80"
                                        >
                                            <RefreshCw size={28} />
                                        </button>
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                    {!qrLoading && !qrError && timeLeft > 0 && (
                        <span className="inline-flex items-center px-3 py-1 rounded-full bg-primary/5 text-primary font-bold text-sm border border-primary/10 shadow-sm animate-in fade-in slide-in-from-top-2">
                            {Math.floor(timeLeft / 60)}:{(timeLeft % 60).toString().padStart(2, '0')}
                        </span>
                    )}
                </div>

                {/* ── Bank info ── */}
                {!qrLoading && qrData && (
                    <div className="bg-muted/30 border border-border rounded-2xl p-5 mb-6 text-left space-y-3.5 text-sm z-10 relative">
                        <div className="flex justify-between items-center border-b border-border/50 pb-3">
                            <span className="text-muted-foreground font-medium">Bank</span>
                            <span className="text-foreground font-bold">{qrData.bankName || '—'}</span>
                        </div>
                        <div className="flex justify-between items-center border-b border-border/50 pb-3">
                            <span className="text-muted-foreground font-medium">Account Number</span>
                            <span className="text-foreground font-bold text-base">{qrData.bankAccount || '—'}</span>
                        </div>
                        <div className="flex justify-between items-center border-b border-border/50 pb-3">
                            <span className="text-muted-foreground font-medium">Account Holder</span>
                            <span className="text-foreground font-bold">{qrData.userBankName || '—'}</span>
                        </div>
                        <div className="flex justify-between items-center border-b border-border/50 pb-3">
                            <span className="text-muted-foreground font-medium">Content</span>
                            <span className="text-foreground font-bold">{qrData.content || '—'}</span>
                        </div>
                        {qrData.amount != null && (
                            <div className="flex justify-between items-center pt-1">
                                <span className="text-muted-foreground font-medium">Amount</span>
                                <span className="text-foreground font-bold text-base">
                                    {qrData.amount.toLocaleString('vi-VN')} ₫
                                </span>
                            </div>
                        )}
                    </div>
                )}

                {/* ── Placeholder bank info while loading ── */}
                {(qrLoading || (!qrData && !qrError)) && (
                    <div className="bg-muted/30 border border-border rounded-2xl p-5 mb-6 text-left space-y-3.5 text-sm z-10 relative animate-pulse">
                        <div className="h-4 bg-muted rounded w-3/4 mx-auto" />
                        <div className="h-4 bg-muted rounded w-1/2 mx-auto" />
                        <div className="h-4 bg-muted rounded w-2/3 mx-auto" />
                    </div>
                )}

                {/* ── Status message above the confirm button ── */}
                <div className="mb-3 min-h-[2.5rem] flex items-center justify-center">
                    {isSuccess ? (
                        /* Green success banner */
                        <div className="w-full flex items-center justify-center gap-2 px-4 py-2.5
                                        bg-green-500/10 border border-green-500/30 rounded-xl
                                        animate-in fade-in zoom-in-95 duration-300">
                            <CheckCircle2 size={18} className="text-green-600 shrink-0" />
                            <span className="text-sm font-bold text-green-600">Payment Received Successfully!</span>
                        </div>
                    ) : confirmState === 'error' && confirmError ? (
                        /* Error banner */
                        <div className="w-full flex items-start gap-2 px-4 py-2.5
                                        bg-destructive/10 border border-destructive/20 rounded-xl
                                        animate-in fade-in duration-200">
                            <AlertCircle size={17} className="text-destructive shrink-0 mt-0.5" />
                            <p className="text-sm text-destructive font-medium text-left">{confirmError}</p>
                        </div>
                    ) : (
                        /* Default awaiting message */
                        <p className="text-sm text-muted-foreground font-medium">
                            Awaiting confirmation…
                        </p>
                    )}
                </div>

                <div className="space-y-4 relative z-10">
                    {/* Confirm / Paid button */}
                    <button
                        id="btn-confirm-payment"
                        onClick={handleConfirmPayment}
                        disabled={isSuccess || isLoading || qrLoading || !!qrError || timeLeft === 0}
                        className={`w-full py-4 rounded-xl font-bold text-base transition-all flex items-center justify-center gap-2 shadow-md ${
                            isSuccess
                                ? 'bg-green-500/20 text-green-700 border-2 border-green-500/40 cursor-default'
                                : isLoading
                                ? 'bg-primary/50 text-primary-foreground cursor-wait'
                                : qrLoading || !!qrError || timeLeft === 0
                                ? 'bg-muted text-muted-foreground cursor-not-allowed'
                                : 'bg-primary text-primary-foreground hover:bg-accent hover:text-accent-foreground hover:shadow-primary/30 hover:-translate-y-0.5'
                        }`}
                    >
                        {isSuccess ? (
                            <><CheckCircle2 size={20} className="animate-in zoom-in" /> Paid</>
                        ) : isLoading ? (
                            <><Loader2 size={20} className="animate-spin" /> Confirming…</>
                        ) : (
                            'I have successfully paid'
                        )}
                    </button>

                    {/* Switch to PayPal */}
                    <button
                        id="btn-switch-paypal"
                        onClick={() => navigate('/checkout/payment/paypal')}
                        disabled={isLoading || isSuccess}
                        className="w-full py-3.5 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2 shadow-sm disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <CreditCard size={18} />
                        Switch to PayPal Payment
                    </button>

                    {/* Cancel */}
                    {!confirmCancel ? (
                        <button
                            id="btn-cancel-order"
                            onClick={() => setConfirmCancel(true)}
                            disabled={isLoading || isSuccess}
                            className="w-full py-3.5 rounded-xl border border-destructive/30 text-destructive font-semibold hover:bg-destructive/10 transition-colors flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
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