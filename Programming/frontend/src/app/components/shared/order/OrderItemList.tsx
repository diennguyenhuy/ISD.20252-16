import { Package, AlertTriangle } from 'lucide-react';
import { useNavigate } from 'react-router';
import type { OrderItem, Invoice } from '../../../models/order.interface';
import { formatVND } from '../../../data/formatter';
import { InvoiceSummary } from './InvoiceSummary';

interface OrderItemListProps {
    items: OrderItem[];
    invoice: Invoice;
    isManagerScreen: boolean;
}

export function OrderItemList({ items, invoice, isManagerScreen }: OrderItemListProps) {
    const navigate = useNavigate();

    // Check if any product in this order has been deleted from the database
    const hasMissingProducts = items.some(item => !item.productId);

    return (
        <div className="bg-card rounded-3xl border border-border shadow-lg overflow-hidden mb-6">

            {/* Header */}
            <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <Package size={18} className="text-primary" />
                    <span className="text-base font-bold text-foreground">
                        Products ({items.length})
                    </span>
                </div>
                {/* Aggregate Warning Badge */}
                {hasMissingProducts && (
                    <div className="flex items-center gap-1.5 text-xs text-destructive bg-destructive/10 px-3 py-1 rounded-full font-bold border border-destructive/20 animate-in fade-in">
                        <AlertTriangle size={14} />
                        <span>Contains unresolved products</span>
                    </div>
                )}
            </div>

            {/* Line items */}
            <div className="p-4 sm:p-6 space-y-2">
                {items.map((item, idx) => {
                    const isMissing = !item.productId;

                    return (
                        <div
                            key={idx}
                            onClick={() => !isMissing && navigate(`${isManagerScreen ? `/manager/products/${item.productId}` : `/product/${item.productId}`}`)}
                            className={`flex flex-col sm:flex-row sm:items-center gap-4 pb-4 border-b border-border/50 last:border-0 last:pb-0 p-2 -mx-2 rounded-2xl transition-all ${
                                isMissing
                                    ? 'opacity-80 bg-destructive/5 border-destructive/10'
                                    : 'cursor-pointer hover:bg-muted/50'
                            }`}
                        >
                            <div className="flex items-center gap-4 flex-1 min-w-0">

                                {/* Image / Fallback Icon */}
                                <div className={`w-16 h-16 rounded-xl overflow-hidden flex items-center justify-center shrink-0 border ${isMissing ? 'bg-destructive/10 border-destructive/20' : 'bg-primary/5 border-primary/10'}`}>
                                    {item.productImage ? (
                                        <img
                                            src={item.productImage}
                                            alt={item.productName}
                                            className={`w-full h-full object-cover ${isMissing ? 'grayscale opacity-50' : ''}`}
                                            onError={(e) => {
                                                // Fallback if image URL is broken
                                                e.currentTarget.style.display = 'none';
                                                e.currentTarget.parentElement?.classList.add('flex');
                                            }}
                                        />
                                    ) : (
                                        <Package size={24} className={isMissing ? "text-destructive/50" : "text-primary/50"} />
                                    )}
                                </div>

                                <div className="min-w-0">
                                    <p className={`text-base font-bold line-clamp-2 mb-1 ${isMissing ? 'text-muted-foreground line-through' : 'text-foreground'}`}>
                                        {item.productName}
                                    </p>
                                    <div className="flex flex-wrap items-center gap-2">
                                        <span className="text-xs font-semibold px-2 py-0.5 rounded-md bg-muted text-muted-foreground">
                                            {item.unitWeight} kg
                                        </span>
                                        <span className="text-sm text-muted-foreground font-medium">
                                            {formatVND(item.unitPrice)} / unit
                                        </span>

                                        {/* Missing Product Warning Label */}
                                        {isMissing && (
                                            <span className="flex items-center gap-1 text-[10px] uppercase tracking-wider text-destructive font-extrabold mt-1 sm:mt-0">
                                                <AlertTriangle size={12} />
                                                This product can no longer be resolved in {isManagerScreen ? 'the' : 'our'} system.
                                                {isManagerScreen ? 'As a result, you cannot approve this order.' : 'You may consider cancelling this order.'}
                                            </span>
                                        )}
                                    </div>
                                </div>
                            </div>

                            <div className="text-left sm:text-right shrink-0 pl-20 sm:pl-0">
                                <p className="text-sm text-muted-foreground font-medium mb-1">
                                    Qty: <span className="text-foreground font-bold">{item.quantity}</span>
                                </p>
                                <p className={`text-base font-bold ${isMissing ? 'text-muted-foreground' : 'text-primary'}`}>
                                    {formatVND(item.unitPrice * item.quantity)}
                                </p>
                            </div>
                        </div>
                    );
                })}
            </div>

            {/* Invoice summary footer */}
            {invoice && <InvoiceSummary invoice={invoice} />}
        </div>
    );
}