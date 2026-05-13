import {useState, useEffect} from 'react';
import {useParams, useNavigate} from 'react-router';
import {
    ArrowLeft, ShoppingCart, Package,
    BookOpen, Disc, Tv, FileText, ChevronRight, CheckCircle2, Loader2
} from 'lucide-react';

// Use standard services instead of AppContext
import HomepageService from '../../api/homepageService';
import {useCart} from "../../context/CartContext";
import {formatVND, formatDate, formatDurationMinutes} from '../../data/mockData';

// Import our strict interfaces
import type {
    Product,
    Book,
    CD,
    DVD,
    Newspaper,
    ProductTypeName
} from '../../models/product.interface';

const TYPE_LABELS: Record<ProductTypeName, string> = {
    Book: 'Book',
    CD: 'CD',
    DVD: 'DVD',
    Newspaper: 'Newspaper'
};

const TYPE_ICONS: Record<ProductTypeName, React.ElementType> = {
    Book: BookOpen,
    CD: Disc,
    DVD: Tv,
    Newspaper: FileText
};

const TYPE_COLORS: Record<ProductTypeName, string> = {
    Book: 'bg-primary/10 text-primary border-primary/20',
    CD: 'bg-secondary text-secondary-foreground border-border',
    DVD: 'bg-accent/50 text-accent-foreground border-accent',
    Newspaper: 'bg-muted text-muted-foreground border-border',
};

function InfoRow({label, value}: { label: string; value: string | number | undefined }) {
    if (value === undefined || value === '') return null; // Don't render empty rows
    return (
        <div
            className="flex py-3 border-b border-border last:border-0 hover:bg-muted/20 transition-colors px-2 rounded-lg">
            <span className="text-muted-foreground text-sm w-44 shrink-0 font-medium">{label}</span>
            <span className="text-foreground text-sm font-semibold">{value}</span>
        </div>
    );
}

export default function ProductDetail() {
    const {id} = useParams<{ id: string }>();
    const navigate = useNavigate();

    // Local State for standard fetching
    const [product, setProduct] = useState<Product | null>(null);
    const [loading, setLoading] = useState(true);
    const [qty, setQty] = useState(1);
    const [added, setAdded] = useState(false);

    const { addToCart } = useCart();

    // Fetch product data on mount
    useEffect(() => {
        if (!id) return;

        HomepageService.getProductDetail(id)
            .then(data => {
                setProduct(data as unknown as Product);
                setLoading(false);
            })
            .catch(err => {
                console.error("Failed to fetch product:", err);
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4"/>
                <p className="text-muted-foreground">Loading product details...</p>
            </div>
        );
    }

    if (!product) {
        return (
            <div className="max-w-7xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <p className="text-muted-foreground text-lg mb-4">Product not found or has been removed.</p>
                <button
                    onClick={() => navigate('/')}
                    className="text-primary font-semibold hover:text-accent-foreground transition-colors underline underline-offset-4"
                >
                    Back to Home
                </button>
            </div>
        );
    }

    const TypeIcon = TYPE_ICONS[product.productType];
    const stockQty = product.stockQuantity ?? 0;

    const handleAdd = async () => {
        try {
            await addToCart(product.id, qty);
            setAdded(true);
            setTimeout(() => setAdded(false), 2000);
        } catch (err) {
            console.error("Failed to add to cart:", err);
            alert("Could not add item to cart.");
        }
    };

    return (
        <div className="max-w-7xl mx-auto px-4 py-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
            {/* Breadcrumb */}
            <nav className="flex items-center gap-2 text-sm text-muted-foreground mb-8">
                <button onClick={() => navigate('/')}
                        className="hover:text-foreground transition-colors flex items-center gap-1">
                    <ArrowLeft size={16} className="mr-1"/>
                    Back to Home
                </button>
                <ChevronRight size={14} className="opacity-50"/>
                <span
                    className={`px-2.5 py-0.5 rounded-full text-xs font-semibold border ${TYPE_COLORS[product.productType]}`}>
          {TYPE_LABELS[product.productType]}
        </span>
                <ChevronRight size={14} className="opacity-50"/>
                <span className="text-foreground font-medium truncate max-w-[200px]">{product.title}</span>
            </nav>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
                {/* Left: Image */}
                <div className="space-y-4">
                    <div className="bg-card rounded-3xl overflow-hidden border border-border shadow-lg group relative">
                        <div
                            className="absolute inset-0 bg-gradient-to-tr from-primary/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none"></div>

                        <img
                            src={product.imageURL}
                            alt={product.title}
                            className="w-full object-cover group-hover:scale-105 transition-transform duration-700 ease-out"
                            style={{maxHeight: '500px'}}
                        />

                        {/* ADDED: Out of Stock Overlay matching the Homepage */}
                        {stockQty === 0 && (
                            <div
                                className="absolute inset-0 bg-black/60 backdrop-blur-[2px] flex items-center justify-center z-10">
                  <span className="bg-background text-foreground px-6 py-2 rounded-full text-lg font-bold shadow-lg">
                    Out of stock
                  </span>
                            </div>
                        )}
                    </div>
                </div>

                {/* Right: Details */}
                <div className="space-y-6 flex flex-col">
                    <div>
                        <div className="flex items-center gap-2 mb-3">
              <span
                  className={`flex items-center gap-1.5 text-xs font-bold px-3 py-1 rounded-full border shadow-sm ${TYPE_COLORS[product.productType]}`}>
                <TypeIcon size={14}/>
                  {TYPE_LABELS[product.productType]}
              </span>
                        </div>

                        <h1 className="text-3xl sm:text-4xl text-foreground font-bold leading-tight tracking-tight mb-4">
                            {product.title}
                        </h1>

                        <div className="space-y-1.5">
                            {product.productType === 'Book' && (
                                <p className="text-muted-foreground">Author: <strong
                                    className="text-foreground font-semibold">{(product as Book).authors.join(', ')}</strong>
                                </p>
                            )}
                            {product.productType === 'CD' && (
                                <p className="text-muted-foreground">Artist: <strong
                                    className="text-foreground font-semibold">{(product as CD).artists.join(', ')}</strong>
                                </p>
                            )}
                            {product.productType === 'DVD' && (
                                <p className="text-muted-foreground">Director: <strong
                                    className="text-foreground font-semibold">{(product as DVD).director}</strong></p>
                            )}
                            {product.productType === 'Newspaper' && (
                                <p className="text-muted-foreground">Editor in Chief: <strong
                                    className="text-foreground font-semibold">{(product as Newspaper).editorInChief}</strong>
                                </p>
                            )}
                        </div>
                    </div>

                    {/* Price */}
                    <div
                        className="bg-primary/5 border border-primary/20 rounded-2xl p-6 shadow-sm relative overflow-hidden">
                        <div
                            className="absolute top-0 right-0 w-32 h-32 bg-primary/10 rounded-full blur-3xl -mr-10 -mt-10"></div>

                        <p className="text-4xl text-primary font-extrabold tracking-tight relative z-10">{formatVND(product.currentPrice)}</p>

                        {/* Psychological Pricing Strikethrough */}
                        {product.originalValue && product.originalValue > product.currentPrice && (
                            <p className="text-muted-foreground line-through mt-1 text-sm font-medium relative z-10">
                                Original Price: {formatVND(product.originalValue)}
                            </p>
                        )}
                    </div>

                    {/* Stock */}
                    <div className="flex items-center gap-2 font-medium">
                        <Package size={18} className={stockQty > 0 ? 'text-primary' : 'text-destructive'}/>
                        {stockQty > 10 ? (
                            <span className="text-foreground">Status: <span className="text-primary font-bold">In Stock ({stockQty})</span></span>
                        ) : stockQty > 0 ? (
                            <span className="text-foreground">Status: <span
                                className="text-amber-500 dark:text-amber-400 font-bold">Low Stock ({stockQty})</span></span>
                        ) : (
                            <span className="text-destructive font-bold">Out of Stock</span>
                        )}
                    </div>

                    {/* Quantity + Add to cart */}
                    <div className="flex items-center gap-4 mt-2">
                        <div
                            className="flex items-center border-2 border-border bg-background rounded-xl overflow-hidden shadow-sm h-14">
                            <button
                                // FIXED: Disable if 0 stock OR if qty is already 1
                                disabled={stockQty === 0 || qty <= 1}
                                onClick={() => setQty(q => Math.max(1, q - 1))}
                                className="px-5 h-full text-muted-foreground hover:bg-muted hover:text-foreground font-bold text-xl transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                            >−
                            </button>
                            <input
                                type="number"
                                min={1}
                                max={stockQty > 0 ? stockQty : 1}
                                disabled={stockQty === 0}
                                value={stockQty === 0 ? 0 : qty}
                                onChange={e => {
                                    // FIXED: Input validation so user can't type 999 if stock is only 5
                                    const val = Number(e.target.value);
                                    if (val > stockQty) setQty(stockQty);
                                    else setQty(Math.max(1, val));
                                }}
                                className="w-16 h-full text-center bg-transparent outline-none text-foreground font-bold text-lg disabled:opacity-50"
                            />
                            <button
                                // FIXED: Disable if 0 stock OR if qty reaches max stock limit
                                disabled={stockQty === 0 || qty >= stockQty}
                                onClick={() => setQty(q => q + 1)}
                                className="px-5 h-full text-muted-foreground hover:bg-muted hover:text-foreground font-bold text-xl transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                            >+
                            </button>
                        </div>
                        <button
                            disabled={stockQty === 0}
                            onClick={handleAdd}
                            className={`flex-1 flex items-center justify-center gap-2 h-14 px-6 rounded-xl font-bold text-base transition-all shadow-md ${
                                added
                                    ? 'bg-primary/20 text-primary border-2 border-primary shadow-primary/20'
                                    : stockQty === 0
                                        ? 'bg-muted text-muted-foreground cursor-not-allowed opacity-70'
                                        : 'bg-primary text-primary-foreground hover:bg-accent hover:text-accent-foreground hover:shadow-primary/30 hover:-translate-y-0.5'
                            }`}
                        >
                            {added ? (
                                <><CheckCircle2 size={20} className="animate-in zoom-in"/> Added to cart!</>
                            ) : (
                                <><ShoppingCart size={20}/> Add to Cart</>
                            )}
                        </button>
                    </div>

                    {/* Description */}
                    <div className="bg-card rounded-2xl border border-border p-6 shadow-sm mt-auto">
                        <h3 className="text-foreground font-bold text-lg mb-3">Product Description</h3>
                        <p className="text-muted-foreground text-sm leading-relaxed">{product.description}</p>
                    </div>
                </div>
            </div>

            {/* Product Info Table */}
            <div className="mt-12 bg-card rounded-3xl border border-border shadow-sm overflow-hidden">
                <div className="px-8 py-5 border-b border-border bg-muted/30">
                    <h2 className="text-foreground font-bold text-xl">Product Information</h2>
                </div>
                <div className="p-8 grid grid-cols-1 md:grid-cols-2 gap-x-12 gap-y-2">
                    <div className="space-y-1">
                        <InfoRow label="Category" value={product.category}/>
                        <InfoRow label="Barcode" value={product.barcode}/>
                        <InfoRow label="Weight" value={`${product.weight} kg`}/>
                        <InfoRow label="Dimensions"
                                 value={`${product.length} x ${product.width} x ${product.height} cm`}/>
                        {/*<InfoRow label="Added At" value={product.addedAt ? formatDate(product.addedAt) : "-"} />*/}
                    </div>
                    <div className="space-y-1">

                        {product.productType === 'Book' && (() => {
                            const b = product as Book;
                            return <>
                                <InfoRow label="Publisher" value={b.publisher}/>
                                <InfoRow label="Publish Date" value={formatDate(b.publicationDate)}/>
                                <InfoRow label="Pages" value={b.numberOfPages}/>
                                <InfoRow label="Language" value={b.language}/>
                                <InfoRow label="Cover" value={b.coverType}/>
                                <InfoRow label="Genre" value={b.genre}/>
                            </>;
                        })()}

                        {product.productType === 'CD' && (() => {
                            const c = product as CD;
                            return <>
                                <InfoRow label="Record Label" value={c.recordLabel}/>
                                {c.releaseDate && <InfoRow label="Release Date" value={formatDate(c.releaseDate)}/>}
                                <InfoRow label="Genre" value={c.genre}/>
                                <InfoRow label="Tracks" value={c.tracks.map(t => t.title).join(', ')}/>
                            </>;
                        })()}

                        {product.productType === 'DVD' && (() => {
                            const d = product as DVD;
                            return <>
                                <InfoRow label="Studio" value={d.studio}/>
                                <InfoRow label="Runtime" value={formatDurationMinutes(d.runtime)}/>
                                <InfoRow label="Language" value={d.language}/>
                                <InfoRow label="Subtitles" value={d.subtitles.join(', ')}/>
                                <InfoRow label="Disc Type" value={d.discType}/>
                                {d.releaseDate && <InfoRow label="Release Date" value={formatDate(d.releaseDate)}/>}
                            </>;
                        })()}

                        {product.productType === 'Newspaper' && (() => {
                            const n = product as Newspaper;
                            return <>
                                <InfoRow label="Publisher" value={n.publisher}/>
                                <InfoRow label="Publish Date" value={formatDate(n.publicationDate)}/>
                                <InfoRow label="Language" value={n.language}/>
                                <InfoRow label="Issue Number" value={n.issueNumber}/>
                                <InfoRow label="Frequency" value={n.publicationFrequency}/>
                                <InfoRow label="ISSN" value={n.ISSN}/>
                            </>;
                        })()}
                    </div>
                </div>
            </div>
        </div>
    );
}