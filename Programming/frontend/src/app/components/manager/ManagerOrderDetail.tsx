import { useParams, useNavigate } from 'react-router';
import { ArrowLeft, Package, User, MapPin, Phone, CreditCard, Hash, Calendar, Truck, CheckCircle2, XCircle, Clock, X, FileText, Loader2 } from 'lucide-react';
import { formatVND, formatDateTime } from '../../data/mockData';
import { useState, useEffect } from 'react';
import { apiClient } from '../../api/client';
import type { Order } from '../../models/order.interface';

const STATUS_CONFIG = {
  PENDING: { label: 'Pending Processing', color: 'bg-amber-500/10 text-amber-500', icon: Clock },
  APPROVED: { label: 'Approved', color: 'bg-emerald-500/10 text-emerald-500', icon: CheckCircle2 },
  REJECTED: { label: 'Rejected', color: 'bg-destructive/10 text-destructive', icon: XCircle },
  CANCELLED: { label: 'Cancelled', color: 'bg-muted text-muted-foreground', icon: X },
  DRAFT: { label: 'Draft', color: 'bg-muted text-muted-foreground', icon: FileText },
  REFUNDED: { label: 'Refunded', color: 'bg-blue-500/10 text-blue-500', icon: CreditCard },
};

export default function ManagerOrderDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [confirmAction, setConfirmAction] = useState<'APPROVED' | 'REJECTED' | null>(null);
  const [isUpdating, setIsUpdating] = useState(false);

  useEffect(() => {
    if (id) {
      // FIX LỖI 1 & 2: Gọi trực tiếp apiClient, ép kiểu trả về rõ ràng
      apiClient.get<Order>(`/orders/${id}`)
          .then(response => {
            setOrder(response.data);
            setLoading(false);
          })
          .catch(() => {
            setLoading(false);
          });
    }
  }, [id]);

  if (loading) {
    return <div className="py-32 flex justify-center"><Loader2 className="animate-spin text-primary" size={48} /></div>;
  }

  if (!order) {
    return (
        <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
          <FileText size={64} className="mx-auto mb-6 text-muted-foreground opacity-30" />
          <p className="text-xl text-foreground font-bold mb-2">Order not found</p>
          <p className="text-muted-foreground mb-6">This order does not exist or has been removed.</p>
          <button onClick={() => navigate('/manager/orders')} className="text-primary font-semibold hover:text-accent-foreground underline underline-offset-4">
            Back to list
          </button>
        </div>
    );
  }

  const statusCfg = STATUS_CONFIG[order.status as keyof typeof STATUS_CONFIG] || STATUS_CONFIG['PENDING'];
  const StatusIcon = statusCfg.icon;

  const handleAction = async (action: 'APPROVED' | 'REJECTED') => {
    setIsUpdating(true);
    try {
      // FIX LỖI: Gọi trực tiếp API update status thay vì dùng file service ngoài
      await apiClient.patch(`/orders/${order.id}/status`, { status: action });
      setOrder({ ...order, status: action });
      setConfirmAction(null);
    } catch(err) {
      alert("Could not update order status");
    } finally {
      setIsUpdating(false);
    }
  };

  const delivery = order.deliveryInformation;
  const invoice = order.invoice;
  const trans = order.paymentTransaction;

  return (
      <div className="max-w-4xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500">
        <div className="flex flex-wrap items-center gap-4 mb-8">
          <button onClick={() => navigate('/manager/orders')} className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0">
            <ArrowLeft size={20} />
          </button>
          <div>
            <h2 className="text-2xl text-foreground font-bold tracking-tight">Order #{order.id.slice(0,8)}...</h2>
            <p className="text-sm text-muted-foreground mt-1 font-medium">{formatDateTime(order.createdAt)}</p>
          </div>
          <span className={`ml-auto flex items-center gap-1.5 px-4 py-2 rounded-full text-sm font-bold border shadow-sm ${statusCfg.color}`}>
          <StatusIcon size={16} /> {statusCfg.label}
        </span>
        </div>

        <div className="bg-card rounded-3xl border border-border shadow-lg overflow-hidden mb-6">
          <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <Package size={18} className="text-primary" />
              <span className="text-base font-bold text-foreground">Products ({order.items.length})</span>
            </div>
          </div>
          <div className="p-6 space-y-4">
            {order.items.map((item, idx) => (
                <div key={idx} className="flex flex-col sm:flex-row sm:items-center gap-4 pb-4 border-b border-border/50 last:border-0 last:pb-0">
                  <div className="flex items-center gap-4 flex-1 min-w-0">
                    <div className="w-16 h-16 rounded-xl overflow-hidden bg-muted shrink-0 border border-border flex items-center justify-center">
                      <Package size={24} className="text-muted-foreground/50"/>
                    </div>
                    <div className="min-w-0">
                      <p className="text-base text-foreground font-bold line-clamp-1 mb-1">{item.productName}</p>
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-semibold px-2 py-0.5 rounded-md bg-muted text-muted-foreground">Weight: {item.unitWeight} kg</span>
                        <span className="text-sm text-muted-foreground font-medium">Unit price: {formatVND(item.unitPrice)}</span>
                      </div>
                    </div>
                  </div>
                  <div className="text-left sm:text-right shrink-0 pl-20 sm:pl-0 mt-1 sm:mt-0">
                    <p className="text-sm text-muted-foreground font-medium mb-1">Quantity: <span className="text-foreground font-bold">{item.quantity}</span></p>
                    <p className="text-base text-primary font-bold">{formatVND(item.unitPrice * item.quantity)}</p>
                  </div>
                </div>
            ))}
          </div>

          {invoice && (
              <div className="border-t border-border p-6 bg-muted/10">
                <div className="max-w-xs ml-auto space-y-2.5 text-sm">
                  <div className="flex justify-between items-center text-muted-foreground font-medium">
                    <span>Subtotal (excl. VAT)</span>
                    <span className="text-foreground">{formatVND(invoice.totalPriceWithoutVAT)}</span>
                  </div>
                  <div className="flex justify-between items-center text-muted-foreground font-medium">
                    <span>VAT (10%)</span>
                    <span className="text-foreground">+{formatVND(invoice.totalPriceWithVAT - invoice.totalPriceWithoutVAT)}</span>
                  </div>
                  <div className="flex justify-between items-center text-muted-foreground font-medium">
                    <span>Delivery Fee</span>
                    <span className="text-foreground">+{formatVND(invoice.deliveryFee)}</span>
                  </div>
                  <div className="border-t-2 border-border pt-3 mt-1 flex justify-between items-center">
                    <span className="font-bold text-foreground text-base">Total Payment</span>
                    <span className="text-2xl text-primary font-extrabold tracking-tight">{formatVND(invoice.totalAmount)}</span>
                  </div>
                </div>
              </div>
          )}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          {delivery && (
              <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden flex flex-col">
                <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                  <Truck size={18} className="text-primary" />
                  <span className="text-base font-bold text-foreground">Shipping information</span>
                </div>
                <div className="p-6 grid grid-cols-1 gap-4 flex-1">
                  <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
                    <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><User size={16} className="text-primary" /></div>
                    <div>
                      <p className="text-xs text-muted-foreground font-medium mb-0.5">Recipient</p>
                      <p className="text-sm text-foreground font-bold">{delivery.customerName}</p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
                    <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><Phone size={16} className="text-primary" /></div>
                    <div>
                      <p className="text-xs text-muted-foreground font-medium mb-0.5">Phone</p>
                      <p className="text-sm text-foreground font-bold">{delivery.phoneNumber}</p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
                    <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><MapPin size={16} className="text-primary" /></div>
                    <div>
                      <p className="text-xs text-muted-foreground font-medium mb-0.5">Address</p>
                      <p className="text-sm text-foreground font-semibold leading-relaxed">{delivery.address}, {delivery.commune}, {delivery.province}</p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3 bg-primary/5 p-3.5 rounded-2xl border border-primary/20">
                    <div className="p-2 bg-primary/20 rounded-lg shadow-sm shrink-0"><Truck size={16} className="text-primary" /></div>
                    <div>
                      <p className="text-xs text-muted-foreground font-medium mb-0.5">Delivery method</p>
                      <p className="text-sm text-primary font-bold">{delivery.deliveryMethod === 'rush' ? 'Rush delivery (1-2 days)' : 'Standard delivery (3-5 days)'}</p>
                    </div>
                  </div>
                </div>
              </div>
          )}

          {trans && (
              <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden flex flex-col">
                <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                  <CreditCard size={18} className="text-primary" />
                  <span className="text-base font-bold text-foreground">Transaction details</span>
                </div>
                <div className="p-6 space-y-4 flex-1">
                  <div className="flex items-center justify-between border-b border-border/50 pb-3">
                    <div className="flex items-center gap-2 text-muted-foreground"><Hash size={16} /><span className="text-sm font-medium">Transaction code</span></div>
                    <p className="text-sm text-foreground font-mono font-bold bg-muted px-2 py-0.5 rounded border border-border">{trans.id}</p>
                  </div>
                  <div className="flex items-center justify-between border-b border-border/50 pb-3">
                    <div className="flex items-center gap-2 text-muted-foreground"><CreditCard size={16} /><span className="text-sm font-medium">Method</span></div>
                    <p className="text-sm text-foreground font-bold flex items-center gap-1.5">{trans.transactionMethod === 'VIETQR' ? '📱 QR Code' : '💳 PayPal'}</p>
                  </div>
                  <div className="flex items-center justify-between border-b border-border/50 pb-3">
                    <div className="flex items-center gap-2 text-muted-foreground"><Calendar size={16} /><span className="text-sm font-medium">Payment time</span></div>
                    <p className="text-sm text-foreground font-semibold">{formatDateTime(trans.transactionTimestamp)}</p>
                  </div>
                  <div className="flex items-center justify-between pt-1">
                    <div className="flex items-center gap-2 text-muted-foreground shrink-0"><FileText size={16} /><span className="text-sm font-medium">Transfer note</span></div>
                    <p className="text-sm text-foreground font-bold text-right truncate pl-4">{trans.transactionContent}</p>
                  </div>
                </div>
              </div>
          )}
        </div>

        <div className="flex flex-col sm:flex-row gap-4 pt-4 border-t border-border">
          {order.status === 'PENDING' ? (
              <>
                <button onClick={() => navigate('/manager/orders')} className="sm:flex-none flex items-center justify-center gap-2 py-3.5 px-6 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted shadow-sm">
                  <ArrowLeft size={18} /> Back
                </button>
                <button onClick={() => setConfirmAction('REJECTED')} className="flex-1 py-3.5 px-6 rounded-xl border border-destructive/30 text-destructive bg-card font-semibold hover:bg-destructive/10 flex items-center justify-center gap-2 shadow-sm">
                  <XCircle size={18} /> Reject order
                </button>
                <button onClick={() => setConfirmAction('APPROVED')} className="flex-1 py-3.5 px-6 rounded-xl bg-emerald-600 text-white font-bold hover:bg-emerald-700 shadow-md flex items-center justify-center gap-2">
                  <CheckCircle2 size={18} /> Approve order
                </button>
              </>
          ) : (
              <button onClick={() => navigate('/manager/orders')} className="flex-1 sm:flex-none flex items-center justify-center gap-2 py-3.5 px-6 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted shadow-sm">
                <ArrowLeft size={18} /> Back to list
              </button>
          )}
        </div>

        {confirmAction && (
            <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
              <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border">
                <div className="flex items-center gap-4 mb-5">
                  <div className={`p-3 rounded-2xl border ${confirmAction === 'APPROVED' ? 'bg-emerald-500/10 border-emerald-500/20' : 'bg-destructive/10 border-destructive/20'}`}>
                    {confirmAction === 'APPROVED' ? <CheckCircle2 size={24} className="text-emerald-500" /> : <XCircle size={24} className="text-destructive" />}
                  </div>
                  <h3 className="font-bold text-lg">{confirmAction === 'APPROVED' ? 'Approve order' : 'Reject order'}</h3>
                </div>
                <p className="text-muted-foreground text-sm mb-8">Are you sure you want to {confirmAction.toLowerCase()} this order? This action updates the status immediately.</p>
                <div className="flex gap-3">
                  <button disabled={isUpdating} onClick={() => setConfirmAction(null)} className="flex-1 py-3 rounded-xl border border-border bg-card text-foreground font-bold disabled:opacity-50">Cancel</button>
                  <button disabled={isUpdating} onClick={() => handleAction(confirmAction)} className={`flex-1 flex justify-center items-center py-3 rounded-xl text-primary-foreground font-bold shadow-md disabled:opacity-70 ${confirmAction === 'APPROVED' ? 'bg-emerald-600 hover:bg-emerald-700' : 'bg-destructive hover:bg-destructive/90'}`}>
                    {isUpdating ? <Loader2 size={20} className="animate-spin" /> : 'Confirm'}
                  </button>
                </div>
              </div>
            </div>
        )}
      </div>
  );
}