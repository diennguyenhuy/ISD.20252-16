import type { Invoice } from '../../../models/order.interface';
import { formatVND } from '../../../data/formatter';

interface InvoiceSummaryProps {
    invoice: Invoice;
}

export function InvoiceSummary({ invoice }: InvoiceSummaryProps) {
    const vatAmount = invoice.totalPriceWithVAT - invoice.totalPriceWithoutVAT;

    return (
        <div className="border-t border-border p-6 bg-muted/10">
            <div className="max-w-xs ml-auto space-y-2.5 text-sm">
                <div className="flex justify-between items-center text-muted-foreground font-medium">
                    <span>Subtotal (excl. VAT)</span>
                    <span className="text-foreground">{formatVND(invoice.totalPriceWithoutVAT)}</span>
                </div>
                <div className="flex justify-between items-center text-muted-foreground font-medium">
                    <span>VAT (10%)</span>
                    <span className="text-foreground">+{formatVND(vatAmount)}</span>
                </div>
                <div className="flex justify-between items-center text-muted-foreground font-medium">
                    <span>Delivery fee</span>
                    <span className="text-foreground">+{formatVND(invoice.deliveryFee)}</span>
                </div>
                <div className="border-t-2 border-border pt-3 mt-1 flex justify-between items-baseline">
                    <span className="font-bold text-foreground text-base">Total payment</span>
                    <span className="text-2xl text-primary font-extrabold tracking-tight">
                        {formatVND(invoice.totalAmount)}
                    </span>
                </div>
            </div>
        </div>
    );
}
