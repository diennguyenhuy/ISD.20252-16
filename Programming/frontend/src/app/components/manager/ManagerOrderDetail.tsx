import { useParams, useNavigate } from 'react-router';
import { useState, useEffect } from 'react';
import {
    ArrowLeft, FileText, Loader2,
    CheckCircle2, XCircle, CreditCard, Info,
} from 'lucide-react';

import OrderManagementService from '../../api/OrderManagementService';
import { formatDateTime } from '../../data/formatter';
import type { Order } from '../../models/order.interface';

import {
    OrderStatusBadge,
    OrderItemList,
    ShippingInfoCard,
    TransactionCard,
} from '../shared/order';

type ConfirmAction = 'APPROVED' | 'REJECTED' | 'REFUND';

function buildConfirmConfig(isVietQR: boolean): Record<ConfirmAction, {
    title: string;
    body: string;
    btnClass: string;
    icon: React.ReactNode;
}> {
    return {
        APPROVED: {
            title: 'Approve Order',
            body: 'Stock will be decremented and the customer will be notified by email.',
            btnClass: 'bg-emerald-600 hover:bg-emerald-700 text-white',
            icon: <CheckCircle2 size={24} className="text-emerald-500" />,
        },
        REJECTED: {
            title: 'Reject Order',
            body: isVietQR
                ? 'The order will be rejected. Because this order was paid via VietQR, you will need to process the refund manually.'
                : 'The order will be rejected and a refund will be initiated automatically via PayPal.',
            btnClass: 'bg-destructive hover:bg-destructive/90 text-destructive-foreground',
            icon: <XCircle size={24} className="text-destructive" />,
        },
        REFUND: {
            title: 'Mark as Refunded',
            body: 'Confirm that you have completed the bank transfer refund to the customer for this VietQR order.',
            btnClass: 'bg-blue-600 hover:bg-blue-700 text-white',
            icon: <CreditCard size={24} className="text-blue-600" />,
        },
    };
}

export default function ManagerOrderDetail() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    const [confirmAction, setConfirmAction] = useState<ConfirmAction | null>(null);
    const [isUpdating, setIsUpdating] = useState(false);

    // ── Data fetching ────────────────────────────────────────────────────────
    useEffect(() => {
        if (!id) return;
        OrderManagementService.getOrder(id)
            .then(setOrder)
            .catch(() => setError(true))
            .finally(() => setLoading(false));
    }, [id]);

    // ── Action handler ───────────────────────────────────────────────────────
    const handleConfirm = async () => {
        if (!confirmAction || !order) return;
        setIsUpdating(true);
        try {
            let updated: Order;
            if (confirmAction === 'APPROVED') {
                updated = await OrderManagementService.approveOrder(order.id);
            } else if (confirmAction === 'REJECTED') {
                updated = await OrderManagementService.rejectOrder(order.id);
            } else {
                // REFUND: backend endpoint TBD — optimistic update as placeholder
                updated = { ...order, status: 'REFUNDED' };
            }
            setOrder(updated);
            setConfirmAction(null);
        } catch {
            alert('Could not update order status. Please try again.');
        } finally {
            setIsUpdating(false);
        }
    };

    // ── Loading state ─────────────────────────────────────────────────────────
    if (loading) {
        return (
            <div className="py-32 flex justify-center">
                <Loader2 className="animate-spin text-primary" size={48} />
            </div>
        );
    }

    // ── Error / not found state ───────────────────────────────────────────────
    if (error || !order) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <FileText size={64} className="mx-auto mb-6 text-muted-foreground opacity-30" />
                <p className="text-xl font-bold mb-2">Order not found</p>
                <p className="text-muted-foreground mb-6">
                    This order does not exist or has been removed.
                </p>
                <button
                    onClick={() => navigate('/manager/orders')}
                    className="text-primary font-semibold hover:underline underline-offset-4"
                >
                    Back to list
                </button>
            </div>
        );
    }

    // ── Derived values ────────────────────────────────────────────────────────
    const isVietQR = order.paymentTransaction?.transactionMethod === 'VIETQR';

    // VietQR orders that are rejected or cancelled need a manual refund —
    // show the CTA only when the refund hasn't already been recorded.
    const showRefundButton =
        isVietQR &&
        (order.status === 'REJECTED' || order.status === 'CANCELLED');

    const confirmConfig = buildConfirmConfig(isVietQR);

    return (
        <div className="max-w-4xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500">

            {/* ── Page header ── */}
            <div className="flex flex-wrap items-center gap-4 mb-8">
                <button
                    onClick={() => navigate('/manager/orders')}
                    className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0"
                >
                    <ArrowLeft size={20} />
                </button>
                <div>
                    <h2 className="text-2xl font-bold tracking-tight">
                        Order #{order.id.slice(0, 8)}…
                    </h2>
                    <p className="text-sm text-muted-foreground mt-1 font-medium">
                        {formatDateTime(order.createdAt)}
                    </p>
                </div>
                {/* Status badge — display delegated entirely */}
                <OrderStatusBadge status={order.status} />
            </div>

            {/* ── Shared display cards ── */}
            <OrderItemList items={order.items} invoice={order.invoice} />

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                <ShippingInfoCard delivery={order.deliveryInformation} />
                <TransactionCard
                    transaction={order.paymentTransaction}
                    title="Transaction Details"
                />
            </div>

            {/* ── VietQR manual refund notice ── */}
            {showRefundButton && (
                <div className="flex items-start gap-3 p-4 rounded-2xl bg-blue-500/10 border border-blue-300/40 mb-6 text-sm text-blue-700">
                    <Info size={16} className="shrink-0 mt-0.5" />
                    <span>
                        This order was paid via <strong>VietQR</strong> and requires a{' '}
                        <strong>manual bank transfer refund</strong>. Once you have completed
                        the transfer, mark it as refunded below.
                    </span>
                </div>
            )}

            {/* ── Action bar ── */}
            <div className="flex flex-col sm:flex-row gap-4 pt-4 border-t border-border">
                <button
                    onClick={() => navigate('/manager/orders')}
                    className="sm:flex-none flex items-center justify-center gap-2 py-3.5 px-6 rounded-xl border border-border bg-card font-semibold hover:bg-muted shadow-sm"
                >
                    <ArrowLeft size={18} /> Back
                </button>

                {order.status === 'PENDING' && (
                    <>
                        <button
                            onClick={() => setConfirmAction('REJECTED')}
                            className="flex-1 py-3.5 px-6 rounded-xl border border-destructive/30 text-destructive bg-card font-semibold hover:bg-destructive/10 flex items-center justify-center gap-2 shadow-sm"
                        >
                            <XCircle size={18} /> Reject Order
                        </button>
                        <button
                            onClick={() => setConfirmAction('APPROVED')}
                            className="flex-1 py-3.5 px-6 rounded-xl bg-emerald-600 text-white font-bold hover:bg-emerald-700 shadow-md flex items-center justify-center gap-2"
                        >
                            <CheckCircle2 size={18} /> Approve Order
                        </button>
                    </>
                )}

                {showRefundButton && (
                    <button
                        onClick={() => setConfirmAction('REFUND')}
                        className="flex-1 py-3.5 px-6 rounded-xl border border-blue-300/40 text-blue-700 bg-card font-semibold hover:bg-blue-500/10 flex items-center justify-center gap-2 shadow-sm"
                    >
                        <CreditCard size={18} /> Mark as Refunded
                    </button>
                )}
            </div>

            {/* ── Confirmation modal ── */}
            {confirmAction && (() => {
                const cfg = confirmConfig[confirmAction];
                const iconBg =
                    confirmAction === 'APPROVED' ? 'bg-emerald-500/10 border-emerald-500/20'
                        : confirmAction === 'REFUND'  ? 'bg-blue-500/10 border-blue-300/40'
                            :                              'bg-destructive/10 border-destructive/20';

                return (
                    <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                        <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border">
                            <div className="flex items-center gap-4 mb-4">
                                <div className={`p-3 rounded-2xl border ${iconBg}`}>
                                    {cfg.icon}
                                </div>
                                <h3 className="font-bold text-lg">{cfg.title}</h3>
                            </div>
                            <p className="text-muted-foreground text-sm mb-8">{cfg.body}</p>
                            <div className="flex gap-3">
                                <button
                                    disabled={isUpdating}
                                    onClick={() => setConfirmAction(null)}
                                    className="flex-1 py-3 rounded-xl border border-border bg-card font-bold disabled:opacity-50"
                                >
                                    Cancel
                                </button>
                                <button
                                    disabled={isUpdating}
                                    onClick={handleConfirm}
                                    className={`flex-1 flex justify-center items-center py-3 rounded-xl font-bold shadow-md disabled:opacity-70 ${cfg.btnClass}`}
                                >
                                    {isUpdating
                                        ? <Loader2 size={20} className="animate-spin" />
                                        : 'Confirm'
                                    }
                                </button>
                            </div>
                        </div>
                    </div>
                );
            })()}
        </div>
    );
}
