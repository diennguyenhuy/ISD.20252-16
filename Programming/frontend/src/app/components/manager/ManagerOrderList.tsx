import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Search, ChevronDown, CheckCircle2, XCircle, Clock, ClipboardList, Loader2 } from 'lucide-react';
import { apiClient } from '../../api/client';
import { formatVND } from '../../data/mockData';
import type { Order, OrderStatus } from '../../models/order.interface';

const STATUS_CONFIG = {
  PENDING: { label: 'Pending', color: 'bg-amber-500/10 text-amber-500', icon: Clock },
  APPROVED: { label: 'Approved', color: 'bg-emerald-500/10 text-emerald-500', icon: CheckCircle2 },
  REJECTED: { label: 'Rejected', color: 'bg-destructive/10 text-destructive', icon: XCircle },
  CANCELLED: { label: 'Cancelled', color: 'bg-muted text-muted-foreground', icon: XCircle },
  DRAFT: { label: 'Draft', color: 'bg-muted text-muted-foreground', icon: ClipboardList },
};

const inputClass = "border border-border rounded-xl px-4 py-2.5 text-sm outline-none focus:border-primary focus:ring-1 focus:ring-primary/50 bg-input-background text-foreground transition-all";

export default function ManagerOrderList() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [filterStatus, setFilterStatus] = useState<string>('ALL');

  useEffect(() => {
    // FIX TS2307 & TS7006: Gọi API trực tiếp, ép kiểu response trả về
    apiClient.get<Order[]>('/orders/manager')
        .then(response => {
          setOrders(response.data);
          setLoading(false);
        })
        .catch(err => {
          console.error("Failed to load orders:", err);
          setLoading(false);
        });
  }, []);

  const handleAction = async (id: string, action: OrderStatus) => {
    try {
      await apiClient.patch(`/orders/${id}/status`, { status: action });
      setOrders(prev => prev.map(o => o.id === id ? { ...o, status: action } : o));
    } catch(err) {
      alert("Failed to update status");
    }
  };

  const filtered = orders.filter(o => {
    const q = search.toLowerCase();
    const matchSearch = !q || o.id.toLowerCase().includes(q) || o.deliveryInformation.customerName.toLowerCase().includes(q);
    const matchStatus = filterStatus === 'ALL' || o.status === filterStatus;
    return matchSearch && matchStatus;
  });

  if (loading) {
    return <div className="py-20 text-center"><Loader2 className="animate-spin inline text-primary" size={40} /></div>;
  }

  return (
      <div className="animate-in fade-in duration-500">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold tracking-tight text-foreground">Order Management</h2>
        </div>

        {/* FIX UNUSED VARS: Khôi phục thanh tìm kiếm và bộ lọc */}
        <div className="flex flex-wrap gap-3 mb-6">
          <div className="flex-1 min-w-[240px] relative">
            <Search size={18} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground" />
            <input
                type="text"
                placeholder="Search by order ID or customer name..."
                value={search}
                onChange={e => setSearch(e.target.value)}
                className={`w-full pl-10 pr-4 ${inputClass}`}
            />
          </div>
          <div className="relative min-w-[180px]">
            <select
                value={filterStatus}
                onChange={e => setFilterStatus(e.target.value)}
                className={`w-full appearance-none pr-10 cursor-pointer ${inputClass}`}
            >
              <option value="ALL">All statuses</option>
              <option value="PENDING">Pending</option>
              <option value="APPROVED">Approved</option>
              <option value="REJECTED">Rejected</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
            <ChevronDown size={16} className="absolute right-3.5 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none" />
          </div>
        </div>

        <div className="bg-card rounded-3xl shadow-md border border-border overflow-hidden">
          <table className="w-full text-sm">
            <thead>
            <tr className="bg-muted/30 border-b border-border">
              <th className="px-5 py-4 text-left font-semibold text-muted-foreground">Order ID</th>
              <th className="px-5 py-4 text-left font-semibold text-muted-foreground">Customer</th>
              <th className="px-5 py-4 text-right font-semibold text-muted-foreground">Amount</th>
              <th className="px-5 py-4 text-center font-semibold text-muted-foreground">Status</th>
              <th className="px-5 py-4 text-right font-semibold text-muted-foreground">Actions</th>
            </tr>
            </thead>
            <tbody>
            {filtered.length === 0 ? (
                <tr>
                  <td colSpan={5} className="py-10 text-center text-muted-foreground font-medium">No orders found.</td>
                </tr>
            ) : filtered.map(order => (
                <tr key={order.id} className="border-b border-border/50 hover:bg-muted/30 cursor-pointer transition-colors" onClick={()=>navigate(`/manager/orders/${order.id}`)}>
                  <td className="px-5 py-4 font-mono font-bold text-primary">{order.id.substring(0, 8)}...</td>
                  <td className="px-5 py-4 font-bold text-foreground">{order.deliveryInformation.customerName}</td>
                  <td className="px-5 py-4 text-right font-extrabold text-foreground">{formatVND(order.invoice?.totalAmount || 0)}</td>
                  <td className="px-5 py-4 text-center">
                        <span className={`px-2 py-1 rounded-md text-xs font-bold border shadow-sm ${STATUS_CONFIG[order.status as keyof typeof STATUS_CONFIG]?.color || 'bg-muted text-muted-foreground'}`}>
                            {STATUS_CONFIG[order.status as keyof typeof STATUS_CONFIG]?.label || order.status}
                        </span>
                  </td>
                  <td className="px-5 py-4 text-right">
                    {/* FIX FLOATING PROMISE: Thêm từ khóa 'void' trước hàm async handleAction */}
                    <button
                        onClick={(e) => { e.stopPropagation(); void handleAction(order.id, 'APPROVED'); }}
                        title="Approve"
                        className="p-2 text-emerald-500 hover:bg-emerald-500/10 rounded-lg transition-colors"
                    >
                      <CheckCircle2 size={18}/>
                    </button>
                    <button
                        onClick={(e) => { e.stopPropagation(); void handleAction(order.id, 'REJECTED'); }}
                        title="Reject"
                        className="p-2 text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                    >
                      <XCircle size={18}/>
                    </button>
                  </td>
                </tr>
            ))}
            </tbody>
          </table>
        </div>
      </div>
  );
}