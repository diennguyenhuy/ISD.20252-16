import { useEffect, useRef, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router';
import { CheckCircle2, Loader2, AlertCircle } from 'lucide-react';
import PayByCreditCardService from '../../api/payByCreditCardService';

/**
 * PayPal return handler (route: /checkout/payment/paypal/callback).
 *
 * PayPal redirects the customer here (in the new tab) after approval, appending
 * ?token=<paypal-order-id>&PayerID=<...>. We capture the payment with that token;
 * on success the backend finalizes the order and we navigate to the success screen.
 * The result is also broadcast so the original (opener) tab follows along.
 */

const PAYPAL_CHANNEL = 'aims-paypal';

type State = 'capturing' | 'success' | 'error';

export default function PayPalCallback() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');

    const [state, setState] = useState<State>('capturing');
    const [error, setError] = useState<string | null>(null);
    const ranRef = useRef(false); // StrictMode double-invoke guard

    useEffect(() => {
        if (ranRef.current) return;
        ranRef.current = true;

        const channel =
            typeof BroadcastChannel !== 'undefined' ? new BroadcastChannel(PAYPAL_CHANNEL) : null;

        async function capture() {
            if (!token) {
                setError('Missing PayPal token. The payment could not be verified.');
                setState('error');
                channel?.postMessage({ type: 'paypal-error', message: 'Missing PayPal token.' });
                return;
            }
            try {
                const order = await PayByCreditCardService.capturePayPalPayment(token);
                setState('success');
                channel?.postMessage({ type: 'paypal-success', orderId: order.id });
                setTimeout(() => navigate(`/checkout/success/${order.id}`), 1200);
            } catch (err: any) {
                const msg =
                    err?.response?.data?.message ||
                    err?.response?.data ||
                    'We could not verify your PayPal payment. Please try again.';
                const message = typeof msg === 'string' ? msg : 'Payment verification failed.';
                setError(message);
                setState('error');
                channel?.postMessage({ type: 'paypal-error', message });
            } finally {
                channel?.close();
            }
        }

        capture();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div className="max-w-md mx-auto px-4 py-32 flex flex-col items-center justify-center text-center animate-in fade-in">
            {state === 'capturing' && (
                <>
                    <Loader2 className="w-12 h-12 animate-spin text-primary mb-4" />
                    <p className="text-muted-foreground font-medium">Verifying your PayPal payment…</p>
                </>
            )}

            {state === 'success' && (
                <>
                    <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-green-500/10 border border-green-500/30 mb-4">
                        <CheckCircle2 size={44} className="text-green-600 animate-in zoom-in duration-300" />
                    </div>
                    <p className="text-foreground font-bold text-lg">Payment confirmed!</p>
                    <p className="text-muted-foreground text-sm mt-1">Taking you to your receipt…</p>
                </>
            )}

            {state === 'error' && (
                <>
                    <AlertCircle className="w-12 h-12 text-destructive mb-4" />
                    <p className="text-foreground font-bold text-lg mb-2">Payment not completed</p>
                    <p className="text-muted-foreground text-sm mb-6">{error}</p>
                    <button
                        onClick={() => navigate('/checkout/payment/paypal')}
                        className="bg-primary text-primary-foreground px-6 py-3 rounded-xl font-bold hover:bg-accent hover:text-accent-foreground transition-all shadow-lg"
                    >
                        Try Again
                    </button>
                </>
            )}
        </div>
    );
}
