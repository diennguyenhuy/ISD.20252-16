import { useParams, useNavigate, useLocation } from 'react-router';
import { useState, useEffect } from 'react';
import {
    ArrowLeft, Package, Pencil, Trash2,
    AlertTriangle, Loader2, CheckCircle,
} from 'lucide-react';

import { useProductManagement } from './hooks/useProductManagement';
import { formatVND } from '../../data/formatter';
import type { Product } from '../../models/product.interface';

import { ProductInfoTable } from '../shared/product/ProductInfoTable';
import { ProductCreatorLine } from '../shared/product/ProductCreatorLine';
import { PRODUCT_TYPE_META } from '../shared/product/productTypeMetadata';
import AdjustStockModal from './AdjustStockModal';

export default function ManagerProductDetail() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const location = useLocation();
    const existingProduct = location.state?.product as Product;

    const { getProduct, deleteProducts, activateProduct } = useProductManagement();

    const [product, setProduct] = useState<Product | null>(existingProduct);
    const [loading, setLoading] = useState(!existingProduct);

    const [showAdjust, setShowAdjust] = useState(false);
    const [confirmDelete, setConfirmDelete] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);
    const [confirmActivate, setConfirmActivate] = useState(false);
    const [isActivating, setIsActivating] = useState(false);

    // ── Data fetching ────────────────────────────────────────────────────────
    useEffect(() => {
        if (!id) return;
        if (existingProduct) return;
        getProduct(id).then(data => {
            setProduct(data);
            setLoading(false);
        });
    }, [id]);

    // ── Action handlers ───────────────────────────────────────────────────────
    const handleDelete = async () => {
        if (!product) return;
        setIsDeleting(true);
        await deleteProducts([product.id]);
        const updated = await getProduct(product.id);
        setProduct(updated);
        setConfirmDelete(false);
        setIsDeleting(false);
    };

    const handleActivate = async () => {
        if (!product) return;
        setIsActivating(true);
        await activateProduct(product.id);
        const updated = await getProduct(product.id);
        setProduct(updated);
        setConfirmActivate(false);
        setIsActivating(false);
    };

    // ── Loading / error states ────────────────────────────────────────────────
    if (loading) {
        return (
            <div className="py-32 flex justify-center">
                <Loader2 className="animate-spin text-primary" size={48} />
            </div>
        );
    }

    if (!product) {
        return (
            <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <Package size={64} className="mx-auto mb-6 text-muted-foreground opacity-30" />
                <p className="text-xl text-foreground font-bold mb-2">Product Not Found</p>
                <p className="text-muted-foreground mb-6">
                    This product does not exist or has been deleted from the system.
                </p>
                <button
                    onClick={() => navigate('/manager')}
                    className="text-primary font-semibold hover:text-accent-foreground underline underline-offset-4"
                >
                    Back to List
                </button>
            </div>
        );
    }

    // ── Derived display values ────────────────────────────────────────────────
    const status = (product as any).status ?? 'ACTIVE';
    const { label, icon: TypeIcon, color } = PRODUCT_TYPE_META[product.productType];

    const isDeleted = status === 'DELETED';
    const isDeactivated = status === 'DEACTIVATED';
    const isOutOfStock = product.stockQuantity === 0;
    const isLowStock = product.stockQuantity > 0 && product.stockQuantity <= 5;

    const stockColor = isOutOfStock ? 'text-destructive' : isLowStock ? 'text-amber-500 dark:text-amber-400' : 'text-primary';
    const stockBg = isOutOfStock ? 'bg-destructive/5 border-destructive/20' : isLowStock ? 'bg-amber-500/5 border-amber-500/20' : 'bg-primary/5 border-primary/20';
    const statusColor = isDeleted ? 'text-destructive' : isDeactivated ? 'text-amber-600' : 'text-emerald-600';
    const statusBg = isDeleted ? 'bg-destructive/5 border-destructive/20' : isDeactivated ? 'bg-amber-500/5 border-amber-500/20' : 'bg-emerald-500/5 border-emerald-500/20';

    return (
        <div className="max-w-4xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500">

            {/* ── Page header & action buttons ── */}
            <div className="flex flex-col md:flex-row md:items-center gap-4 mb-8">
                <div className="flex items-center gap-4 flex-1 min-w-0">
                    <button
                        onClick={() => navigate('/manager')}
                        className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0"
                    >
                        <ArrowLeft size={20} />
                    </button>
                    <div className="min-w-0">
                        <h2 className={`text-2xl font-bold tracking-tight truncate ${isDeleted ? 'text-muted-foreground line-through' : 'text-foreground'}`}>
                            Product Details
                        </h2>
                        <p className="text-sm text-muted-foreground font-medium truncate mt-0.5">
                            Barcode: <span className="font-mono">{product.barcode}</span>
                        </p>
                    </div>
                </div>

                <div className="flex flex-wrap gap-2 md:gap-3 shrink-0">
                    {isDeactivated && (
                        <button
                            onClick={() => setConfirmActivate(true)}
                            className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-emerald-500/30 text-emerald-600 font-semibold hover:bg-emerald-500/10 transition-colors shadow-sm bg-card"
                        >
                            <CheckCircle size={16} /> Activate
                        </button>
                    )}
                    <button
                        onClick={() => setShowAdjust(true)}
                        disabled={isDeleted}
                        title={isDeleted ? 'Cannot adjust stock of a deleted product' : 'Adjust Stock'}
                        className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-primary/30 text-primary font-semibold hover:bg-primary/10 transition-colors shadow-sm bg-card disabled:opacity-30 disabled:cursor-not-allowed"
                    >
                        <Package size={16} /> Adjust Stock
                    </button>
                    <button
                        onClick={() => navigate(`/manager/products/edit/${product.id}`)}
                        disabled={isDeleted || isDeactivated}
                        title={isDeactivated ? 'Activate product to edit' : isDeleted ? 'Cannot edit a deleted product' : 'Edit Product'}
                        className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-amber-500/30 text-amber-500 dark:text-amber-400 font-semibold hover:bg-amber-500/10 transition-colors shadow-sm bg-card disabled:opacity-30 disabled:cursor-not-allowed"
                    >
                        <Pencil size={16} /> Edit
                    </button>
                    {!isDeactivated && (
                        <button
                            onClick={() => setConfirmDelete(true)}
                            disabled={isDeleted}
                            title={isDeleted ? 'Product is already deleted' : 'Delete Product'}
                            className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-destructive/30 text-destructive font-semibold hover:bg-destructive/10 transition-colors shadow-sm bg-card disabled:opacity-30 disabled:cursor-not-allowed"
                        >
                            <Trash2 size={16} /> Delete
                        </button>
                    )}
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 lg:gap-8">

                {/* ── Left column: image, stock, status ── */}
                <div className="lg:col-span-1 space-y-6">
                    <div
                        className={`bg-card rounded-3xl border border-border shadow-lg overflow-hidden group ${isDeleted ? 'opacity-50 grayscale' : ''}`}>
                        <img
                            src={product.imageURL || '/favicon.png'}
                            alt={product.title}
                            className="w-full object-cover aspect-square group-hover:scale-105 transition-transform duration-700 ease-out bg-muted"
                        />
                    </div>

                    {/* Stock panel */}
                    <div className={`p-6 rounded-3xl border shadow-sm ${stockBg}`}>
                        <div className="flex items-center gap-2 mb-2">
                            {isLowStock && <AlertTriangle size={18} className={stockColor} />}
                            <span className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                                Stock Quantity
                            </span>
                        </div>
                        <p className={`text-4xl font-extrabold tracking-tight ${stockColor}`}>
                            {product.stockQuantity}
                        </p>
                        <p className={`text-sm font-bold mt-1.5 ${stockColor} opacity-80`}>
                            {isOutOfStock ? 'Out of Stock' : isLowStock ? 'Low Stock — Restock needed' : 'In Stock'}
                        </p>
                    </div>

                    {/* Status panel */}
                    <div className={`p-6 rounded-3xl border shadow-sm ${statusBg}`}>
                        <span className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">
                            Product Status
                        </span>
                        <div className="flex items-center gap-2.5 mt-2">
                            {!isDeleted && !isDeactivated && <CheckCircle size={24} className={statusColor} />}
                            {isDeactivated && <AlertTriangle size={24} className={statusColor} />}
                            {isDeleted && <Trash2 size={24} className={statusColor} />}
                            <p className={`text-2xl font-extrabold tracking-tight uppercase ${statusColor}`}>
                                {status}
                            </p>
                        </div>
                    </div>
                </div>

                {/* ── Right column: title, price, description, type-specific info ── */}
                <div className="lg:col-span-2 space-y-6">
                    <div
                        className={`bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8 relative overflow-hidden ${isDeleted ? 'opacity-70' : ''}`}>
                        <div
                            className="absolute top-0 right-0 w-48 h-48 bg-primary/5 rounded-full blur-3xl -mr-24 -mt-24 pointer-events-none" />
                        <div className="relative z-10">
                            <div className="flex items-center gap-2 mb-4">
                                <span
                                    className={`flex items-center gap-1.5 text-xs font-bold px-3 py-1 rounded-md border shadow-sm ${color}`}>
                                    <TypeIcon size={14} /> {label}
                                </span>
                            </div>
                            <h1 className={`text-2xl sm:text-3xl font-extrabold tracking-tight leading-tight mb-2 ${isDeleted ? 'text-muted-foreground line-through' : 'text-foreground'}`}>
                                {product.title}
                            </h1>

                            {/* Creator attribution — type-dispatch fully delegated */}
                            <div className="mb-4">
                                <ProductCreatorLine product={product} />
                            </div>

                            <div
                                className="bg-primary/5 border border-primary/10 rounded-2xl p-4 inline-block w-full sm:w-auto">
                                <p className="text-sm text-muted-foreground font-medium mb-1">Current Price</p>
                                <p className="text-3xl text-primary font-extrabold tracking-tight">
                                    {formatVND(product.currentPrice)}
                                </p>
                            </div>

                            {product.description && (
                                <div className="mt-6 pt-6 border-t border-border/50">
                                    <p className="text-sm text-muted-foreground font-medium mb-2">
                                        Product Description
                                    </p>
                                    <p className="text-foreground text-sm leading-relaxed">{product.description}</p>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* ── Product info table — type-dispatch fully delegated ── */}
                    <div className={isDeleted ? 'opacity-70' : ''}>
                        <ProductInfoTable product={product} showManagerFields={true} />
                    </div>
                </div>
            </div>

            {/* ── Modals ── */}
            {showAdjust && (
                <AdjustStockModal
                    product={product as any}
                    onClose={() => {
                        setShowAdjust(false);
                        getProduct(id!).then(setProduct);
                    }}
                />
            )}

            {/* Activate confirmation */}
            {confirmActivate && (
                <div
                    className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div
                        className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <div className="flex items-center gap-4 mb-5">
                            <div className="p-3 bg-emerald-500/10 rounded-2xl">
                                <CheckCircle size={24} className="text-emerald-600" />
                            </div>
                            <h3 className="text-lg font-bold text-foreground">Activate Product</h3>
                        </div>
                        <p className="text-muted-foreground text-sm mb-8 leading-relaxed">
                            Are you sure you want to reactivate{' '}
                            <span className="text-foreground font-bold">"{product.title}"</span>?
                            This will allow you to edit the product again.
                        </p>
                        <div className="flex gap-3">
                            <button
                                disabled={isActivating}
                                onClick={() => setConfirmActivate(false)}
                                className="flex-1 py-3 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors shadow-sm disabled:opacity-50"
                            >
                                Cancel
                            </button>
                            <button
                                disabled={isActivating}
                                onClick={handleActivate}
                                className="flex-1 py-3 rounded-xl bg-emerald-600 text-white font-bold hover:opacity-90 flex justify-center shadow-lg disabled:opacity-70"
                            >
                                {isActivating ? <Loader2 className="animate-spin" size={20} /> : 'Activate'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Delete confirmation */}
            {confirmDelete && (
                <div
                    className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div
                        className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <div className="flex items-center gap-4 mb-5">
                            <div className="p-3 bg-destructive/10 rounded-2xl">
                                <Trash2 size={24} className="text-destructive" />
                            </div>
                            <h3 className="text-lg font-bold text-foreground">Delete Product</h3>
                        </div>
                        <p className="text-muted-foreground text-sm mb-4 leading-relaxed">
                            Are you sure you want to delete{' '}
                            <span className="text-foreground font-bold">"{product.title}"</span>?
                        </p>
                        <div
                            className="mb-8 p-3 rounded-xl border flex gap-3 text-sm font-medium bg-muted/50 border-border">
                            <AlertTriangle size={18} className="shrink-0 text-amber-500 mt-0.5" />
                            <p>
                                {product.stockQuantity > 0
                                    ? 'Because this product still has stock, it will be marked as DEACTIVATED instead.'
                                    : 'It will be marked as DELETED permanently.'}
                            </p>
                        </div>
                        <div className="flex gap-3">
                            <button
                                disabled={isDeleting}
                                onClick={() => setConfirmDelete(false)}
                                className="flex-1 py-3 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors shadow-sm disabled:opacity-50"
                            >
                                Cancel
                            </button>
                            <button
                                disabled={isDeleting}
                                onClick={handleDelete}
                                className="flex-1 py-3 rounded-xl bg-destructive text-destructive-foreground font-bold hover:opacity-90 flex justify-center shadow-lg disabled:opacity-70"
                            >
                                {isDeleting ? <Loader2 className="animate-spin" size={20} /> : 'Confirm Delete'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
