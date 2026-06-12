import { CreditCard, CheckCircle, Clock, XCircle, X } from 'lucide-react';
import type { OrderStatus } from '../../../models/order.interface';

export const ORDER_STATUS_CONFIG: Record<OrderStatus, {
    label: string;
    color: string;
    icon: React.ElementType;
}> = {
    PENDING:   { label: 'Pending Processing', color: 'bg-amber-500/10 text-amber-600 border-amber-400/30',   icon: Clock },
    APPROVED:  { label: 'Approved',           color: 'bg-emerald-500/10 text-emerald-600 border-emerald-400/30', icon: CheckCircle },
    REJECTED:  { label: 'Rejected',           color: 'bg-destructive/10 text-destructive border-destructive/20', icon: XCircle },
    CANCELLED: { label: 'Cancelled',          color: 'bg-muted text-muted-foreground border-border',         icon: X },
    REFUNDED:  { label: 'Refunded',           color: 'bg-blue-500/10 text-blue-600 border-blue-400/30',      icon: CreditCard },
};

interface StatusConfig {
    label: string;
    color: string;
    icon: React.ElementType;
}

interface OrderStatusBadgeProps {
    status: OrderStatus;
}

export function OrderStatusBadge( { status }: OrderStatusBadgeProps) {
    const { label, color, icon: Icon } = ORDER_STATUS_CONFIG[status];

    return (
        <span className={`ml-auto flex items-center gap-1.5 px-4 py-2 rounded-full text-sm font-bold border shadow-sm ${color}`}>
            <Icon size={16} />
            {label}
        </span>
    );
}
