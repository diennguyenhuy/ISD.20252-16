import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router';
import { XCircle, RefreshCw, Smartphone } from 'lucide-react';
import PayByCreditCardService from '../../api/payByCreditCardService';

/**
 * PayPal cancel handler (route: /checkout/payment/paypal/cancel).
 *
 * PayPal redirects here (in the new tab) when the customer clicks
 * "Cancel and return". We notify the backend (best-effort, the order is NOT
 * charged and the draft is kept) and broadcast so the opener tab can react.
 */

const PAYPAL_CHANNEL = 'aims-paypal';

export default function PayPalCancel() {
    const navigate = useNavigate();
    const ranRef = useRef(false);

    useEffect(() => {
        if (ranRef.current) return;
        ranRef.current = true;

        const channel =
            typeof BroadcastChannel !== 'undefined' ? new BroadcastChannel(PAYPAL_CHANNEL) : null;
        channel?.postMessage({ type: 'paypal-cancel' });

        // Best-effort: let the backend log the cancellation. Draft is preserved.
        PayByCreditCardService.cancelPayPalPayment().catch(() => { /* ignore */ });

        channel?.close();
    }, []);

    return (
        <div className="max-w-md mx-auto px-4 py-32 flex flex-col items-center justify-center text-center animate-in fade-in">
            <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-muted border border-border mb-4">
                <XCircle size={44} className="text-muted-foreground" />
            </div>
            <p className="text-foreground font-bold text-lg mb-2">Payment cancelled</p>
            <p className="text-muted-foreground text-sm mb-8">
                You haven't been charged. You can retry with PayPal or pay by QR code instead.
            </p>
            <div className="flex flex-col sm:flex-row gap-3 w-full max-w-sm">
                <button
                    onClick={() => navigate('/checkout/payment/paypal')}
                    className="flex-1 py-3.5 rounded-xl bg-primary text-primary-foreground font-bold hover:bg-accent hover:text-accent-foreground transition-all shadow-lg flex items-center justify-center gap-2"
                >
                    <RefreshCw size={18} />
                    Retry PayPal
                </button>
                <button
                    onClick={() => navigate('/checkout/payment/qr')}
                    className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2"
                >
                    <Smartphone size={18} />
                    Pay by QR
                </button>
            </div>
        </div>
    );
}
