import { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router';
import { ArrowLeft, Truck, ShoppingBag, User, MapPin, FileText, Loader2, AlertCircle } from 'lucide-react';

import { useCart } from '../../context/CartContext';
import OrderService from '../../api/OrderService';
import { formatVND } from '../../data/formatter';

import type {DeliveryInformation, Invoice as InvoiceType, OrderDraft} from '../../models/order.interface';
import type { ProductTypeName } from '../../models/product.interface';

const TYPE_LABELS: Record<ProductTypeName, string> = {
    Book: 'Book', CD: 'CD', DVD: 'DVD', Newspaper: 'Newspaper'
};

export default function Invoice() {
    const navigate = useNavigate();
    const { checkoutId } = useParams<{ checkoutId: string }>();
    const location = useLocation();
    const { cart } = useCart();

    const orderDraft = location.state?.orderDraft as OrderDraft | undefined;

    // Local State for Invoice API
    const [invoice, setInvoice] = useState<InvoiceType | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (orderDraft) {
            if (!orderDraft.invoice) {
                navigate(`/checkout/${checkoutId}/delivery`);
            }
            setInvoice(orderDraft.invoice!);
            setLoading(false);
            return;
        }

        OrderService.getInvoice()
            .then(data => {
                setInvoice(data.invoice!);
                setLoading(false);
            })
            .catch(err => {
                console.error("Failed to fetch invoice:", err);
                setError(`Could not generate invoice. ${err}`);
                setLoading(false);
            });
    }, [orderDraft, navigate]);

    if (loading || !cart) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4" />
                <p className="text-muted-foreground">Calculating taxes and delivery fees...</p>
            </div>
        );
    }

    if (error || !invoice || !orderDraft?.deliveryInformation) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in">
                <AlertCircle className="w-12 h-12 text-destructive mx-auto mb-4" />
                <p className="text-lg font-medium mb-4">{error || "Missing invoice data."}</p>
                <button onClick={() => navigate(`/checkout/${checkoutId}/delivery`)} className="text-primary hover:underline">
                    Return to Delivery Form
                </button>
            </div>
        );
    }

    const deliveryLabel = orderDraft.deliveryInformation.deliveryMethod === 'rush' ? 'Rush Delivery (1-2 days)' : 'Standard Delivery (3-5 days)';

    return (
        <div className="max-w-3xl mx-auto px-4 py-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
            {/* Progress */}
            <div
                className="flex items-center gap-2 mb-8 text-sm font-medium overflow-x-auto pb-2 whitespace-nowrap custom-scrollbar">
                <span className="text-muted-foreground">Cart</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-muted-foreground">Delivery Information</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-primary px-3 py-1.5 bg-primary/10 rounded-md shadow-sm">Invoice</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-muted-foreground">Payment</span>
            </div>

            <div className="bg-card rounded-3xl border border-border shadow-xl overflow-hidden mb-8">
                {/* Cinematic Header */}
                <div
                    className="bg-gradient-to-r from-primary/10 to-transparent border-b border-border px-6 py-6 sm:px-8 sm:py-8 relative overflow-hidden">
                    <div
                        className="absolute top-0 right-0 w-48 h-48 bg-primary/10 rounded-full blur-3xl -mr-20 -mt-20"></div>
                    <div className="flex items-center gap-3 relative z-10">
                        <div className="p-3 bg-primary/20 rounded-xl text-primary backdrop-blur-sm">
                            <FileText size={24} />
                        </div>
                        <div>
                            <h1 className="text-2xl text-foreground font-bold tracking-tight">INVOICE
                                #{invoice.invoiceNumber}</h1>
                            <p className="text-muted-foreground text-sm mt-1 font-medium">Please review the information
                                carefully before proceeding with the payment</p>
                        </div>
                    </div>
                </div>

                {/* Delivery Info */}
                <div className="px-6 py-6 sm:px-8 border-b border-border bg-muted/10">
                    <h2 className="text-foreground font-bold text-base mb-4 flex items-center gap-2">
                        <User size={18} className="text-primary" />
                        Delivery Information
                    </h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-5 text-sm">
                        <div className="bg-card p-4 rounded-xl border border-border shadow-sm">
                            <p className="text-muted-foreground text-xs font-medium mb-1">Recipient</p>
                            <p className="text-foreground font-bold">{orderDraft.deliveryInformation.customerName}</p>
                        </div>
                        <div className="bg-card p-4 rounded-xl border border-border shadow-sm">
                            <p className="text-muted-foreground text-xs font-medium mb-1">Phone Number</p>
                            {/* FIXED: Uses phoneNumber matching our strictly typed interface */}
                            <p className="text-foreground font-bold">{orderDraft.deliveryInformation.phoneNumber}</p>
                        </div>
                        <div className="bg-card p-4 rounded-xl border border-border shadow-sm sm:col-span-2">
                            <p className="text-muted-foreground text-xs font-medium mb-1">Delivery Address</p>
                            <p className="text-foreground font-semibold flex items-start gap-1.5">
                                <MapPin size={16} className="text-primary shrink-0 mt-0.5" />
                                {orderDraft.deliveryInformation.address}, {orderDraft.deliveryInformation.commune}, {orderDraft.deliveryInformation.province}
                            </p>
                        </div>
                        <div
                            className="bg-primary/5 p-4 rounded-xl border border-primary/20 shadow-sm sm:col-span-2 flex items-center gap-3">
                            <div className="bg-primary/20 p-2 rounded-lg text-primary">
                                <Truck size={18} />
                            </div>
                            <div>
                                <p className="text-muted-foreground text-xs font-medium mb-0.5">Delivery Method</p>
                                <p className="text-primary font-bold">{deliveryLabel}</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Product List */}
                <div className="px-6 py-6 sm:px-8">
                    <h2 className="text-foreground font-bold text-base mb-4 flex items-center gap-2">
                        <ShoppingBag size={18} className="text-primary" />
                        Product List
                    </h2>
                    <div className="overflow-x-auto">
                        <table className="w-full text-sm">
                            <thead>
                                <tr className="text-muted-foreground text-xs border-b border-border">
                                    <th className="text-left pb-3 font-semibold uppercase tracking-wider">Product</th>
                                    <th className="text-center pb-3 font-semibold uppercase tracking-wider w-16">Quantity</th>
                                    <th className="text-right pb-3 font-semibold uppercase tracking-wider w-28">Unit Price
                                    </th>
                                    <th className="text-right pb-3 font-semibold uppercase tracking-wider w-32">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {cart.items.map(item => (
                                    <tr key={item.product.id}
                                        className="border-b border-border/50 hover:bg-muted/30 transition-colors">
                                        <td className="py-4">
                                            <div className="flex items-center gap-4">
                                                <div
                                                    className="w-12 h-12 rounded-xl overflow-hidden bg-muted shrink-0 border border-border">
                                                    {/* FIXED: Uses imageURL */}
                                                    <img
                                                        src={item.product.imageURL}
                                                        alt={item.product.title}
                                                        className="w-full h-full object-cover"
                                                    />
                                                </div>
                                                <div className="min-w-0">
                                                    <p className="text-foreground line-clamp-2 font-bold leading-tight mb-1">{item.product.title}</p>
                                                    <span
                                                        className="text-xs font-semibold px-2 py-0.5 rounded-md bg-muted text-muted-foreground capitalize">
                                                        {TYPE_LABELS[item.product.productType]}
                                                    </span>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="text-center py-4 text-foreground font-semibold">{item.quantity}</td>
                                        <td className="text-right py-4 text-muted-foreground font-medium">{formatVND(item.product.currentPrice)}</td>
                                        <td className="text-right py-4 text-foreground font-bold">{formatVND(item.itemTotalPrice)}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="px-6 py-6 sm:px-8 bg-muted/20 border-t border-border">
                    <div className="max-w-sm ml-auto space-y-3">
                        <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground font-medium">Subtotal (excluding VAT)</span>
                            <span
                                className="text-foreground font-semibold">{formatVND(invoice.totalPriceWithoutVAT)}</span>
                        </div>
                        <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground font-medium">VAT (10%)</span>
                            <span
                                className="text-foreground font-semibold">+{formatVND(invoice.totalPriceWithVAT - invoice.totalPriceWithoutVAT)}</span>
                        </div>
                        <div className="flex justify-between text-sm pt-2 border-t border-border/50">
                            <span className="text-muted-foreground font-medium">Subtotal (including VAT)</span>
                            <span className="text-foreground font-bold">{formatVND(invoice.totalPriceWithVAT)}</span>
                        </div>
                        <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground font-medium">Shipping Fee ({deliveryLabel})</span>
                            <span className="text-foreground font-semibold">+{formatVND(invoice.deliveryFee)}</span>
                        </div>

                        {/* Grand Total */}
                        <div className="border-t-2 border-border mt-4 pt-4 flex justify-between items-center">
                            <span className="font-bold text-foreground text-base">Grand Total</span>
                            <span
                                className="font-extrabold text-primary text-2xl tracking-tight">{formatVND(invoice.totalAmount)}</span>
                        </div>
                    </div>
                </div>
            </div>

            {/* Actions */}
            <div className="flex flex-col sm:flex-row gap-4">
                <button
                    onClick={() => navigate(`/checkout/${checkoutId}/delivery`, {
                        state: {
                            prefilledDeliveryInfo: orderDraft.deliveryInformation
                        }
                    })}
                    className="flex-1 sm:flex-none flex items-center justify-center gap-2 py-3.5 px-6 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors shadow-sm"
                >
                    <ArrowLeft size={18} />
                    Edit Delivery
                </button>
                <button
                    onClick={() => navigate('/cart')}
                    className="flex-1 sm:flex-none flex items-center justify-center gap-2 py-3.5 px-6 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors shadow-sm"
                >
                    <ShoppingBag size={18} />
                    Edit Cart
                </button>
                <button
                    onClick={() => navigate(`/checkout/${checkoutId}/payment/qr`)}
                    className="flex-1 py-3.5 px-6 rounded-xl bg-primary text-primary-foreground font-bold text-base hover:bg-accent hover:text-accent-foreground transition-all shadow-lg hover:shadow-primary/30 hover:-translate-y-0.5 flex items-center justify-center gap-2"
                >
                    Proceed to Payment →
                </button>
            </div>
        </div>
    );
}