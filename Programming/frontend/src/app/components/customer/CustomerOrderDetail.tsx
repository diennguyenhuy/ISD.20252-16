import { useParams, useNavigate, useLocation } from 'react-router';
import { useState, useEffect } from 'react';
import { ArrowLeft, AlertCircle, Package, Loader2, X } from 'lucide-react';

import OrderService from '../../api/OrderService';
import { formatDateTime } from '../../data/formatter';
import type { Order } from '../../models/order.interface';

import {
    OrderStatusBadge,
    OrderItemList,
    ShippingInfoCard,
    TransactionCard,
} from '../shared/order';

export default function CustomerOrderDetail() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const location = useLocation();
    const passedOrder = location.state?.order as Order | undefined;

    const [order, setOrder] = useState<Order | null | undefined>(passedOrder);
    const [loading, setLoading] = useState(!passedOrder);
    const [confirmCancel, setConfirmCancel] = useState(false);
    const [isCanceling, setIsCanceling] = useState(false);

    // ── Data fetching ────────────────────────────────────────────────────────
    useEffect(() => {
        // Skip fetch if we already have the right order from navigation state
        if (!id || (passedOrder && passedOrder.id === id)) return;

        setLoading(true);
        OrderService.getOrder(id)
            .then(data => {
                setOrder(data);
                setLoading(false);
            })
            .catch(err => {
                console.error('Failed to fetch order', err);
                setOrder(null);
                setLoading(false);
            });
    }, [id]);

    // ── Cancel handler ───────────────────────────────────────────────────────
    const handleCancelOrder = async () => {
        if (!order) return;
        setIsCanceling(true);
        try {
            await OrderService.cancelOrder(order.id);
            // Optimistic update — avoids a round-trip fetch for a status change
            setOrder({ ...order, status: 'CANCELLED' });
            setConfirmCancel(false);
        } catch {
            alert('Could not cancel the order. It might have already been processed.');
        } finally {
            setIsCanceling(false);
        }
    };

    // Back navigation: go to previous list page if we came from there,
    // otherwise fall back to home since there's no authenticated history.
    const handleGoBack = () => (passedOrder ? navigate(-1) : navigate('/'));

    // ── Loading state ─────────────────────────────────────────────────────────
    if (loading) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4" />
                <p className="text-muted-foreground">Loading order details...</p>
            </div>
        );
    }

    // ── Not found state ───────────────────────────────────────────────────────
    if (!order) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <Package size={64} className="mx-auto mb-6 text-muted-foreground opacity-30" />
                <p className="text-xl text-foreground font-bold mb-2">Order not found</p>
                <p className="text-muted-foreground mb-6">
                    This order does not exist or you do not have permission to access it.
                </p>
                <button
                    onClick={() => navigate('/')}
                    className="text-primary font-semibold hover:text-accent-foreground transition-colors underline underline-offset-4"
                >
                    Back to Home
                </button>
            </div>
        );
    }

    return (
        <div className="max-w-3xl mx-auto px-4 py-8 animate-in fade-in slide-in-from-bottom-4 duration-500">

            {/* ── Page header ── */}
            <div className="flex flex-wrap items-center gap-4 mb-8">
                <button
                    onClick={handleGoBack}
                    className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0"
                >
                    <ArrowLeft size={20} />
                </button>
                <div>
                    <h1 className="text-2xl text-foreground font-bold tracking-tight">
                        Order #{order.id}
                    </h1>
                    <p className="text-sm text-muted-foreground mt-1 font-medium">
                        {formatDateTime(order.createdAt)}
                    </p>
                </div>
                {/* Status badge — display delegated entirely */}
                <OrderStatusBadge status={order.status} />
            </div>

            {/* ── REJECTED warning banner (customer-facing copy) ── */}
            {order.status === 'REJECTED' && (
                <div className="bg-destructive/10 border border-destructive/20 rounded-2xl p-4 mb-6 flex items-start gap-3 shadow-sm">
                    <AlertCircle size={20} className="text-destructive shrink-0 mt-0.5" />
                    <div>
                        <p className="text-sm text-destructive font-bold">
                            This order has been rejected by the Manager.
                        </p>
                        <p className="text-xs text-destructive/80 mt-1">
                            If you paid via PayPal, the refund will be processed automatically.
                            If you paid via VietQR, please wait for manual refund processing.
                        </p>
                    </div>
                </div>
            )}

            {/* ── Shared display cards ── */}
            <OrderItemList items={order.items} invoice={order.invoice} />

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                <ShippingInfoCard delivery={order.deliveryInformation} />
                <TransactionCard
                    transaction={order.paymentTransaction}
                    title="Transaction Receipt"
                />
            </div>

            {/* ── Action bar ── */}
            <div className="flex flex-col sm:flex-row gap-4 pt-4 border-t border-border">
                <button
                    onClick={handleGoBack}
                    className="flex-1 py-3.5 px-6 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2 shadow-sm"
                >
                    <ArrowLeft size={18} /> Back
                </button>

                {/* Cancel — only available before the order is approved (PENDING) */}
                {order.status === 'PENDING' && (
                    !confirmCancel ? (
                        <button
                            onClick={() => setConfirmCancel(true)}
                            className="flex-1 py-3.5 px-6 rounded-xl border border-destructive/30 text-destructive font-semibold hover:bg-destructive/10 transition-colors flex items-center justify-center gap-2 shadow-sm"
                        >
                            <X size={18} /> Cancel Order
                        </button>
                    ) : (
                        /* Inline confirm — keeps the cancel flow on-page without a modal */
                        <div className="flex-1 bg-destructive/10 border border-destructive/20 rounded-2xl p-4 flex flex-col sm:flex-row items-center gap-3 animate-in fade-in zoom-in-95 duration-200">
                            <span className="text-sm text-destructive font-bold text-center sm:text-left flex-1">
                                Confirm Cancellation?
                            </span>
                            <div className="flex gap-2 w-full sm:w-auto">
                                <button
                                    onClick={() => setConfirmCancel(false)}
                                    disabled={isCanceling}
                                    className="flex-1 sm:flex-none px-4 py-2 rounded-lg border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors disabled:opacity-50"
                                >
                                    No
                                </button>
                                <button
                                    onClick={handleCancelOrder}
                                    disabled={isCanceling}
                                    className="flex-1 sm:flex-none flex items-center justify-center gap-2 px-4 py-2 rounded-lg bg-destructive text-destructive-foreground font-bold hover:opacity-90 transition-opacity shadow-sm disabled:opacity-70"
                                >
                                    {isCanceling && <Loader2 size={16} className="animate-spin" />}
                                    Yes, Cancel
                                </button>
                            </div>
                        </div>
                    )
                )}
            </div>
        </div>
    );
}
