import type {OrderAction, OrderLog} from "../../../models";
import {
    Activity,
    AlertTriangle,
    CheckCircle2, ChevronDown,
    CreditCard,
    Loader2,
    RotateCcw,
    ShoppingCart,
    XCircle
} from "lucide-react";
import {useState} from "react";
import {OrderAuditLogService} from "../../../api/AuditLogService";
import {formatDateTime} from "../../../data/formatter";

// ── Semantic UI Configuration ──────────────────────────────────────────────

const ORDER_ACTION_CONFIG: Record<OrderAction, { color: string, icon: any, label: string }> = {
    PLACE: { color: 'bg-blue-500/10 text-blue-600 dark:text-blue-400 border-blue-500/20', icon: ShoppingCart, label: 'Order Placed' },
    APPROVE: { color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: CheckCircle2, label: 'Approved' },
    REJECT: { color: 'bg-destructive/10 text-destructive border-destructive/20', icon: XCircle, label: 'Rejected' },
    CANCEL: { color: 'bg-muted text-muted-foreground border-border', icon: XCircle, label: 'Cancelled' },
    REFUND: { color: 'bg-purple-500/10 text-purple-600 dark:text-purple-400 border-purple-500/20', icon: CreditCard, label: 'Refunded' },
    CANCEL_REFUND: { color: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20', icon: RotateCcw, label: 'Cancel Refunded' },
    REJECT_REFUND: { color: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20', icon: AlertTriangle, label: 'Reject Refunded' },
};

// ── NEW: Lazy-Loaded Accordion Order Audit Trail ───────────────────────────
export default function OrderAuditTrail({ orderId, isManagerScreen=false }: { orderId: string, isManagerScreen?: boolean }) {
    const [isExpanded, setIsExpanded] = useState(false);
    const [hasFetched, setHasFetched] = useState(false);
    const [logs, setLogs] = useState<OrderLog[]>([]);
    const [loading, setLoading] = useState(false);

    const toggleAccordion = async () => {
        // If it's already fetched, just toggle the UI state (no extra API calls)
        if (!isExpanded && !hasFetched) {
            setLoading(true);
            try {
                const data = await OrderAuditLogService.getOrderLogsByOrderId(orderId);
                setLogs(data);
                setHasFetched(true);
            } catch (err) {
                console.error("Failed to load order audit logs");
            } finally {
                setLoading(false);
            }
        }
        setIsExpanded(!isExpanded);
    };

    return (
        <div className="mb-8 border border-border rounded-2xl bg-card overflow-hidden shadow-sm transition-all">
            {/* Accordion Header / Button */}
            <button
                onClick={toggleAccordion}
                className="w-full flex items-center justify-between p-5 hover:bg-muted/50 transition-colors outline-none"
            >
                <div className="flex items-center gap-3">
                    <div className="p-2 bg-primary/10 text-primary rounded-lg">
                        <Activity size={20} />
                    </div>
                    <span className="font-bold text-lg text-foreground">Order History</span>
                </div>
                <div className="flex items-center gap-3">
                    {/* Optional: Status indicator showing if it's currently fetching */}
                    {loading && <Loader2 size={16} className="animate-spin text-muted-foreground" />}
                    <ChevronDown size={20} className={`text-muted-foreground transition-transform duration-300 ${isExpanded ? 'rotate-180' : ''}`} />
                </div>
            </button>

            {/* Expanded Content Section */}
            {isExpanded && (
                <div className="p-5 border-t border-border bg-muted/10 animate-in slide-in-from-top-2 duration-300">
                    {loading ? (
                        <div className="flex items-center justify-center py-6">
                            <span className="text-sm font-medium text-muted-foreground">Loading history logs...</span>
                        </div>
                    ) : logs.length === 0 ? (
                        <div className="text-center py-6">
                            <p className="text-muted-foreground text-sm font-medium">No audit logs found for this order.</p>
                        </div>
                    ) : (
                        <div className="space-y-4 relative before:absolute before:inset-0 before:ml-[15px] before:-translate-x-px before:h-full before:w-0.5 before:bg-border/70 mt-2">
                            {logs.map((log) => {
                                const cfg = ORDER_ACTION_CONFIG[log.action] || ORDER_ACTION_CONFIG.PLACE;
                                const ActionIcon = cfg.icon;
                                const isManager = !!log.managerUsername;

                                return (
                                    <div key={log.id} className="relative flex items-start gap-4 z-10 group">
                                        {/* Timeline Dot */}
                                        <div className={`w-8 h-8 rounded-full border-4 border-background flex items-center justify-center shrink-0 mt-0.5 shadow-sm transition-transform group-hover:scale-110 ${cfg.color.split(' ')[0]} ${cfg.color.split(' ')[1]}`}>
                                            <ActionIcon size={14} />
                                        </div>

                                        {/* Log Content Card */}
                                        <div className="flex-1 bg-card border border-border rounded-xl p-4 shadow-sm hover:shadow-md transition-shadow">
                                            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 mb-2">
                                                <span className={`font-bold text-xs uppercase tracking-wider px-2 py-0.5 rounded-md border ${cfg.color}`}>
                                                    {cfg.label}
                                                </span>
                                                <span className="text-xs text-muted-foreground font-mono font-medium">
                                                    {formatDateTime(log.timestamp)}
                                                </span>
                                            </div>

                                            <p className="text-sm text-muted-foreground mt-2">
                                                Action performed by{' '}
                                                {isManager ? (
                                                    <span className="font-bold text-foreground">
                                                        {isManagerScreen ? `Manager @${log.managerUsername}` : 'A product manager of our store.'}
                                                    </span>
                                                ) : (
                                                    <span className="font-bold text-foreground">
                                                        {isManagerScreen ? 'Customer' : 'You'}
                                                    </span>
                                                )}
                                            </p>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}
