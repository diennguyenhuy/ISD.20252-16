import {useParams, useNavigate, useLocation} from 'react-router';
import {useState, useEffect} from 'react';
import {
    ArrowLeft, Package, User, MapPin, Phone, CreditCard, Hash, Calendar,
    Truck, CheckCircle, Clock, XCircle, X, FileText, Loader2, AlertCircle
} from 'lucide-react';

import OrderService from '../../api/orderService';
import {formatVND, formatDateTime} from '../../data/mockData';
import type {Order, OrderStatus} from '../../models/order.interface';

// UPDATED: Theme-safe status badges matching the strict OrderStatus type
const STATUS_CONFIG: Record<OrderStatus, { label: string; color: string; icon: any }> = {
    DRAFT: {label: 'Draft', color: 'bg-muted text-muted-foreground border-border', icon: FileText},
    PENDING: {label: 'Pending Processing', color: 'bg-amber-500/10 text-amber-500 border-amber-500/20', icon: Clock},
    APPROVED: {label: 'Approved', color: 'bg-emerald-500/10 text-emerald-500 border-emerald-500/20', icon: CheckCircle},
    REJECTED: {label: 'Rejected', color: 'bg-destructive/10 text-destructive border-destructive/20', icon: XCircle},
    CANCELLED: {label: 'Cancelled', color: 'bg-muted text-muted-foreground border-border', icon: X},
    REFUNDED: {label: 'Refunded', color: 'bg-blue-500/10 text-blue-500 border-blue-500/20', icon: CreditCard},
};

export default function CustomerOrderDetail() {
    const {id} = useParams<{ id: string }>();
    const navigate = useNavigate();
    const location = useLocation();
    const passedOrder = location.state?.order as Order | undefined;

    const [order, setOrder] = useState<Order | null | undefined>(passedOrder);
    const [loading, setLoading] = useState(!passedOrder);
    const [confirmCancel, setConfirmCancel] = useState(false);
    const [isCanceling, setIsCanceling] = useState(false);

    // Fetch Order Details
    useEffect(() => {
        if (!id || (passedOrder && passedOrder.id === id)) return;

        setLoading(true);
        OrderService.getOrder(id)
            .then(data => {
                setOrder(data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Failed to fetch order", err);
                setLoading(false);
            });
    }, [id]);

    const handleCancelOrder = async () => {
        if (!order) return;
        setIsCanceling(true);
        try {
            await OrderService.cancelOrder(order.id);
            // Optimistically update the UI to show it's cancelled
            setOrder({...order, status: 'CANCELLED'});
            setConfirmCancel(false);
        } catch (error) {
            console.error("Failed to cancel order", error);
            alert("Could not cancel the order. It might have already been processed.");
        } finally {
            setIsCanceling(false);
        }
    };

    if (loading) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4"/>
                <p className="text-muted-foreground">Loading order details...</p>
            </div>
        );
    }

    if (!order) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <Package size={64} className="mx-auto mb-6 text-muted-foreground opacity-30"/>
                <p className="text-xl text-foreground font-bold mb-2">Order not found</p>
                <p className="text-muted-foreground mb-6">This order does not exist or you do not have permission to
                    access it.</p>
                <button
                    onClick={() => navigate('/')}
                    className="text-primary font-semibold hover:text-accent-foreground transition-colors underline underline-offset-4"
                >
                    Back to Home
                </button>
            </div>
        );
    }

    const statusCfg = STATUS_CONFIG[order.status];
    const StatusIcon = statusCfg.icon;

    // Destructure for cleaner JSX mapping
    const {deliveryInformation, invoice, paymentTransaction} = order;

    return (
        <div className="max-w-3xl mx-auto px-4 py-8 animate-in fade-in slide-in-from-bottom-4 duration-500">

            {/* Header */}
            <div className="flex flex-wrap items-center gap-4 mb-8">
                <button
                    onClick={() => navigate(-1)}
                    className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0"
                >
                    <ArrowLeft size={20}/>
                </button>
                <div>
                    <h1 className="text-2xl text-foreground font-bold tracking-tight">Details of Order #{order.id}</h1>
                    <p className="text-sm text-muted-foreground mt-1 font-medium">{formatDateTime(order.createdAt)}</p>
                </div>
                <span
                    className={`ml-auto flex items-center gap-1.5 px-4 py-2 rounded-full text-sm font-bold border shadow-sm ${statusCfg.color}`}>
          <StatusIcon size={16}/>
                    {statusCfg.label}
        </span>
            </div>

            {/* Warning if Rejected */}
            {order.status === 'REJECTED' && (
                <div
                    className="bg-destructive/10 border border-destructive/20 rounded-2xl p-4 mb-6 flex items-start gap-3 shadow-sm">
                    <AlertCircle size={20} className="text-destructive shrink-0 mt-0.5"/>
                    <div>
                        <p className="text-sm text-destructive font-bold">This order has been rejected by the
                            Manager.</p>
                        <p className="text-xs text-destructive/80 mt-1">If you paid via PayPal, the refund will be
                            processed automatically. If you paid via VietQR, please wait for manual refund
                            processing.</p>
                    </div>
                </div>
            )}

            {/* Products Card */}
            <div className="bg-card rounded-3xl border border-border shadow-lg overflow-hidden mb-6 relative">
                <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                    <Package size={18} className="text-primary"/>
                    <span className="text-base font-bold text-foreground">Products in Order</span>
                </div>
                <div className="p-6 space-y-4">
                    {order.items.map((item, idx) => (
                        <div key={idx}
                             className="flex items-center gap-4 pb-4 border-b border-border/50 last:border-0 last:pb-0">

                            {/* Fallback Icon Box since OrderItem is a snapshot and doesn't store imageURL */}
                            <div
                                className="w-16 h-16 rounded-xl bg-primary/5 flex items-center justify-center shrink-0 border border-primary/10">
                                <Package size={24} className="text-primary/50"/>
                            </div>

                            <div className="flex-1 min-w-0">
                                <p className="text-base text-foreground font-bold line-clamp-2 mb-1">{item.productName}</p>
                                <span
                                    className="text-xs font-semibold px-2 py-0.5 rounded-md bg-muted text-muted-foreground">
                  Weight: {item.unitWeight} kg
                </span>
                            </div>
                            <div className="text-right shrink-0">
                                <p className="text-sm text-muted-foreground font-medium mb-1">Qty: <span
                                    className="text-foreground font-bold">{item.quantity}</span></p>
                                <p className="text-base text-primary font-bold">{formatVND(item.unitPrice * item.quantity)}</p>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Price Summary mapped to Invoice Interface */}
                <div className="border-t border-border p-6 bg-muted/10">
                    <div className="max-w-xs ml-auto space-y-2.5 text-sm">
                        <div className="flex justify-between items-center text-muted-foreground font-medium">
                            <span>Total (excluding VAT)</span>
                            <span className="text-foreground">{formatVND(invoice.totalPriceWithoutVAT)}</span>
                        </div>
                        <div className="flex justify-between items-center text-muted-foreground font-medium">
                            <span>VAT (10%)</span>
                            <span
                                className="text-foreground">+{formatVND(invoice.totalPriceWithVAT - invoice.totalPriceWithoutVAT)}</span>
                        </div>
                        <div className="flex justify-between items-center text-muted-foreground font-medium">
                            <span>Shipping Fee</span>
                            <span className="text-foreground">+{formatVND(invoice.deliveryFee)}</span>
                        </div>
                        <div className="border-t-2 border-border pt-3 mt-1 flex justify-between items-center">
                            <span className="font-bold text-foreground text-base">Total Amount</span>
                            <span
                                className="text-2xl text-primary font-extrabold tracking-tight">{formatVND(invoice.totalAmount)}</span>
                        </div>
                    </div>
                </div>
            </div>

            {/* Shipping & Transaction Bento Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                {/* Shipping Info mapped to DeliveryInformation */}
                <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden flex flex-col">
                    <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                        <Truck size={18} className="text-primary"/>
                        <span className="text-base font-bold text-foreground">Shipping Information</span>
                    </div>
                    <div className="p-6 grid grid-cols-1 gap-4 flex-1">
                        <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><User size={16}
                                                                                                   className="text-primary"/>
                            </div>
                            <div>
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Recipient</p>
                                <p className="text-sm text-foreground font-bold">{deliveryInformation.customerName}</p>
                            </div>
                        </div>
                        <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><Phone size={16}
                                                                                                    className="text-primary"/>
                            </div>
                            <div>
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Phone Number</p>
                                <p className="text-sm text-foreground font-bold">{deliveryInformation.phoneNumber}</p>
                            </div>
                        </div>
                        <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><MapPin size={16}
                                                                                                     className="text-primary"/>
                            </div>
                            <div>
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Address</p>
                                <p className="text-sm text-foreground font-semibold leading-relaxed">
                                    {deliveryInformation.address}, {deliveryInformation.commune}, {deliveryInformation.province}
                                </p>
                            </div>
                        </div>
                        <div className="flex items-start gap-3 bg-primary/5 p-3.5 rounded-2xl border border-primary/20">
                            <div className="p-2 bg-primary/20 rounded-lg shadow-sm shrink-0"><Truck size={16}
                                                                                                    className="text-primary"/>
                            </div>
                            <div>
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Delivery Method</p>
                                <p className="text-sm text-primary font-bold">
                                    {deliveryInformation.deliveryMethod === 'rush' ? 'Rush Delivery (1-2 days)' : 'Standard Delivery (3-5 days)'}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Transaction Info mapped to PaymentTransaction */}
                <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden flex flex-col">
                    <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                        <CreditCard size={18} className="text-primary"/>
                        <span className="text-base font-bold text-foreground">Transaction Receipt</span>
                    </div>
                    <div className="p-6 space-y-4 flex-1">
                        <div className="flex items-center justify-between border-b border-border/50 pb-3">
                            <div className="flex items-center gap-2 text-muted-foreground">
                                <Hash size={16}/>
                                <span className="text-sm font-medium">Transaction ID</span>
                            </div>
                            <p className="text-sm text-foreground font-mono font-bold bg-muted px-2 py-0.5 rounded border border-border">{paymentTransaction.id}</p>
                        </div>

                        <div className="flex items-center justify-between border-b border-border/50 pb-3">
                            <div className="flex items-center gap-2 text-muted-foreground">
                                <CreditCard size={16}/>
                                <span className="text-sm font-medium">Payment Method</span>
                            </div>
                            <p className="text-sm text-foreground font-bold flex items-center gap-1.5">
                                {paymentTransaction.transactionMethod === 'VIETQR' ? '📱 QR Code' : '💳 PayPal'}
                            </p>
                        </div>

                        <div className="flex items-center justify-between border-b border-border/50 pb-3">
                            <div className="flex items-center gap-2 text-muted-foreground">
                                <Calendar size={16}/>
                                <span className="text-sm font-medium">Payment Date</span>
                            </div>
                            <p className="text-sm text-foreground font-semibold">{formatDateTime(paymentTransaction.transactionTimestamp)}</p>
                        </div>

                        <div className="flex items-center justify-between pt-1">
                            <div className="flex items-center gap-2 text-muted-foreground shrink-0">
                                <FileText size={16}/>
                                <span className="text-sm font-medium">Transaction Content</span>
                            </div>
                            <p className="text-sm text-foreground font-bold text-right truncate pl-4">{paymentTransaction.transactionContent}</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Actions */}
            <div className="flex flex-col sm:flex-row gap-4 pt-4 border-t border-border">
                <button
                    onClick={() => navigate(-1)}
                    className="flex-1 py-3.5 px-6 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2 shadow-sm"
                >
                    <ArrowLeft size={18}/>
                    Back
                </button>

                {/* Strictly matches Problem Statement: "before the order is approved" */}
                {order.status === 'PENDING' && (
                    !confirmCancel ? (
                        <button
                            onClick={() => setConfirmCancel(true)}
                            className="flex-1 py-3.5 px-6 rounded-xl border border-destructive/30 text-destructive font-semibold hover:bg-destructive/10 transition-colors flex items-center justify-center gap-2 shadow-sm"
                        >
                            <X size={18}/>
                            Cancel Order
                        </button>
                    ) : (
                        <div
                            className="flex-1 bg-destructive/10 border border-destructive/20 rounded-2xl p-4 flex flex-col sm:flex-row items-center gap-3 animate-in fade-in zoom-in-95 duration-200">
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
                                    {isCanceling && <Loader2 size={16} className="animate-spin"/>}
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