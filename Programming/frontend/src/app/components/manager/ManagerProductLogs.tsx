import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Activity, Search, Loader2, ArrowRight, PackagePlus, Edit3, Trash2, ShieldAlert, ArchiveRestore, Layers, ExternalLink } from 'lucide-react';
import { formatDateTime } from '../../data/formatter'; // Adjust path
import AuditLogService from '../../api/AuditLogService'; // Adjust path
import type { ProductAuditLog, ProductAction } from '../../models/audit.interface'; // Adjust path

// Visual configuration for different action types
const ACTION_CONFIG: Record<ProductAction | 'STOCK_ADJUST', { color: string, icon: any, label: string }> = {
    CREATE: { color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: PackagePlus, label: 'Created' },
    UPDATE: { color: 'bg-blue-500/10 text-blue-600 dark:text-blue-400 border-blue-500/20', icon: Edit3, label: 'Updated' },
    DELETE: { color: 'bg-destructive/10 text-destructive border-destructive/20', icon: Trash2, label: 'Deleted' },
    DEACTIVATE: { color: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20', icon: ShieldAlert, label: 'Deactivated' },
    ACTIVATE: { color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: ArchiveRestore, label: 'Activated' },
    STOCK_ADJUST: { color: 'bg-purple-500/10 text-purple-600 dark:text-purple-400 border-purple-500/20', icon: Layers, label: 'Stock Adjust' }
};

export default function ManagerProductLogs() {
    const navigate = useNavigate();
    const [logs, setLogs] = useState<ProductAuditLog[]>([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');

    useEffect(() => {
        fetchLogs();
    }, []);

    const fetchLogs = async () => {
        try {
            const data = await AuditLogService.getAuditLogs();
            setLogs(data);
        } catch (err) {
            console.error("Failed to load audit logs:", err);
        } finally {
            setLoading(false);
        }
    };

    const filteredLogs = logs.filter(log => {
        const q = search.toLowerCase();
        return !q ||
            log.productTitle?.toLowerCase().includes(q) ||
            log.managerUsername?.toLowerCase().includes(q) ||
            log.productId?.toLowerCase().includes(q);
    });

    if (loading) {
        return (
            <div className="py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="animate-spin text-primary mb-4" size={40} />
                <p className="text-muted-foreground font-medium">Loading audit history...</p>
            </div>
        );
    }

    return (
        <div className="animate-in fade-in duration-500 pb-10 max-w-5xl mx-auto">

            {/* Header */}
            <div className="mb-8">
                <h2 className="text-2xl font-extrabold tracking-tight text-foreground flex items-center gap-2">
                    <Activity className="text-primary" /> Product Audit Logs
                </h2>
                <p className="text-muted-foreground text-sm font-medium mt-1">
                    Track all product modifications, creations, and stock adjustments across the system. Displaying the 100 most recent records.
                </p>
            </div>

            {/* Search Filter */}
            <div className="mb-8">
                <div className="relative max-w-md">
                    <Search size={18} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground" />
                    <input
                        type="text"
                        placeholder="Search by product title, ID, or manager username..."
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                        className="w-full border border-border rounded-xl pl-10 pr-4 py-3 text-sm font-medium outline-none focus:border-primary focus:ring-1 focus:ring-primary/50 bg-input-background text-foreground transition-all shadow-sm"
                    />
                </div>
            </div>

            {/* Timeline Layout */}
            <div className="space-y-6 relative before:absolute before:inset-0 before:ml-5 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-border before:to-transparent">

                {filteredLogs.length === 0 ? (
                    <div className="text-center py-20 relative z-10 bg-background rounded-3xl border border-dashed border-border/50">
                        <p className="text-muted-foreground font-medium">No audit logs match your search.</p>
                    </div>
                ) : filteredLogs.map((log) => {

                    // Type Guard Check: Is this a StockAdjustLog or a standard ProductLog?
                    const isStockAdjust = 'oldStock' in log;
                    const actionType = isStockAdjust ? 'STOCK_ADJUST' : (log as any).action;
                    const cfg = ACTION_CONFIG[actionType as keyof typeof ACTION_CONFIG] || ACTION_CONFIG.UPDATE;
                    const ActionIcon = cfg.icon;

                    return (
                        <div key={log.id} className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active">

                            {/* Timeline Dot */}
                            <div className={`flex items-center justify-center w-10 h-10 rounded-full border-4 border-background shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 shadow-sm z-10 ${cfg.color.split(' ')[0]} ${cfg.color.split(' ')[1]}`}>
                                <ActionIcon size={16} />
                            </div>

                            {/* Log Card */}
                            <div className="w-[calc(100%-4rem)] md:w-[calc(50%-2.5rem)] bg-card border border-border rounded-2xl p-5 shadow-sm hover:shadow-md transition-shadow">

                                {/* Card Header */}
                                <div className="flex items-center justify-between mb-3 border-b border-border/50 pb-3">
                                    <span className={`text-xs font-bold px-2.5 py-1 rounded-md border ${cfg.color}`}>
                                        {cfg.label}
                                    </span>
                                    <span className="text-xs text-muted-foreground font-medium font-mono">
                                        {formatDateTime(log.timestamp)}
                                    </span>
                                </div>

                                {/* Actor & Target */}
                                <div className="mb-4">
                                    <p className="text-sm font-medium text-muted-foreground mb-1">
                                        Manager <span className="font-bold text-foreground">@{log.managerUsername}</span> modified:
                                    </p>
                                    <p className="text-base font-extrabold text-foreground truncate" title={log.productTitle}>
                                        {log.productTitle}
                                    </p>

                                    {/* Clickable Product ID Routing */}
                                    <div className="flex items-center gap-1.5 mt-1 text-xs font-mono">
                                        <span className="text-muted-foreground">ID:</span>
                                        {log.productId ? (
                                            <button
                                                onClick={() => navigate(`/manager/products/${log.productId}`)}
                                                className="text-primary hover:text-primary/80 font-semibold hover:underline underline-offset-2 transition-all flex items-center gap-1 group/link"
                                                title="View Product Details"
                                            >
                                                {log.productId}
                                                <ExternalLink size={12} className="opacity-0 group-hover/link:opacity-100 transition-opacity" />
                                            </button>
                                        ) : (
                                            <span className="text-muted-foreground italic opacity-70 border border-border/50 bg-muted px-1.5 py-0.5 rounded">
                                                N/A (Deleted)
                                            </span>
                                        )}
                                    </div>
                                </div>

                                {/* Card Body: Delta Changes */}
                                <div className="bg-muted/30 rounded-xl p-4 border border-border/50">

                                    {isStockAdjust ? (
                                        // Render StockAdjustLog Details
                                        <div>
                                            <div className="flex items-center gap-3 mb-2 text-sm">
                                                <span className="text-muted-foreground line-through font-mono">{(log as any).oldStock} qty</span>
                                                <ArrowRight size={14} className="text-muted-foreground" />
                                                <span className="font-bold text-foreground font-mono">{(log as any).newStock} qty</span>
                                            </div>
                                            <p className="text-xs text-muted-foreground font-medium italic border-t border-border/50 pt-2 mt-2">
                                                Reason: "{(log as any).reason}"
                                            </p>
                                        </div>
                                    ) : (

                                        // Render Standard ProductLog Details
                                        <div className="space-y-2">
                                            {!log.details || log.details.length === 0 ? (
                                                <p className="text-xs text-muted-foreground italic">No specific field deltas recorded.</p>
                                            ) : (
                                                log.details.map((detail: any, idx: number) => (
                                                    <div key={idx} className="text-sm flex flex-col sm:flex-row sm:items-center gap-1 sm:gap-2">
                                                        <span className="font-semibold text-foreground min-w-[100px] truncate">
                                                            {detail.fieldName}:
                                                        </span>
                                                        <div className="flex items-center gap-2 flex-1 min-w-0">
                                                          <span className="text-muted-foreground line-through truncate flex-1" title={detail.oldValue || 'empty'}>
                                                                {detail.oldValue || 'empty'}
                                                          </span>
                                                            <ArrowRight size={14} className="text-muted-foreground shrink-0" />
                                                            <span className="font-bold text-primary truncate flex-1" title={detail.newValue || 'empty'}>
                                                                {detail.newValue || 'empty'}
                                                            </span>
                                                        </div>
                                                    </div>
                                                ))
                                            )}
                                        </div>
                                    )}

                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}