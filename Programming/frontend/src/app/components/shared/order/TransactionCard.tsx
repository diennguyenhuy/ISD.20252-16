import { Calendar, CreditCard, FileText, Hash } from 'lucide-react';
import type { PaymentTransaction } from '../../../models/order.interface';
import { formatDateTime } from '../../../data/formatter';

interface TransactionCardProps {
    transaction: PaymentTransaction;
    title?: string;
}

export function TransactionCard({transaction, title = 'Transaction Receipt'}: TransactionCardProps) {
    const isVietQR = transaction.transactionMethod === 'VIETQR';
    const methodLabel = isVietQR ? '📱 VietQR' : '💳 PayPal';

    return (
        <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden flex flex-col">

            {/* Header */}
            <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                <CreditCard size={18} className="text-primary" />
                <span className="text-base font-bold text-foreground">{title}</span>
            </div>

            {/* Fields */}
            <div className="p-6 space-y-4 flex-1">

                {/* Transaction ID */}
                <TransactionRow icon={Hash} label="Transaction ID">
                    <span className="font-mono text-sm bg-muted px-2 py-0.5 rounded border border-border">
                        {transaction.id}
                    </span>
                </TransactionRow>

                {/* Payment method */}
                <TransactionRow icon={CreditCard} label="Payment Method">
                    <span className="text-sm text-foreground font-bold">{methodLabel}</span>
                </TransactionRow>

                {/* Payment timestamp */}
                <TransactionRow icon={Calendar} label="Payment Date">
                    <span className="text-sm text-foreground font-semibold">
                        {formatDateTime(transaction.transactionTimestamp)}
                    </span>
                </TransactionRow>

                {/* Transfer note */}
                <TransactionRow icon={FileText} label="Transfer Note">
                    <span className="text-sm text-foreground font-bold text-right pl-4 break-words">
                        {transaction.transactionContent}
                    </span>
                </TransactionRow>
            </div>
        </div>
    );
}

function TransactionRow({icon: Icon, label, children}: {
    icon: React.ElementType;
    label: string;
    children: React.ReactNode;
}) {
    return (
        <div className="flex items-center justify-between border-b border-border/50 pb-3 last:border-0 last:pb-0">
            <div className="flex items-center gap-2 text-muted-foreground shrink-0">
                <Icon size={16} />
                <span className="text-sm font-medium">{label}</span>
            </div>
            <div className="text-right">{children}</div>
        </div>
    );
}
