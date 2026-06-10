import { useParams, useNavigate } from 'react-router';
import { useState, useEffect } from 'react';
import {
    ArrowLeft, Package, ShoppingCart,
    ChevronRight, CheckCircle2, Loader2,
} from 'lucide-react';

import HomepageService from '../../api/HomepageService';
import { useCart } from '../../context/CartContext';
import { formatVND } from '../../data/formatter';
import type { Product } from '../../models/product.interface';

import { ProductInfoTable } from '../shared/product/ProductInfoTable';
import { ProductCreatorLine } from '../shared/product/ProductCreatorLine';
import { PRODUCT_TYPE_META } from '../shared/product/productTypeMetadata';

export default function CustomerProductDetail() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [product, setProduct] = useState<Product | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [qty, setQty] = useState(1);
    const [added, setAdded] = useState(false);

    const { addToCart } = useCart();

    // ── Data fetching ────────────────────────────────────────────────────────
    useEffect(() => {
        if (!id) return;

        HomepageService.getProductDetail(id)
            .then(data => {
                setProduct(data as unknown as Product);
                setLoading(false);
            })
            .catch(err => {
                if (err.response?.status === 404) {
                    setError(err.response.data || 'Product not found or has been removed.');
                } else if (err.response) {
                    setError('An unexpected server error occurred. Please try again later.');
                } else {
                    setError('Network error. Please check your connection.');
                }
                setLoading(false);
            });
    }, [id]);

    // ── Cart handler ─────────────────────────────────────────────────────────
    const handleAddToCart = async () => {
        try {
            await addToCart(product!.id, qty);
            setAdded(true);
            setTimeout(() => setAdded(false), 2000);
        } catch (err: any) {
            alert(err.message || 'Could not add item to cart.');
        }
    };

    // ── Loading / error states ────────────────────────────────────────────────
    if (loading) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4" />
                <p className="text-muted-foreground">Loading product details...</p>
            </div>
        );
    }

    if (error || !product) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-destructive/10 mb-6">
                    <Package size={32} className="text-destructive" />
                </div>
                <p className="text-foreground text-xl font-bold mb-2">Oops!</p>
                <p className="text-muted-foreground text-lg mb-8">{error ?? 'Product not found.'}</p>
                <button
                    onClick={() => navigate('/')}
                    className="bg-primary text-primary-foreground px-8 py-3.5 rounded-xl font-bold hover:bg-accent hover:text-accent-foreground transition-all shadow-lg hover:shadow-primary/30 hover:-translate-y-0.5"
                >
                    Back to Home
                </button>
            </div>
        );
    }

    // ── Resolved display metadata ─────────────────────────────────────────────
    const { label, icon: TypeIcon, color } = PRODUCT_TYPE_META[product.productType];
    const stockQty = product.stockQuantity ?? 0;

    return (
        <div className="max-w-7xl mx-auto px-4 py-6 animate-in fade-in slide-in-from-bottom-4 duration-500">

            {/* ── Breadcrumb ── */}
            <nav className="flex items-center gap-2 text-sm text-muted-foreground mb-8">
                <button
                    onClick={() => navigate(-1)}
                    className="hover:text-foreground transition-colors flex items-center gap-1"
                >
                    <ArrowLeft size={16} className="mr-1" />
                    Back
                </button>
                <ChevronRight size={14} className="opacity-50" />
                <span className={`px-2.5 py-0.5 rounded-full text-xs font-semibold border ${color}`}>
                    {label}
                </span>
                <ChevronRight size={14} className="opacity-50" />
                <span className="text-foreground font-medium truncate max-w-[200px]">{product.title}</span>
            </nav>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">

                {/* ── Left: Image ── */}
                <div className="space-y-4">
                    <div className="bg-card rounded-3xl overflow-hidden border border-border shadow-lg group relative">
                        <div className="absolute inset-0 bg-gradient-to-tr from-primary/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none" />
                        <img
                            src={product.imageURL}
                            alt={product.title}
                            className="w-full object-cover group-hover:scale-105 transition-transform duration-700 ease-out"
                            style={{ maxHeight: '500px' }}
                        />
                        {stockQty === 0 && (
                            <div className="absolute inset-0 bg-black/60 backdrop-blur-[2px] flex items-center justify-center z-10">
                                <span className="bg-background text-foreground px-6 py-2 rounded-full text-lg font-bold shadow-lg">
                                    Out of stock
                                </span>
                            </div>
                        )}
                    </div>
                </div>

                {/* ── Right: Info + Actions ── */}
                <div className="space-y-6 flex flex-col">

                    {/* Type badge + title + creator */}
                    <div>
                        <div className="flex items-center gap-2 mb-3">
                            <span className={`flex items-center gap-1.5 text-xs font-bold px-3 py-1 rounded-full border shadow-sm ${color}`}>
                                <TypeIcon size={14} />
                                {label}
                            </span>
                        </div>
                        <h1 className="text-3xl sm:text-4xl text-foreground font-bold leading-tight tracking-tight mb-4">
                            {product.title}
                        </h1>
                        {/* Creator line — type-dispatch fully delegated */}
                        <ProductCreatorLine product={product} />
                    </div>

                    {/* Price */}
                    <div className="bg-primary/5 border border-primary/20 rounded-2xl p-6 shadow-sm relative overflow-hidden">
                        <div className="absolute top-0 right-0 w-32 h-32 bg-primary/10 rounded-full blur-3xl -mr-10 -mt-10" />
                        <p className="text-4xl text-primary font-extrabold tracking-tight relative z-10">
                            {formatVND(product.currentPrice)}
                        </p>
                        {product.originalValue > product.currentPrice && (
                            <p className="text-muted-foreground line-through mt-1 text-sm font-medium relative z-10">
                                Original price: {formatVND(product.originalValue)}
                            </p>
                        )}
                    </div>

                    {/* Stock indicator */}
                    <div className="flex items-center gap-2 font-medium">
                        <Package size={18} className={stockQty > 0 ? 'text-primary' : 'text-destructive'} />
                        {stockQty > 10 ? (
                            <span>Status: <span className="text-primary font-bold">In Stock ({stockQty})</span></span>
                        ) : stockQty > 0 ? (
                            <span>Status: <span className="text-amber-500 dark:text-amber-400 font-bold">Low Stock ({stockQty})</span></span>
                        ) : (
                            <span className="text-destructive font-bold">Out of Stock</span>
                        )}
                    </div>

                    {/* Quantity picker + Add to Cart */}
                    <div className="flex items-center gap-4 mt-2">
                        <div className="flex items-center border-2 border-border bg-background rounded-xl overflow-hidden shadow-sm h-14">
                            <button
                                disabled={stockQty === 0 || qty <= 1}
                                onClick={() => setQty(q => Math.max(1, q - 1))}
                                className="px-5 h-full text-muted-foreground hover:bg-muted hover:text-foreground font-bold text-xl transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                            >−</button>
                            <input
                                type="number"
                                min={1}
                                max={stockQty > 0 ? stockQty : 1}
                                disabled={stockQty === 0}
                                value={stockQty === 0 ? 0 : qty}
                                onChange={e => {
                                    const val = Number(e.target.value);
                                    setQty(val > stockQty ? stockQty : Math.max(1, val));
                                }}
                                className="w-16 h-full text-center bg-transparent outline-none text-foreground font-bold text-lg disabled:opacity-50"
                            />
                            <button
                                disabled={stockQty === 0 || qty >= stockQty}
                                onClick={() => setQty(q => q + 1)}
                                className="px-5 h-full text-muted-foreground hover:bg-muted hover:text-foreground font-bold text-xl transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                            >+</button>
                        </div>
                        <button
                            disabled={stockQty === 0}
                            onClick={handleAddToCart}
                            className={`flex-1 flex items-center justify-center gap-2 h-14 px-6 rounded-xl font-bold text-base transition-all shadow-md ${
                                added
                                    ? 'bg-primary/20 text-primary border-2 border-primary shadow-primary/20'
                                    : stockQty === 0
                                        ? 'bg-muted text-muted-foreground cursor-not-allowed opacity-70'
                                        : 'bg-primary text-primary-foreground hover:bg-accent hover:text-accent-foreground hover:shadow-primary/30 hover:-translate-y-0.5'
                            }`}
                        >
                            {added
                                ? <><CheckCircle2 size={20} className="animate-in zoom-in" /> Added to cart!</>
                                : <><ShoppingCart size={20} /> Add to Cart</>
                            }
                        </button>
                    </div>

                    {/* Description */}
                    <div className="bg-card rounded-2xl border border-border p-6 shadow-sm mt-auto">
                        <h3 className="text-foreground font-bold text-lg mb-3">Product Description</h3>
                        <p className="text-muted-foreground text-sm leading-relaxed">{product.description}</p>
                    </div>
                </div>
            </div>

            {/* ── Product info table — type-dispatch fully delegated ── */}
            <ProductInfoTable product={product} showManagerFields={false} />
        </div>
    );
}
