import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Trash2, ShoppingCart, ArrowLeft, AlertTriangle, ShoppingBag, Loader2 } from 'lucide-react';
import { formatVND } from '../../data/formatter';

// Import Services and Interfaces
import { useCart } from "../../context/CartContext";
import OrderService from '../../api/OrderService';
import type { ProductTypeName } from '../../models/product.interface';

const TYPE_LABELS: Record<ProductTypeName, string> = {
    Book: 'Book', CD: 'CD', DVD: 'DVD', Newspaper: 'Newspaper'
};

export default function CartScreen() {
    const navigate = useNavigate();

    const { cart, fetchCart, loading, updateQuantity, removeFromCart } = useCart();

    // Local API State
    const [isPlacingOrder, setIsPlacingOrder] = useState(false);
    const [updatingId, setUpdatingId] = useState<string | null>(null); // To disable buttons while updating a specific item

    // API Handlers
    const handleUpdateQuantity = async (productId: string, newQuantity: number) => {
        if (newQuantity < 1) return;
        setUpdatingId(productId);
        try {
            await updateQuantity(productId, newQuantity);
        } catch (error: any) {
            alert(`Failed to update quantity: ${error?.message || "Unknown server error"}`);
        } finally {
            setUpdatingId(null);
        }
    };

    const handleRemoveItem = async (productId: string) => {
        setUpdatingId(productId);
        try {
            await removeFromCart(productId);
        } catch (error) {
            alert("Failed to remove item.");
        } finally {
            setUpdatingId(null);
        }
    };

    const handlePlaceOrder = async () => {
        setIsPlacingOrder(true);
        try {
            // Hit the POST /order endpoint to start the place-order process in the backend session
            const draftOrderResponse = await OrderService.placeOrder();
            // Only navigate if the API succeeds
            navigate('/checkout/delivery', {
                state: {
                    prefilledDeliveryInfo: draftOrderResponse.deliveryInformation
                }
            });
        } catch (error: any) {
            const errorMessage = error.response?.data?.message || error.response?.data || "Failed to initiate place order process. Please check your cart.";
            alert(`Checkout paused: ${errorMessage}`);
            if (error.response?.status === 409) await fetchCart();
        } finally {
            setIsPlacingOrder(false);
        }
    };

    if (loading) {
        return (
            <div className="max-w-4xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4" />
                <p className="text-muted-foreground">Loading your cart...</p>
            </div>
        );
    }

    if (!cart || cart.items.length === 0) {
        return (
            <div
                className="max-w-4xl mx-auto px-4 py-20 text-center animate-in fade-in slide-in-from-bottom-4 duration-500">
                <div className="bg-muted/30 w-32 h-32 rounded-full flex items-center justify-center mx-auto mb-6">
                    <ShoppingCart size={48} className="text-muted-foreground opacity-50" />
                </div>
                <h2 className="text-2xl text-foreground font-bold mb-2">Empty cart</h2>
                <p className="text-muted-foreground text-sm mb-8">Add products to your cart to continue</p>
                <button
                    onClick={() => navigate('/')}
                    className="bg-primary text-primary-foreground px-8 py-3.5 rounded-xl font-semibold hover:bg-accent hover:text-accent-foreground transition-colors shadow-lg shadow-primary/20"
                >
                    Discover products
                </button>
            </div>
        );
    }

    // Dynamically calculate if any item exceeds available stock
    const hasInsufficient = cart.items.some(item => item.quantity > (item.product.stockQuantity ?? 0));

    return (
        <div className="max-w-5xl mx-auto px-4 py-6 animate-in fade-in duration-300">
            {/* Header */}
            <div className="flex items-center gap-4 mb-8">
                <button
                    onClick={() => navigate('/')}
                    className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm"
                >
                    <ArrowLeft size={20} />
                </button>
                <div>
                    <h1 className="text-2xl text-foreground font-bold">Your Shopping Cart</h1>
                    <p className="text-sm text-muted-foreground mt-1">{cart.items.length} products in cart</p>
                </div>
                {isPlacingOrder && (
                    <span
                        className="ml-auto bg-primary/10 border border-primary/20 text-primary text-xs px-4 py-1.5 rounded-full font-semibold flex items-center gap-1.5 shadow-sm">
                        <span className="w-2 h-2 rounded-full bg-primary animate-pulse"></span>
                        Processing...
                    </span>
                )}
            </div>

            {/* Global Stock Warning */}
            {hasInsufficient && (
                <div
                    className="bg-destructive/10 border border-destructive/20 rounded-xl p-4 mb-6 flex items-start gap-3 shadow-sm">
                    <AlertTriangle size={18} className="text-destructive shrink-0 mt-0.5" />
                    <p className="text-sm text-destructive font-medium">
                        Some products in your cart exceed available stock. Please adjust quantities before proceeding
                        with checkout.
                    </p>
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Cart Items */}
                <div className="lg:col-span-2 space-y-4">
                    {cart.items.map(item => {
                        const isInsufficient = item.quantity > (item.product.stockQuantity ?? 0);
                        const isUpdating = updatingId === item.product.id;

                        return (
                            <div
                                key={item.product.id}
                                className={`bg-card rounded-2xl border shadow-sm p-4 flex gap-5 transition-all duration-200 hover:shadow-md ${isInsufficient ? 'border-destructive/50 ring-1 ring-destructive/20' : 'border-border hover:border-primary/30'
                                    } ${isUpdating ? 'opacity-50 pointer-events-none' : ''}`}
                            >
                                <div
                                    className="w-24 h-24 bg-muted rounded-xl shrink-0 cursor-pointer overflow-hidden relative group"
                                    onClick={() => navigate(`/product/${item.product.id}`)}
                                >
                                    <img
                                        src={item.product.imageURL}
                                        alt={item.product.title}
                                        className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                                    />
                                </div>

                                <div className="flex-1 min-w-0 flex flex-col">
                                    <div className="flex items-start justify-between gap-2">
                                        <div className="min-w-0">
                                            <h3
                                                className="text-foreground text-base font-semibold line-clamp-2 cursor-pointer hover:text-primary transition-colors"
                                                onClick={() => navigate(`/product/${item.product.id}`)}
                                            >
                                                {item.product.title}
                                            </h3>
                                            <p className="text-xs text-muted-foreground mt-1 capitalize font-medium">
                                                {TYPE_LABELS[item.product.productType]}
                                            </p>
                                        </div>
                                        <button
                                            onClick={() => handleRemoveItem(item.product.id)}
                                            className="text-muted-foreground hover:text-destructive hover:bg-destructive/10 p-2 rounded-lg transition-colors shrink-0"
                                            title="Remove product"
                                        >
                                            <Trash2 size={18} />
                                        </button>
                                    </div>

                                    <div className="mt-auto pt-3 flex items-center justify-between">
                                        <div
                                            className="flex items-center border border-border bg-background rounded-lg overflow-hidden shadow-sm">
                                            <button
                                                onClick={() => handleUpdateQuantity(item.product.id, item.quantity - 1)}
                                                disabled={item.quantity <= 1}
                                                className="px-3 py-1.5 text-muted-foreground hover:bg-muted hover:text-foreground transition-colors disabled:opacity-50"
                                            >−
                                            </button>

                                            {/* Read-only input for safety, forced strictly via API */}
                                            <span
                                                className="w-12 text-center py-1.5 bg-transparent text-sm text-foreground font-medium">
                                                {item.quantity}
                                            </span>

                                            <button
                                                onClick={() => handleUpdateQuantity(item.product.id, item.quantity + 1)}
                                                className="px-3 py-1.5 text-muted-foreground hover:bg-muted hover:text-foreground transition-colors"
                                            >+
                                            </button>
                                        </div>
                                        <p className="text-primary font-bold text-base">
                                            {/* Uses itemTotalPrice from backend instead of calculating locally */}
                                            {formatVND(item.itemTotalPrice)}
                                        </p>
                                    </div>

                                    {/* Individual Stock warning */}
                                    {isInsufficient && (
                                        <div
                                            className="mt-3 flex items-center gap-1.5 text-destructive bg-destructive/5 px-3 py-1.5 rounded-md text-xs font-medium w-fit">
                                            <AlertTriangle size={14} />
                                            <span>Exceeds stock! (Only {item.product.stockQuantity} left)</span>
                                        </div>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>

                {/* Order Summary */}
                <div className="lg:col-span-1">
                    <div className="bg-card rounded-2xl border border-border shadow-sm p-6 sticky top-24">
                        <h2 className="text-foreground font-bold text-lg mb-5 pb-4 border-b border-border">Cart
                            summary</h2>

                        <div className="space-y-4 mb-6">
                            {cart.items.map(item => (
                                <div key={item.product.id} className="flex justify-between text-sm">
                                    <span className="truncate pr-3 flex-1 text-muted-foreground">
                                        {item.product.title} <span className="text-foreground font-medium ml-1">×{item.quantity}</span>
                                    </span>
                                    <span
                                        className="shrink-0 text-foreground font-medium">{formatVND(item.itemTotalPrice)}</span>
                                </div>
                            ))}
                        </div>

                        <div className="border-t border-border mt-4 pt-4 flex justify-between items-center mb-2">
                            <span className="font-semibold text-foreground">Total</span>
                            <span className="text-primary font-bold text-xl">{formatVND(cart.totalPrice)}</span>
                        </div>
                        <p className="text-xs text-muted-foreground text-right mb-6">Does not include shipping and 10%
                            VAT</p>

                        <button
                            onClick={handlePlaceOrder}
                            disabled={hasInsufficient || isPlacingOrder}
                            className={`w-full py-4 rounded-xl font-bold text-sm flex items-center justify-center gap-2 transition-all shadow-md ${hasInsufficient || isPlacingOrder
                                ? 'bg-muted text-muted-foreground cursor-not-allowed opacity-70'
                                : 'bg-primary text-primary-foreground hover:bg-accent hover:text-accent-foreground'
                                }`}
                        >
                            {isPlacingOrder ? (
                                <Loader2 size={18} className="animate-spin" />
                            ) : (
                                <ShoppingBag size={18} />
                            )}
                            {isPlacingOrder ? 'Starting Order...' : 'Place Order'}
                        </button>

                        {hasInsufficient && (
                            <p className="text-xs text-destructive text-center mt-3 font-medium">
                                Please adjust quantities before placing your order
                            </p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}