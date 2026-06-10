import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { ArrowLeft, Save, AlertCircle, Package, FileText, LayoutGrid, Loader2 } from 'lucide-react';
import ProductManagementService from '../../api/ProductManagementService';
import { extractErrorMessage } from "./hooks/useProductManagement";
import { Field, inputClass } from './forms/FormField';
import { PRODUCT_TYPES, useAllFormSections } from './forms/ProductFormSectionRegistry';
import type { ProductTypeName } from '../../models/product.interface';

export default function ProductCreation() {
    const navigate = useNavigate();
    const { createProduct } = ProductManagementService;

    // --- References for Auto-Scrolling ---
    const topRef = useRef<HTMLDivElement>(null);

    const [activeType, setActiveType] = useState<ProductTypeName>('Book');

    const sections = useAllFormSections();
    const activeSection = sections[activeType];

    const [title, setTitle] = useState('');
    const [barcode, setBarcode] = useState('');
    const [category, setCategory] = useState('');
    const [originalValue, setOriginalValue] = useState('');
    const [currentPrice, setCurrentPrice] = useState('');
    const [stockQuantity, setStockQuantity] = useState('');
    const [imageURL, setImageURL] = useState('');
    const [description, setDescription] = useState('');
    const [weight, setWeight] = useState('');
    const [height, setHeight] = useState('');
    const [width, setWidth] = useState('');
    const [length, setLength] = useState('');

    const [submitError, setSubmitError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false); // NEW: Loading state

    // NEW: React Lifecycle hook to smoothly scroll AFTER the DOM paints the error
    useEffect(() => {
        if (submitError && topRef.current) {
            topRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }, [submitError]);

    const validate = (): boolean => {
        const missing: string[] = [];
        if (!title.trim()) missing.push('Title');
        if (!barcode.trim()) missing.push('Barcode');
        if (!category.trim()) missing.push('Category');
        if (!originalValue || Number(originalValue) < 0) missing.push('Original Value');
        if (!currentPrice || Number(currentPrice) < 0) missing.push('Current Price');
        if (!stockQuantity || Number(stockQuantity) < 0) missing.push('Stock Quantity');
        if (!height || Number(height) <= 0) missing.push('Height');
        if (!width || Number(width) <= 0) missing.push('Width');
        if (!length || Number(length) <= 0) missing.push('Length');
        if (!weight || Number(weight) <= 0) missing.push('Weight');

        if (missing.length > 0) {
            setSubmitError(`Please fill in the required fields: ${missing.join(', ')}`);
            return false;
        }
        return true;
    };

    const handleSubmit = async () => {
        setSubmitError(null);
        if (!validate()) return;

        setIsSubmitting(true); // START LOADING

        const payload = {
            productType: activeSection.productType,
            title,
            barcode,
            category,
            originalValue: Number(originalValue),
            currentPrice: Number(currentPrice),
            stockQuantity: Number(stockQuantity),
            imageURL: imageURL || undefined,
            description: description || undefined,
            weight: Number(weight),
            height: Number(height),
            width: Number(width),
            length: Number(length),
            ...activeSection.buildCreatePayload(),
        };

        try {
            const result = await createProduct(payload);
            navigate(`/manager/products/${result.id}`, {
                state: { product: result },
            });
        } catch (err) {
            setSubmitError(extractErrorMessage(err) || 'Failed to create product.');
        } finally {
            setIsSubmitting(false); // STOP LOADING
        }
    };

    return (
        <div className="max-w-4xl mx-auto animate-in fade-in pb-10" ref={topRef}>
            {/* The ref above ensures we always scroll slightly above the header so it isn't cut off */}

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
                    <h2 className="text-2xl font-extrabold tracking-tight">Add New Product</h2>
                    <p className="text-muted-foreground text-sm font-medium mt-1">Fill in all required fields</p>
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

                {/* ── Product type selector ── */}
                <div className="mb-10">
                    <label className="flex items-center gap-2 text-base font-bold mb-4">
                        <LayoutGrid size={18} className="text-primary" /> Product Type
                    </label>
                    <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                        {PRODUCT_TYPES.map(t => (
                            <button
                                key={t}
                                type="button"
                                onClick={() => !isSubmitting && setActiveType(t)}
                                disabled={isSubmitting}
                                className={`py-3.5 px-4 rounded-xl border-2 text-sm font-bold transition-all duration-200 ${
                                    activeType === t
                                        ? 'border-primary bg-primary/10 text-primary shadow-sm'
                                        : 'border-border bg-input-background text-muted-foreground hover:border-primary/40 hover:bg-card'
                                } ${isSubmitting ? 'opacity-50 cursor-not-allowed' : ''}`}
                            >
                                {t}
                            </button>
                        ))}
                    </div>
                </div>

                {/* ── Base fields ── */}
                <div className="border-t border-border/50 pt-8 mb-8">
                    <h3 className="text-lg font-bold mb-6 flex items-center gap-2">
                        <Package size={20} className="text-primary" /> Basic Information
                    </h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-5">
                        <div className="sm:col-span-2">
                            <Field label="Title" required>
                                <input disabled={isSubmitting} value={title} onChange={e => setTitle(e.target.value)} className={inputClass()} />
                            </Field>
                        </div>
                        <Field label="Barcode" required>
                            <input disabled={isSubmitting} value={barcode} onChange={e => setBarcode(e.target.value)} className={`${inputClass()} font-mono`} />
                        </Field>
                        <Field label="Category" required>
                            <input disabled={isSubmitting} value={category} onChange={e => setCategory(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Original Value" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={originalValue}
                                   onChange={e => setOriginalValue(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Current Price" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={currentPrice}
                                   onChange={e => setCurrentPrice(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Stock Quantity" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={stockQuantity}
                                   onChange={e => setStockQuantity(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Weight (kg)" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={weight}
                                   onChange={e => setWeight(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Height (cm)" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={height}
                                   onChange={e => setHeight(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Width (cm)" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={width}
                                   onChange={e => setWidth(e.target.value)} className={inputClass()} />
                        </Field>
                        <Field label="Length (cm)" required>
                            <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={length}
                                   onChange={e => setLength(e.target.value)} className={inputClass()} />
                        </Field>
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
                                                // If the URL is invalid, hide the broken image icon
                                                e.currentTarget.style.display = 'none';
                                                // And show the fallback container
                                                if (e.currentTarget.nextElementSibling) {
                                                    (e.currentTarget.nextElementSibling as HTMLElement).style.display = 'flex';
                                                }
                                            }}
                                            onLoad={(e) => {
                                                // If image loads successfully, ensure the fallback is hidden
                                                e.currentTarget.style.display = 'block';
                                                if (e.currentTarget.nextElementSibling) {
                                                    (e.currentTarget.nextElementSibling as HTMLElement).style.display = 'none';
                                                }
                                            }}
                                        />
                                    ) : null}

                                    {/* Fallback Display (Shows when URL is empty or image fails to load) */}
                                    <div className={`absolute inset-0 flex flex-col items-center justify-center text-muted-foreground ${imageURL ? 'hidden' : 'flex'}`}>
                                        <Package size={40} className="mb-2 opacity-20" />
                                        <span className="text-xs font-medium uppercase tracking-widest opacity-50">No Image Provided</span>
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
                        {activeSection.renderCreateFields(inputClass, Field, isSubmitting)}
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
                        {isSubmitting ? 'Creating Product...' : 'Create Product'}
                    </button>
                </div>
            </div>
        </div>
    );
}