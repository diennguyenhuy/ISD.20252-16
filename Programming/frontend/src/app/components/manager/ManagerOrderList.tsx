import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Search, ChevronDown, CheckCircle2, XCircle, Clock, ClipboardList, Loader2, Eye, ChevronLeft, ChevronRight, RefreshCcw } from 'lucide-react';
import OrderManagementService from '../../api/OrderManagementService'; // Adjust path as needed
import { formatVND } from '../../data/formatter';
import type { Order, OrderStatus } from '../../models/order.interface';

// Theme-safe status badges using opacity layers
const STATUS_CONFIG: Record<OrderStatus, { label: string, color: string, icon: any }> = {
    PENDING: { label: 'Pending', color: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20', icon: Clock },
    APPROVED: { label: 'Approved', color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: CheckCircle2 },
    REJECTED: { label: 'Rejected', color: 'bg-destructive/10 text-destructive border-destructive/20', icon: XCircle },
    CANCELLED: { label: 'Cancelled', color: 'bg-muted text-muted-foreground border-border', icon: XCircle },
    REFUNDED: { label: 'Refunded', color: 'bg-blue-500/10 text-blue-600 dark:text-blue-400 border-blue-500/20', icon: RefreshCcw },
};

const inputClass = "border border-border rounded-xl px-4 py-2.5 text-sm font-medium outline-none focus:border-primary focus:ring-1 focus:ring-primary/50 bg-input-background text-foreground transition-all";

export default function ManagerOrderList() {
    const navigate = useNavigate();
    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');

    const [filterStatus, setFilterStatus] = useState<OrderStatus | 'ALL'>('PENDING');
    const [page, setPage] = useState(0);

    const [isLastPage, setIsLastPage] = useState(false);

    const [confirmAction, setConfirmAction] = useState<{ id: string; action: 'APPROVE' | 'REJECT' } | null>(null);

    useEffect(() => {
        fetchOrders();
    }, [filterStatus, page]);

    const fetchOrders = async () => {
        setLoading(true);
        try {
            let data: any;

            if (filterStatus === 'PENDING') {
                data = await OrderManagementService.getPendingOrders(page);
            } else {
                const statusParam = filterStatus === 'ALL' ? undefined : filterStatus as OrderStatus;
                data = await OrderManagementService.getOrders(page, statusParam);
            }

            // Extract the list
            const orderList = Array.isArray(data) ? data : (data.content || []);
            setOrders(orderList);

            if (!Array.isArray(data) && data.last !== undefined) {
                // If it's a Spring Boot Page<T>, use the exact 'last' boolean
                setIsLastPage(data.last);
            } else {
                // Fallback: If it's just an array, assume it's the last page if we received less than 30 items
                setIsLastPage(orderList.length < 30);
            }

        } catch (err) {
            console.error("Failed to load orders:", err);
        } finally {
            setLoading(false);
        }
    };

    const handleAction = async (id: string, action: 'APPROVE' | 'REJECT') => {
        try {
            if (action === 'APPROVE') {
                await OrderManagementService.approveOrder(id);
            } else {
                await OrderManagementService.rejectOrder(id);
            }

            if (filterStatus === 'PENDING') {
                setOrders(prev => prev.filter(o => o.id !== id));
            } else {
                setOrders(prev => prev.map(o => o.id === id ? { ...o, status: action === 'APPROVE' ? 'APPROVED' : 'REJECTED' } : o));
            }
            setConfirmAction(null);
        } catch(err) {
            alert("Failed to update status. Please try again.");
        }
    };

    const filtered = orders.filter(o => {
        const q = search.toLowerCase();
        return !q ||
            o.id.toLowerCase().includes(q) ||
            o.deliveryInformation?.customerName?.toLowerCase().includes(q);
    });

    return (
        <div className="animate-in fade-in duration-500 pb-10">

            {/* Header */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
                <div>
                    <h2 className="text-2xl font-extrabold tracking-tight text-foreground">Order Management</h2>
                    <p className="text-muted-foreground text-sm font-medium mt-1">
                        {filterStatus === 'PENDING' ? 'Orders requiring your approval' : 'Complete system order history'}
                    </p>
                </div>
            </div>

            {/* Filters */}
            <div className="flex flex-wrap gap-3 mb-6">
                <div className="flex-1 min-w-[240px] relative">
                    <Search size={18} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground" />
                    <input
                        type="text"
                        placeholder="Search by Order ID or Customer Name..."
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                        className={`w-full pl-10 pr-4 ${inputClass}`}
                    />
                </div>
                <div className="relative min-w-[180px]">
                    <select
                        value={filterStatus}
                        onChange={e => { setFilterStatus(e.target.value as OrderStatus | 'ALL'); setPage(0); setIsLastPage(false); }}
                        className={`w-full appearance-none pr-10 cursor-pointer ${inputClass}`}
                    >
                        <option value="PENDING">Pending Approval</option>
                        <option value="ALL">All Orders</option>
                        <option value="APPROVED">Approved</option>
                        <option value="REJECTED">Rejected</option>
                        <option value="CANCELLED">Cancelled</option>
                        <option value="REFUNDED">Refunded</option>
                    </select>
                    <ChevronDown size={16} className="absolute right-3.5 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none" />
                </div>
            </div>

            {/* Table */}
            <div className="bg-card rounded-3xl shadow-md border border-border overflow-hidden">
                {loading ? (
                    <div className="py-20 text-center flex flex-col items-center justify-center">
                        <Loader2 className="animate-spin text-primary mb-4" size={32} />
                        <p className="text-muted-foreground font-medium">Loading orders...</p>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-sm">
                            <thead>
                            <tr className="bg-muted/30 border-b border-border">
                                <th className="px-5 py-4 text-left font-semibold text-muted-foreground uppercase tracking-wider text-xs">Order ID</th>
                                <th className="px-5 py-4 text-left font-semibold text-muted-foreground uppercase tracking-wider text-xs">Customer</th>
                                <th className="px-5 py-4 text-right font-semibold text-muted-foreground uppercase tracking-wider text-xs">Amount</th>
                                <th className="px-5 py-4 text-center font-semibold text-muted-foreground uppercase tracking-wider text-xs">Status</th>
                                <th className="px-5 py-4 text-right font-semibold text-muted-foreground uppercase tracking-wider text-xs">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.length === 0 ? (
                                <tr>
                                    <td colSpan={5} className="py-16 text-center">
                                        <ClipboardList size={32} className="mx-auto mb-3 text-muted-foreground opacity-30" />
                                        <p className="text-foreground font-medium text-base">No orders found</p>
                                    </td>
                                </tr>
                            ) : filtered.map(order => {
                                const cfg = STATUS_CONFIG[order.status] || STATUS_CONFIG.PENDING;
                                const StatusIcon = cfg.icon;
                                return (
                                    <tr
                                        key={order.id}
                                        className="border-b border-border/50 hover:bg-muted/30 cursor-pointer transition-colors"
                                        onClick={() => navigate(`/manager/orders/${order.id}`)}
                                    >
                                        <td className="px-5 py-4">
                                            <span className="font-mono text-primary font-bold bg-primary/10 px-2 py-1 rounded border border-primary/20">
                                              {order.id.substring(0, 8)}...
                                            </span>
                                        </td>
                                        <td className="px-5 py-4 font-bold text-foreground">
                                            {order.deliveryInformation?.customerName || 'N/A'}
                                        </td>
                                        <td className="px-5 py-4 text-right font-extrabold text-foreground">
                                            {formatVND(order.invoice?.totalAmount || order.invoice?.deliveryFee || 0)}
                                        </td>
                                        <td className="px-5 py-4 text-center">
                                            <span className={`inline-flex items-center gap-1.5 text-xs px-2.5 py-1 rounded-md font-bold border shadow-sm ${cfg.color}`}>
                                              <StatusIcon size={12} />
                                                {cfg.label}
                                            </span>
                                        </td>
                                        <td className="px-5 py-4">
                                            <div className="flex items-center justify-end gap-1.5" onClick={e => e.stopPropagation()}>
                                                <button
                                                    onClick={() => navigate(`/manager/orders/${order.id}`)}
                                                    className="p-2 text-muted-foreground hover:text-primary hover:bg-primary/10 rounded-lg transition-colors"
                                                    title="View Details"
                                                >
                                                    <Eye size={18} />
                                                </button>

                                                {order.status === 'PENDING' && (
                                                    <>
                                                        <button
                                                            onClick={() => setConfirmAction({ id: order.id, action: 'APPROVE' })}
                                                            title="Approve Order"
                                                            className="p-2 text-muted-foreground hover:text-emerald-600 hover:bg-emerald-500/10 dark:hover:text-emerald-400 rounded-lg transition-colors"
                                                        >
                                                            <CheckCircle2 size={18} />
                                                        </button>
                                                        <button
                                                            onClick={() => setConfirmAction({ id: order.id, action: 'REJECT' })}
                                                            title="Reject Order"
                                                            className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                                                        >
                                                            <XCircle size={18} />
                                                        </button>
                                                    </>
                                                )}
                                            </div>
                                        </td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>
                    </div>
                )}

                {/* Pagination Controls */}
                {!loading && (orders.length > 0 || page > 0) && (
                    <div className="px-5 py-4 border-t border-border flex items-center justify-between bg-muted/10">
                        <span className="text-sm text-muted-foreground font-medium">Page {page + 1}</span>
                        <div className="flex gap-2">
                            <button
                                disabled={page === 0}
                                onClick={() => setPage(p => p - 1)}
                                className="p-1.5 rounded-lg border border-border bg-card text-foreground disabled:opacity-50 disabled:cursor-not-allowed hover:bg-muted transition-colors shadow-sm"
                            >
                                <ChevronLeft size={18} />
                            </button>
                            <button
                                disabled={isLastPage} // NEW: Properly disable Next button
                                onClick={() => setPage(p => p + 1)}
                                className="p-1.5 rounded-lg border border-border bg-card text-foreground disabled:opacity-50 disabled:cursor-not-allowed hover:bg-muted transition-colors shadow-sm"
                            >
                                <ChevronRight size={18} />
                            </button>
                        </div>
                    </div>
                )}
            </div>

            {/* Confirmation Modal */}
            {confirmAction && (
                <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <div className={`flex items-center gap-4 mb-5`}>
                            <div className={`p-3 rounded-2xl border ${confirmAction.action === 'APPROVE' ? 'bg-emerald-500/10 border-emerald-500/20' : 'bg-destructive/10 border-destructive/20'}`}>
                                {confirmAction.action === 'APPROVE' ? (
                                    <CheckCircle2 size={24} className="text-emerald-600 dark:text-emerald-400" />
                                ) : (
                                    <XCircle size={24} className="text-destructive" />
                                )}
                            </div>
                            <h3 className="font-bold text-lg text-foreground tracking-tight">
                                {confirmAction.action === 'APPROVE' ? 'Approve Order' : 'Reject Order'}
                            </h3>
                        </div>
                        <p className="text-muted-foreground text-sm font-medium mb-8 leading-relaxed">
                            Are you sure you want to {confirmAction.action === 'APPROVE' ? 'approve' : 'reject'} order <span className="font-mono text-foreground font-bold">{confirmAction.id.substring(0, 8)}...</span>?
                        </p>
                        <div className="flex gap-3">
                            <button
                                onClick={() => setConfirmAction(null)}
                                className="flex-1 py-3 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={() => void handleAction(confirmAction.id, confirmAction.action)}
                                className={`flex-1 py-3 rounded-xl text-primary-foreground font-bold shadow-md hover:shadow-lg transition-all hover:-translate-y-0.5 ${
                                    confirmAction.action === 'APPROVE'
                                        ? 'bg-emerald-600 hover:bg-emerald-700 shadow-emerald-500/20'
                                        : 'bg-destructive hover:bg-destructive/90 shadow-destructive/20'
                                }`}
                            >
                                Confirm
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}