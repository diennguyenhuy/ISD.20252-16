import { Package } from 'lucide-react';
import type { OrderItem, Invoice } from '../../../models/order.interface';
import { formatVND } from '../../../data/formatter';
import { InvoiceSummary } from './InvoiceSummary';

interface OrderItemListProps {
    items: OrderItem[];
    invoice: Invoice;
}

export function OrderItemList({ items, invoice }: OrderItemListProps) {
    return (
        <div className="bg-card rounded-3xl border border-border shadow-lg overflow-hidden mb-6">

            {/* Header */}
            <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                <Package size={18} className="text-primary" />
                <span className="text-base font-bold text-foreground">
                    Products ({items.length})
                </span>
            </div>

            {/* Line items */}
            <div className="p-6 space-y-4">
                {items.map((item, idx) => (
                    <div
                        key={idx}
                        className="flex flex-col sm:flex-row sm:items-center gap-4 pb-4 border-b border-border/50 last:border-0 last:pb-0"
                    >
                        <div className="flex items-center gap-4 flex-1 min-w-0">
                            {/* Intentional fallback — see component JSDoc */}
                            <div className="w-16 h-16 rounded-xl bg-primary/5 flex items-center justify-center shrink-0 border border-primary/10">
                                <Package size={24} className="text-primary/50" />
                            </div>
                            <div className="min-w-0">
                                <p className="text-base text-foreground font-bold line-clamp-2 mb-1">
                                    {item.productName}
                                </p>
                                <div className="flex items-center gap-2">
                                    <span className="text-xs font-semibold px-2 py-0.5 rounded-md bg-muted text-muted-foreground">
                                        {item.unitWeight} kg
                                    </span>
                                    <span className="text-sm text-muted-foreground font-medium">
                                        {formatVND(item.unitPrice)} / unit
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div className="text-left sm:text-right shrink-0 pl-20 sm:pl-0">
                            <p className="text-sm text-muted-foreground font-medium mb-1">
                                Qty: <span className="text-foreground font-bold">{item.quantity}</span>
                            </p>
                            <p className="text-base text-primary font-bold">
                                {formatVND(item.unitPrice * item.quantity)}
                            </p>
                        </div>
                    </div>
                ))}
            </div>

            {/* Invoice summary footer */}
            <InvoiceSummary invoice={invoice} />
        </div>
    );
}
