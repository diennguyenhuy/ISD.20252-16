import { useEffect, useRef, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import { CreditCard, ExternalLink, Loader2, AlertCircle, ArrowLeft, ShieldCheck } from 'lucide-react';
import PayByCreditCardService from '../../api/PayByCreditCardService';

/**
 * PayPal payment landing screen (route: /checkout/payment/paypal).
 *
 * Reached from the "Switch to PayPal Payment" button on QRPayment.tsx.
 *
 * Flow:
 *   1. User clicks "Pay with PayPal" → backend creates a PayPal order and returns
 *      the approval URL.
 *   2. We open that URL in a NEW TAB. PayPal collects the payment there and, on
 *      completion, redirects that tab to /checkout/payment/paypal/callback, which
 *      captures the payment and navigates to the success screen.
 *   3. PayPalCallback broadcasts the result on the "aims-paypal" BroadcastChannel
 *      so THIS (opener) tab can follow the customer to the success screen too.
 */

// Shared channel used by the callback/cancel tabs to notify this opener tab.
const PAYPAL_CHANNEL = 'aims-paypal';

type State = 'idle' | 'creating' | 'awaiting' | 'cancelled' | 'error';

export default function PayPalPayment() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const checkoutId = searchParams.get('checkoutId');

    const [state, setState] = useState<State>('idle');
    const [error, setError] = useState<string | null>(null);
    const channelRef = useRef<BroadcastChannel | null>(null);

    // Listen for the result coming from the PayPal callback tab.
    useEffect(() => {
        if (typeof BroadcastChannel === 'undefined') return;

        const channel = new BroadcastChannel(PAYPAL_CHANNEL);
        channelRef.current = channel;

        channel.onmessage = (event) => {
            const data = event.data;
            if (data?.type === 'paypal-success' && data.orderId) {
                navigate(`/checkout/success/${data.orderId}`);
            } else if (data?.type === 'paypal-cancel') {
                setState('cancelled');
            } else if (data?.type === 'paypal-error') {
                setError(data.message || 'PayPal payment failed. Please try again.');
                setState('error');
            }
        };

        return () => channel.close();
    }, [navigate]);

    async function handlePay() {
        setState('creating');
        setError(null);
        try {
            const { approvalUrl } = await PayByCreditCardService.createPayPalPayment();
            // Open PayPal in a new tab; the callback tab finishes the flow.
            window.open(approvalUrl, '_blank', 'noopener,noreferrer');
            setState('awaiting');
        } catch (err: any) {
            const msg =
                err?.response?.data?.message ||
                err?.response?.data ||
                'Could not start PayPal payment. Please try again.';
            setError(typeof msg === 'string' ? msg : 'Could not start PayPal payment.');
            setState('error');
        }
    }

    const isBusy = state === 'creating';

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
                <span className="text-primary px-3 py-1.5 bg-primary/10 rounded-md shadow-sm">PayPal Payment</span>
            </div>

            <div className="bg-card rounded-3xl border border-border shadow-2xl p-6 sm:p-8 text-center relative overflow-hidden">
                <div className="absolute top-0 right-0 w-64 h-64 bg-primary/5 rounded-full blur-3xl -mr-32 -mt-32 pointer-events-none" />

                <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-primary/10 border border-primary/20 mb-5 relative z-10">
                    <CreditCard size={30} className="text-primary" />
                </div>

                <h1 className="text-2xl sm:text-3xl text-foreground font-bold tracking-tight mb-2 relative z-10">
                    Pay with PayPal
                </h1>
                <p className="text-muted-foreground font-medium mb-6 relative z-10">
                    You'll be taken to PayPal in a new tab to complete your payment securely.
                </p>

                {/* Status banners */}
                <div className="mb-5 min-h-[2.5rem] flex items-center justify-center relative z-10">
                    {state === 'awaiting' ? (
                        <div className="w-full flex items-center justify-center gap-2 px-4 py-2.5 bg-primary/5 border border-primary/20 rounded-xl animate-in fade-in">
                            <Loader2 size={18} className="text-primary shrink-0 animate-spin" />
                            <span className="text-sm font-semibold text-foreground">
                                Waiting for you to finish on PayPal…
                            </span>
                        </div>
                    ) : state === 'cancelled' ? (
                        <div className="w-full flex items-start gap-2 px-4 py-2.5 bg-muted border border-border rounded-xl text-left">
                            <AlertCircle size={17} className="text-muted-foreground shrink-0 mt-0.5" />
                            <p className="text-sm text-muted-foreground font-medium">
                                Payment was cancelled. You can try again or go back to QR payment.
                            </p>
                        </div>
                    ) : state === 'error' && error ? (
                        <div className="w-full flex items-start gap-2 px-4 py-2.5 bg-destructive/10 border border-destructive/20 rounded-xl animate-in fade-in text-left">
                            <AlertCircle size={17} className="text-destructive shrink-0 mt-0.5" />
                            <p className="text-sm text-destructive font-medium">{error}</p>
                        </div>
                    ) : (
                        <p className="text-sm text-muted-foreground font-medium flex items-center gap-1.5">
                            <ShieldCheck size={16} className="text-primary" />
                            Secured by PayPal Sandbox
                        </p>
                    )}
                </div>

                <div className="space-y-4 relative z-10">
                    <button
                        id="btn-pay-paypal"
                        onClick={handlePay}
                        disabled={isBusy}
                        className={`w-full py-4 rounded-xl font-bold text-base transition-all flex items-center justify-center gap-2 shadow-md ${
                            isBusy
                                ? 'bg-primary/50 text-primary-foreground cursor-wait'
                                : 'bg-primary text-primary-foreground hover:bg-accent hover:text-accent-foreground hover:shadow-primary/30 hover:-translate-y-0.5'
                        }`}
                    >
                        {isBusy ? (
                            <><Loader2 size={20} className="animate-spin" /> Connecting to PayPal…</>
                        ) : state === 'awaiting' || state === 'cancelled' || state === 'error' ? (
                            <><ExternalLink size={20} /> Reopen PayPal</>
                        ) : (
                            <><ExternalLink size={20} /> Pay with PayPal</>
                        )}
                    </button>

                    <button
                        id="btn-back-qr"
                        onClick={() => navigate(`/checkout/payment/qr?checkoutId=${checkoutId}`)}
                        disabled={isBusy}
                        className="w-full py-3.5 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2 shadow-sm disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <ArrowLeft size={18} />
                        Back to QR Payment
                    </button>
                </div>
            </div>
        </div>
    );
}
