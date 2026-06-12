import { useState, useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router';
import { ArrowLeft, Save, AlertCircle, Package, FileText, LayoutGrid, Loader2 } from 'lucide-react';
import ProductManagementService from '../../api/ProductManagementService';
import { extractErrorMessage } from "./hooks/useProductManagement";
import { Field, inputClass } from './forms/FormField';
import { PRODUCT_TYPES, useAllFormSections } from './forms/ProductFormSectionRegistry';
import type { ProductTypeName } from '../../models/product.interface';

export default function ProductUpdate() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { getProductById, updateProduct } = ProductManagementService;

    // --- References for Auto-Scrolling ---
    const topRef = useRef<HTMLDivElement>(null);

    const sections = useAllFormSections();
    const [activeType, setActiveType] = useState<ProductTypeName>('Book');
    const activeSection = sections[activeType];

    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [originalData, setOriginalData] = useState<any>(null);

    // Fixed/Immutable fields
    const [barcode, setBarcode] = useState('');
    const [originalValue, setOriginalValue] = useState('');
    const [stockQuantity, setStockQuantity] = useState('');

    // Updatable fields
    const [title, setTitle] = useState('');
    const [category, setCategory] = useState('');
    const [currentPrice, setCurrentPrice] = useState('');
    const [imageURL, setImageURL] = useState('');
    const [description, setDescription] = useState('');
    const [weight, setWeight] = useState('');
    const [height, setHeight] = useState('');
    const [width, setWidth] = useState('');
    const [length, setLength] = useState('');

    const [submitError, setSubmitError] = useState<string | null>(null);

    // Auto-scroll to error banner when it appears
    useEffect(() => {
        if (submitError && topRef.current) {
            topRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }, [submitError]);

    useEffect(() => {
        if (!id) return;
        getProductById(id).then(data => {
            if (!data) { setLoading(false); return; }

            setOriginalData(data);

            const type = data.productType as ProductTypeName;
            setActiveType(type);

            // Immutable display values
            setBarcode(data.barcode);
            setOriginalValue(String(data.originalValue ?? data.currentPrice));
            setStockQuantity(String(data.stockQuantity));

            // Updatable base values
            setTitle(data.title);
            setCategory(data.category ?? '');
            setCurrentPrice(String(data.currentPrice));
            setImageURL(data.imageURL ?? '');
            setDescription(data.description ?? '');
            setWeight(String(data.weight ?? ''));
            setHeight(String(data.height ?? ''));
            setWidth(String(data.width ?? ''));
            setLength(String(data.length ?? ''));

            // Delegate type-specific population to the correct section
            sections[type].populateFromProduct(data);

            setLoading(false);
        });
    }, [id]);

    const handleSubmit = async () => {
        setSubmitError(null);
        setIsSubmitting(true);

        // PATCH semantics: only send fields that have a value.
        const payload: Record<string, any> = {};

        if (title !== originalData.title) payload.title = title;
        if (category !== (originalData.category ?? '')) payload.category = category;
        if (currentPrice && Number(currentPrice) !== originalData.currentPrice) payload.currentPrice = Number(currentPrice);
        if (imageURL !== (originalData.imageURL ?? '')) payload.imageURL = imageURL;
        if (description !== (originalData.description ?? '')) payload.description = description;
        if (weight && Number(weight) !== originalData.weight) payload.weight = Number(weight);
        if (height && Number(height) !== originalData.height) payload.height = Number(height);
        if (width && Number(width) !== originalData.width) payload.width = Number(width);
        if (length && Number(length) !== originalData.length) payload.length = Number(length);

        const finalPayload = {
            productType: activeType,
            ...payload,
            ...activeSection.buildUpdatePayload()
        };

        if (Object.keys(finalPayload).length === 0) {
            navigate(-1);
            return;
        }

        try {
            const result = await updateProduct(id!, finalPayload);
            navigate(`/manager/products/${result.id}`, {
                state: { product: result },
            });
        } catch (err) {
            setSubmitError(extractErrorMessage(err) || 'Failed to update product.');
        } finally {
            setIsSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="animate-spin text-primary mb-4" size={40} />
                <p className="text-muted-foreground font-medium">Loading product data...</p>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto animate-in fade-in pb-10" ref={topRef}>

            {/* ── Header ── */}
            <div className="flex items-center gap-4 mb-8 pt-4">
                <button
                    onClick={() => navigate(-1)}
                    disabled={isSubmitting}
                    className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm disabled:opacity-50"
                >
                    <ArrowLeft size={20} />
                </button>
                <div>
                    <h2 className="text-2xl font-extrabold tracking-tight">Edit Product</h2>
                    <p className="text-muted-foreground text-sm font-medium mt-1">Updating product with barcode <span className="font-mono text-foreground">{barcode}</span></p>
                </div>
            </div>

            {/* ── Error banner ── */}
            {submitError && (
                <div className="mb-6 p-4 bg-destructive/10 border border-destructive/20 text-destructive rounded-xl flex items-center gap-3 font-bold shadow-sm animate-in zoom-in-95 duration-300">
                    <AlertCircle size={20} className="shrink-0" />
                    <span>{submitError}</span>
                </div>
            )}

            <div className="bg-card rounded-3xl border border-border shadow-lg p-6 sm:p-10">

                {/* ── Product type selector (Disabled) ── */}
                <div className="mb-10">
                    <label className="flex items-center gap-2 text-base font-bold mb-4">
                        <LayoutGrid size={18} className="text-primary" /> Product Type
                    </label>
                    <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                        {PRODUCT_TYPES.map(t => (
                            <button
                                key={t}
                                type="button"
                                disabled
                                className={`py-3.5 px-4 rounded-xl border-2 text-sm font-bold transition-all duration-200 ${
                                    activeType === t
                                        ? 'border-primary bg-primary/10 text-primary shadow-sm'
                                        : 'border-border/50 bg-muted/20 text-muted-foreground/40 cursor-not-allowed'
                                }`}
                            >
                                {t}
                            </button>
                        ))}
                    </div>
                    <p className="text-xs text-muted-foreground mt-3 font-medium italic">
                        * Product type cannot be changed after creation.
                    </p>
                </div>

                {/* ── Base fields ── */}
                <div className="border-t border-border/50 pt-8 mb-8">
                    <h3 className="text-lg font-bold mb-6 flex items-center gap-2">
                        <Package size={20} className="text-primary" /> Basic Information
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-5">

                        {/* Title - Editable */}
                        <div className="sm:col-span-2">
                            <Field label="Title">
                                <input disabled={isSubmitting} value={title} onChange={e => setTitle(e.target.value)} className={inputClass()} />
                            </Field>
                        </div>

                        {/* Category - Editable */}
                        <Field label="Category">
                            <input disabled={isSubmitting} value={category} onChange={e => setCategory(e.target.value)} className={inputClass()} />
                        </Field>

                        {/* Prices & Stock */}
                        <Field label="Current Price (VND)">
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={currentPrice} onChange={e => setCurrentPrice(e.target.value)} className={inputClass()} />
                        </Field>

                        <Field label="Original Value">
                            <input disabled value={originalValue} className={`${inputClass()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                        </Field>

                        <Field label="Stock Quantity">
                            <div className="relative">
                                <input disabled value={stockQuantity} className={`${inputClass()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                                <span className="absolute right-4 top-1/2 -translate-y-1/2 text-[10px] uppercase font-bold text-muted-foreground">Use App to Adjust</span>
                            </div>
                        </Field>

                        {/* Physical Specs - Editable */}
                        <Field label="Weight (kg)">
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={weight} onChange={e => setWeight(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Height (cm)">
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={height} onChange={e => setHeight(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Width (cm)">
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={width} onChange={e => setWidth(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Length (cm)">
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={length} onChange={e => setLength(e.target.value)} className={inputClass()} />
                        </Field>

                        {/* ── Image & Description with Live Preview ── */}
                        <div className="sm:col-span-2 grid grid-cols-1 sm:grid-cols-5 gap-6 mt-4 pt-6 border-t border-border/50">

                            {/* Left Side: URL and Description Inputs */}
                            <div className="sm:col-span-3 flex flex-col gap-5">
                                <Field label="Image URL">
                                    <input
                                        disabled={isSubmitting}
                                        value={imageURL}
                                        onChange={e => setImageURL(e.target.value)}
                                        placeholder="https://example.com/image.jpg"
                                        className={inputClass()}
                                    />
                                </Field>

                                <Field label="Description">
                                    <textarea
                                        disabled={isSubmitting}
                                        value={description}
                                        onChange={e => setDescription(e.target.value)}
                                        rows={5}
                                        placeholder="Enter detailed product description..."
                                        className={`${inputClass()} resize-none`}
                                    />
                                </Field>
                            </div>

                            {/* Right Side: Live Image Preview */}
                            <div className="sm:col-span-2 flex flex-col">
                                <label className="text-sm font-bold text-foreground mb-2 flex items-center gap-1">
                                    Live Preview
                                </label>
                                <div className="flex-1 min-h-[200px] border-2 border-dashed border-border rounded-xl bg-muted/20 flex items-center justify-center overflow-hidden relative group">
                                    {imageURL ? (
                                        <img
                                            src={imageURL}
                                            alt="Preview"
                                            className="w-full h-full object-contain transition-opacity duration-300"
                                            onError={(e) => {
                                                e.currentTarget.style.display = 'none';
                                                if (e.currentTarget.nextElementSibling) {
                                                    (e.currentTarget.nextElementSibling as HTMLElement).style.display = 'flex';
                                                }
                                            }}
                                            onLoad={(e) => {
                                                e.currentTarget.style.display = 'block';
                                                if (e.currentTarget.nextElementSibling) {
                                                    (e.currentTarget.nextElementSibling as HTMLElement).style.display = 'none';
                                                }
                                            }}
                                        />
                                    ) : null}

                                    {/* Fallback Display */}
                                    <div className={`absolute inset-0 flex flex-col items-center justify-center text-muted-foreground ${imageURL ? 'hidden' : 'flex'}`}>
                                        <Package size={40} className="mb-2 opacity-20" />
                                        <span className="text-xs font-medium uppercase tracking-widest opacity-50">No Image</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* ── Type-specific fields ── */}
                <div className="border-t border-border/50 pt-8 mb-10">
                    <h3 className="text-lg font-bold mb-6 flex items-center gap-2">
                        <FileText size={20} className="text-primary" /> {activeType} Details
                    </h3>
                    <div className={isSubmitting ? 'opacity-60 pointer-events-none' : ''}>
                        {activeSection.renderEditFields(inputClass, Field, isSubmitting)}
                    </div>
                </div>

                {/* ── Actions ── */}
                <div className="flex flex-col-reverse sm:flex-row gap-4 pt-6 border-t border-border/50">
                    <button
                        onClick={() => navigate(-1)}
                        disabled={isSubmitting}
                        className="w-full sm:w-auto py-3.5 px-8 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm disabled:opacity-50"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleSubmit}
                        disabled={isSubmitting}
                        className="w-full py-3.5 px-8 rounded-xl bg-primary text-primary-foreground font-bold flex justify-center items-center gap-2 hover:bg-accent transition-all shadow-md disabled:opacity-70 disabled:cursor-not-allowed"
                    >
                        {isSubmitting ? (
                            <Loader2 size={18} className="animate-spin" />
                        ) : (
                            <Save size={18} />
                        )}
                        {isSubmitting ? 'Saving Changes...' : 'Save Changes'}
                    </button>
                </div>
            </div>
        </div>
    );
}