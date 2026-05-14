import {useState, useEffect} from 'react';
import {useNavigate, useParams} from 'react-router';
import {CheckCircle, Home, Package, User, MapPin, Phone, CreditCard, Hash, Calendar, FileText, Loader2} from 'lucide-react';
import {formatVND, formatDateTime} from '../../data/mockData'; // Assuming formatDateTime still exists here
import OrderService from '../../api/orderService';
import type {Order} from '../../models/order.interface';

export default function SuccessOrder() {
    const navigate = useNavigate();
    const {id} = useParams<{ id: string }>();

    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!id) {
            setLoading(false);
            return;
        }

        OrderService.getOrder(id)
            .then(data => {
                setOrder(data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Failed to fetch completed order:", err);
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return (
            <div className="max-w-xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-12 h-12 animate-spin text-primary mb-4"/>
                <p className="text-muted-foreground font-medium">Retrieving your receipt...</p>
            </div>
        );
    }

    if (!order) {
        return (
            <div className="max-w-xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <div className="inline-flex items-center justify-center w-24 h-24 rounded-full bg-primary/10 mb-6">
                    <CheckCircle size={48} className="text-primary"/>
                </div>
                <h2 className="text-2xl text-foreground font-bold mb-4">Cannot find order!</h2>
                <p className="text-muted-foreground mb-8">It seems the transaction has expired or the order does not
                    exist.</p>
                <button
                    onClick={() => navigate('/')}
                    className="bg-primary text-primary-foreground px-8 py-3.5 rounded-xl font-bold hover:bg-accent hover:text-accent-foreground transition-all shadow-lg hover:shadow-primary/30 hover:-translate-y-0.5"
                >
                    Back to Home
                </button>
            </div>
        );
    }

    // Destructure for cleaner JSX
    const {deliveryInformation, paymentTransaction, invoice} = order;

    return (
        <div className="max-w-xl mx-auto px-4 py-10 animate-in fade-in slide-in-from-bottom-4 duration-500">
            {/* Success Header with Cinematic Glow */}
            <div className="text-center mb-10 relative">
                <div
                    className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-48 h-48 bg-primary/20 rounded-full blur-3xl pointer-events-none"></div>

                <div
                    className="inline-flex items-center justify-center w-24 h-24 rounded-full bg-primary/10 mb-5 relative z-10 shadow-inner border border-primary/20">
                    <CheckCircle size={52} className="text-primary animate-in zoom-in duration-500 delay-150"/>
                </div>
                <h1 className="text-3xl sm:text-4xl text-foreground font-extrabold tracking-tight mb-3 relative z-10">
                    Order Placed Successfully! 🎉
                </h1>
                <p className="text-muted-foreground font-medium relative z-10">
                    Thank you for shopping with AIMS.<br className="sm:hidden"/> We will process your order immediately!
                </p>
            </div>

            {/* Order Summary Card */}
            <div className="bg-card rounded-3xl border border-border shadow-xl overflow-hidden mb-6 relative z-10">
                <div
                    className="bg-gradient-to-r from-primary/10 to-transparent border-b border-border px-6 py-4 flex items-center justify-between">
          <span className="font-bold text-foreground flex items-center gap-2">
            <Package size={18} className="text-primary"/>
            Order Information
          </span>
                    <span
                        className="bg-background border border-border px-3 py-1 rounded-md text-primary font-mono text-sm font-bold shadow-sm">
            #{order.id}
          </span>
                </div>

                <div className="p-6 space-y-5">
                    {/* Customer Info Grid */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
                        <div className="flex items-start gap-3 bg-muted/30 p-4 rounded-2xl border border-border/50">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0">
                                <User size={16} className="text-primary"/>
                            </div>
                            <div>
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Recipient</p>
                                <p className="text-sm text-foreground font-bold">{deliveryInformation.customerName}</p>
                            </div>
                        </div>
                        <div className="flex items-start gap-3 bg-muted/30 p-4 rounded-2xl border border-border/50">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0">
                                <Phone size={16} className="text-primary"/>
                            </div>
                            <div>
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Phone Number</p>
                                <p className="text-sm text-foreground font-bold">{deliveryInformation.phoneNumber}</p>
                            </div>
                        </div>
                    </div>

                    <div className="flex items-start gap-3 bg-muted/30 p-4 rounded-2xl border border-border/50">
                        <div className="p-2 bg-background rounded-lg shadow-sm shrink-0 mt-0.5">
                            <MapPin size={16} className="text-primary"/>
                        </div>
                        <div>
                            <p className="text-xs text-muted-foreground font-medium mb-0.5">Delivery Address</p>
                            <p className="text-sm text-foreground font-semibold leading-relaxed">
                                {deliveryInformation.address}, {deliveryInformation.commune}, {deliveryInformation.province}
                            </p>
                        </div>
                    </div>

                    <div
                        className="flex items-center justify-between bg-primary/5 p-5 rounded-2xl border border-primary/20 shadow-sm mt-2">
                        <div className="flex items-center gap-3">
                            <div className="p-2 bg-primary/20 rounded-lg shrink-0">
                                <FileText size={18} className="text-primary"/>
                            </div>
                            <p className="text-sm text-muted-foreground font-medium">Total Amount paid</p>
                        </div>
                        <p className="text-2xl text-primary font-extrabold tracking-tight">{formatVND(paymentTransaction.amountPaid)}</p>
                    </div>
                </div>
            </div>

            {/* Transaction Info */}
            <div className="bg-card rounded-3xl border border-border shadow-lg overflow-hidden mb-8 relative z-10">
                <div className="bg-muted/30 border-b border-border px-6 py-4">
                    <span className="font-bold text-foreground">Transaction Receipt</span>
                </div>
                <div className="p-6 space-y-4">
                    <div className="flex items-center justify-between border-b border-border/50 pb-3">
                        <div className="flex items-center gap-2 text-muted-foreground">
                            <Hash size={16}/>
                            <span className="text-sm font-medium">Transaction ID</span>
                        </div>
                        <p className="text-sm text-foreground font-mono font-bold">{paymentTransaction.id}</p>
                    </div>

                    <div className="flex items-center justify-between border-b border-border/50 pb-3">
                        <div className="flex items-center gap-2 text-muted-foreground">
                            <FileText size={16}/>
                            <span className="text-sm font-medium">Content</span>
                        </div>
                        <p className="text-sm text-foreground font-bold bg-muted px-2 py-1 rounded border border-border">{paymentTransaction.transactionContent}</p>
                    </div>

                    <div className="flex items-center justify-between border-b border-border/50 pb-3">
                        <div className="flex items-center gap-2 text-muted-foreground">
                            <Calendar size={16}/>
                            <span className="text-sm font-medium">Time</span>
                        </div>
                        <p className="text-sm text-foreground font-semibold">{formatDateTime(paymentTransaction.transactionTimestamp)}</p>
                    </div>

                    <div className="flex items-center justify-between pt-1">
                        <div className="flex items-center gap-2 text-muted-foreground">
                            <CreditCard size={16}/>
                            <span className="text-sm font-medium">Payment Method</span>
                        </div>
                        <p className="text-sm text-primary font-bold flex items-center gap-1.5">
                            {paymentTransaction.transactionMethod === 'VIETQR' ? '📱 QR Code' : '💳 PayPal'}
                        </p>
                    </div>
                </div>
            </div>

            <div className="space-y-4 relative z-10">
                <button
                    onClick={() => navigate(`/order/${order.id}`, {
                        state: { order: order },
                    })}
                    className="w-full py-4 rounded-xl border-2 border-primary/20 text-primary bg-primary/5 text-base font-bold hover:bg-primary/10 hover:border-primary/40 transition-colors shadow-sm"
                >
                    View Order Details
                </button>
                <button
                    onClick={() => navigate('/')}
                    className="w-full py-4 rounded-xl bg-primary text-primary-foreground font-bold text-base hover:bg-accent hover:text-accent-foreground transition-all shadow-lg hover:shadow-primary/30 hover:-translate-y-0.5 flex items-center justify-center gap-2"
                >
                    <Home size={20}/>
                    Continue Shopping
                </button>
            </div>
        </div>
    );
}