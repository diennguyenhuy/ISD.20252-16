import {Search, SlidersHorizontal, ShoppingCart, ChevronDown, X, Loader2} from 'lucide-react';
import {useState, useMemo, useEffect} from 'react';
import {useNavigate} from 'react-router';
import type {ProductSummary, ProductTypeName} from "../../models/product.interface";
import {formatVND} from '../../data/mockData';
import HomepageService from '../../api/homepageService';
import {useCart} from "../../context/CartContext";

const TYPE_LABELS: Record<ProductTypeName, string> = {
    Book: 'Book', CD: 'CD', DVD: 'DVD', Newspaper: 'Newspaper/Magazine',
};

const TYPE_COLORS: Record<ProductTypeName, string> = {
    Book: 'bg-primary/10 text-primary border border-primary/20',
    CD: 'bg-secondary text-secondary-foreground border border-border',
    DVD: 'bg-accent/50 text-accent-foreground border border-accent',
    Newspaper: 'bg-muted text-muted-foreground border border-border',
};

function ProductCard({product, onAddToCart}: {
    product: ProductSummary;
    onAddToCart: (productId: string, quantity: number) => void
}) {
    const navigate = useNavigate();
    const [qty, setQty] = useState(1);

    return (
        <div
            className="bg-card text-card-foreground rounded-2xl shadow-sm border border-border hover:shadow-md hover:border-primary/50 transition-all duration-200 flex flex-col overflow-hidden group">
            <div
                className="relative overflow-hidden cursor-pointer bg-muted"
                style={{aspectRatio: '4/3'}}
                onClick={() => navigate(`/product/${product.id}`)}
            >
                <img
                    src={product.imageURL}
                    alt={product.title}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                />
                <span
                    className={`absolute top-2 left-2 text-xs px-2 py-1 rounded-full font-medium backdrop-blur-sm ${TYPE_COLORS[product.productType]}`}>
            {TYPE_LABELS[product.productType]}
          </span>

                {/* ADDED: Low Stock Warning */}
                {product.stockQuantity <= 5 && product.stockQuantity > 0 && (
                    <span
                        className="absolute top-2 right-2 bg-destructive text-destructive-foreground text-xs px-2 py-1 rounded-full shadow-md animate-in zoom-in">
              {product.stockQuantity} left
            </span>
                )}

                {/* ADDED: Out of Stock Overlay */}
                {product.stockQuantity === 0 && (
                    <div
                        className="absolute inset-0 bg-black/60 backdrop-blur-[2px] flex items-center justify-center z-10">
              <span className="bg-background text-foreground px-4 py-1.5 rounded-full text-sm font-bold shadow-lg">
                Out of stock
              </span>
                    </div>
                )}
            </div>

            <div className="p-4 flex flex-col flex-1 relative z-0">
                <h3
                    className="text-foreground text-sm line-clamp-2 cursor-pointer hover:text-primary transition-colors mb-1 flex-1 font-semibold"
                    onClick={() => navigate(`/product/${product.id}`)}
                >
                    {product.title}
                </h3>
                <p className="text-muted-foreground text-xs mb-2">
                    {product.creators?.length > 0 ? product.creators.join(', ') : 'Unknown'}
                </p>

                <div className="flex items-center justify-between mt-auto pt-3 border-t border-border">
                    <div className="flex items-end gap-2">
                        <p className="text-primary font-bold text-sm">{formatVND(product.currentPrice)}</p>

                        {/* ADDED: Psychological Pricing (Strikethrough) */}
                        {product.originalValue && product.originalValue > product.currentPrice && (
                            <p className="text-muted-foreground text-xs line-through mb-[1px]">
                                {formatVND(product.originalValue)}
                            </p>
                        )}
                    </div>
                </div>

                <div className="mt-3 flex gap-2">
                    <div className="flex items-center border border-border rounded-lg overflow-hidden bg-background">
                        <button
                            disabled={product.stockQuantity === 0}
                            onClick={() => setQty(q => Math.max(1, q - 1))}
                            className="px-2 py-1.5 text-muted-foreground hover:bg-muted hover:text-foreground text-sm transition-colors disabled:opacity-50"
                        >−
                        </button>
                        <span
                            className="px-2 py-1.5 text-foreground font-medium text-sm min-w-[28px] text-center">{qty}</span>
                        <button
                            // ADDED: Prevent user from clicking "+" if they reach max stock
                            disabled={product.stockQuantity === 0 || qty >= product.stockQuantity}
                            onClick={() => setQty(q => q + 1)}
                            className="px-2 py-1.5 text-muted-foreground hover:bg-muted hover:text-foreground text-sm transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                        >+
                        </button>
                    </div>
                    <button
                        // ADDED: Disable Add To Cart button
                        disabled={product.stockQuantity === 0}
                        onClick={() => onAddToCart(product.id, qty)}
                        className="flex-1 flex items-center justify-center gap-1.5 bg-primary text-primary-foreground py-1.5 rounded-lg text-sm font-semibold hover:bg-accent hover:text-accent-foreground disabled:bg-muted disabled:text-muted-foreground disabled:cursor-not-allowed transition-colors"
                    >
                        <ShoppingCart size={14}/>
                        Add
                    </button>
                </div>
            </div>
        </div>
    );
}

export default function HomePage() {
    const [products, setProducts] = useState<ProductSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [search, setSearch] = useState('');
    const [filterType, setFilterType] = useState<ProductTypeName | 'all'>('all');
    const [sortBy, setSortBy] = useState<'default' | 'price_asc' | 'price_desc' | 'name'>('default');
    const [priceMin, setPriceMin] = useState('');
    const [priceMax, setPriceMax] = useState('');
    const [showFilter, setShowFilter] = useState(false);
    const [addedId, setAddedId] = useState<string | null>(null);

    const { addToCart } = useCart();

    useEffect(() => {
        HomepageService.getProductList()
            .then(data => {
                setProducts(data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Error fetching products:", err);
                setError("Could not load products. Please try again later.");
                setLoading(false);
            });
    }, []);

    const randomProducts = useMemo(() => {
        const shuffled = [...products].sort(() => Math.random() - 0.5);
        return shuffled.slice(0, 20);
    }, [products]);

    const [displayProducts, setDisplayProducts] = useState<ProductSummary[]>([]);

    useEffect(() => {
        if (search.trim() || filterType !== 'all' || priceMin || priceMax) {
            setDisplayProducts(products);
        } else {
            setDisplayProducts(randomProducts);
        }
    }, [search, filterType, priceMin, priceMax, products, randomProducts]);

    const filtered = useMemo(() => {
        let list = [...displayProducts];

        if (search.trim()) {
            const q = search.toLowerCase();
            list = list.filter(p =>
                p.title.toLowerCase().includes(q) ||
                (p.creators && p.creators.some(c => c.toLowerCase().includes(q)))
            );
        }

        if (filterType !== 'all') list = list.filter(p => p.productType === filterType);
        if (priceMin) list = list.filter(p => p.currentPrice >= Number(priceMin));
        if (priceMax) list = list.filter(p => p.currentPrice <= Number(priceMax));

        switch (sortBy) {
            case 'price_asc':
                return list.sort((a, b) => a.currentPrice - b.currentPrice);
            case 'price_desc':
                return list.sort((a, b) => b.currentPrice - a.currentPrice);
            case 'name':
                return list.sort((a, b) => a.title.localeCompare(b.title));
            default:
                return list;
        }
    }, [displayProducts, search, filterType, sortBy, priceMin, priceMax]);

    const handleSearch = () => {
        if (!search.trim() && filterType === 'all' && !priceMin && !priceMax) {
            setDisplayProducts(randomProducts);
        } else {
            setDisplayProducts(products);
        }
    };

    const handleAddToCart = async (productId: string, quantity: number) => {
        try {
            await addToCart(productId, quantity);
            setAddedId(productId);
            setTimeout(() => setAddedId(null), 1500);
        } catch (err: any) {
            console.error("Failed to add to cart:", err);
            alert(err.message ?? "Failed to add item to cart. Please make sure the backend is running.");
        }
    };

    if (loading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[60vh]">
                <Loader2 className="w-10 h-10 animate-spin text-primary mb-4"/>
                <p className="text-muted-foreground font-medium">Loading store inventory...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[60vh] text-destructive">
                <X className="w-12 h-12 mb-4"/>
                <p className="font-medium text-lg">{error}</p>
            </div>
        );
    }

    return (
        <div className="max-w-7xl mx-auto px-4 py-6">
            {/* Hero Banner */}
            <div
                className="bg-gradient-to-br from-primary to-accent/80 rounded-2xl p-8 mb-8 text-primary-foreground shadow-lg relative overflow-hidden">
                <div
                    className="absolute top-0 right-0 -mr-20 -mt-20 w-64 h-64 rounded-full bg-white opacity-10 blur-3xl"></div>

                <div className="relative z-10">
                    <h1 className="text-3xl sm:text-4xl mb-3 font-bold drop-shadow-sm">Welcome to AIMS! 🎉</h1>
                    <p className="text-primary-foreground/80 mb-6 text-lg">Discover thousands of books, CDs, DVDs, and
                        newspapers</p>
                    <div className="flex flex-col sm:flex-row gap-3 max-w-xl">
                        <div className="flex-1 relative">
                            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground"
                                    size={18}/>
                            <input
                                type="text"
                                placeholder="Search products..."
                                value={search}
                                onChange={e => setSearch(e.target.value)}
                                onKeyDown={e => e.key === 'Enter' && handleSearch()}
                                className="w-full bg-background text-foreground placeholder:text-muted-foreground pl-12 pr-4 py-3.5 rounded-xl outline-none focus:ring-2 focus:ring-primary/50 shadow-inner text-sm transition-all"
                            />
                        </div>
                        <button
                            onClick={handleSearch}
                            className="bg-foreground text-background px-8 py-3.5 rounded-xl font-bold text-sm hover:opacity-90 transition-opacity shadow-md"
                        >
                            Search
                        </button>
                    </div>
                </div>
            </div>

            {/* Filters */}
            <div className="flex flex-wrap items-center gap-3 mb-6">
                <div className="flex flex-wrap gap-2">
                    {(['all', 'Book', 'CD', 'DVD', 'Newspaper'] as const).map(type => (
                        <button
                            key={type}
                            onClick={() => setFilterType(type as ProductTypeName | 'all')}
                            className={`px-5 py-2 rounded-full text-sm font-medium transition-all ${
                                filterType === type
                                    ? 'bg-primary text-primary-foreground shadow-md'
                                    : 'bg-card text-foreground border border-border hover:border-primary/50 hover:text-primary'
                            }`}
                        >
                            {type === 'all' ? 'All' : TYPE_LABELS[type as ProductTypeName]}
                        </button>
                    ))}
                </div>

                <div className="flex items-center gap-2 ml-auto">
                    {/* Sort */}
                    <div className="relative">
                        <select
                            value={sortBy}
                            onChange={e => setSortBy(e.target.value as typeof sortBy)}
                            className="appearance-none bg-card border border-border text-foreground text-sm pl-4 pr-10 py-2.5 rounded-xl cursor-pointer outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all hover:border-primary/50"
                        >
                            <option value="default">Default</option>
                            <option value="price_asc">Price: Low to High</option>
                            <option value="price_desc">Price: High to Low</option>
                            <option value="name">Name: A to Z</option>
                        </select>
                        <ChevronDown size={14}
                                     className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none"/>
                    </div>

                    {/* Price filter toggle */}
                    <button
                        onClick={() => setShowFilter(o => !o)}
                        className={`flex items-center gap-2 px-4 py-2.5 rounded-xl border text-sm font-medium transition-all ${
                            showFilter || priceMin || priceMax
                                ? 'border-primary text-primary bg-primary/10'
                                : 'border-border text-muted-foreground bg-card hover:border-primary/50 hover:text-foreground'
                        }`}
                    >
                        <SlidersHorizontal size={16}/>
                        Filter Prices
                    </button>
                </div>
            </div>

            {/* Price filter panel */}
            {showFilter && (
                <div
                    className="bg-card border border-border rounded-xl p-5 mb-6 flex flex-wrap items-center gap-4 shadow-sm animate-in slide-in-from-top-2">
                    <span className="text-sm text-foreground font-medium">Price Range:</span>
                    <input
                        type="number"
                        placeholder="Minimum Price"
                        value={priceMin}
                        onChange={e => setPriceMin(e.target.value)}
                        className="bg-input-background border border-border text-foreground rounded-lg px-3 py-2 text-sm w-36 outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all"
                    />
                    <span className="text-muted-foreground">–</span>
                    <input
                        type="number"
                        placeholder="Maximum Price"
                        value={priceMax}
                        onChange={e => setPriceMax(e.target.value)}
                        className="bg-input-background border border-border text-foreground rounded-lg px-3 py-2 text-sm w-36 outline-none focus:border-primary focus:ring-1 focus:ring-primary transition-all"
                    />
                    <button
                        onClick={() => {
                            setPriceMin('');
                            setPriceMax('');
                        }}
                        className="text-sm text-destructive hover:text-destructive-foreground hover:bg-destructive/10 px-3 py-1.5 rounded-md flex items-center gap-1 transition-colors"
                    >
                        <X size={14}/> Clear Filters
                    </button>
                </div>
            )}

            {/* Products Grid */}
            <div className="flex items-center justify-between mb-4">
                <p className="text-sm text-muted-foreground">
                    Showing <strong className="text-foreground">{filtered.length}</strong> products
                </p>
            </div>

            {filtered.length === 0 ? (
                <div
                    className="text-center py-20 text-muted-foreground bg-card rounded-2xl border border-border border-dashed">
                    <Search size={48} className="mx-auto mb-4 opacity-30"/>
                    <p className="text-lg font-medium text-foreground">No matching products found</p>
                    <p className="text-sm mt-1">Try adjusting your search or filter criteria</p>
                </div>
            ) : (
                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-5">
                    {filtered.map(product => (
                        <div key={product.id} className="relative">
                            <ProductCard product={product} onAddToCart={handleAddToCart}/>
                            {addedId === product.id && (
                                <div
                                    className="absolute inset-0 bg-primary/20 backdrop-blur-sm rounded-2xl flex items-center justify-center z-10 animate-in fade-in zoom-in duration-200">
                          <span
                              className="bg-primary text-primary-foreground font-semibold text-sm px-4 py-2 rounded-full shadow-lg flex items-center gap-2">
                            ✓ Added to Cart
                          </span>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}