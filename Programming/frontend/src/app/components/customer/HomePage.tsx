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
    const { addToCart } = useCart();

    // Core Data State
    const [products, setProducts] = useState<ProductSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Split-Mode State
    const [isSearching, setIsSearching] = useState(false);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(false);
    const [loadingMore, setLoadingMore] = useState(false);

    // Filter State
    const [search, setSearch] = useState('');
    const [filterType, setFilterType] = useState<ProductTypeName | 'all'>('all');
    const [priceMin, setPriceMin] = useState('');
    const [priceMax, setPriceMax] = useState('');
    const [showFilter, setShowFilter] = useState(false);
    const [addedId, setAddedId] = useState<string | null>(null);

    // 1. DISCOVERY MODE (Initial Load)
    const loadDiscoveryMode = async () => {
        setLoading(true);
        try {
            const data = await HomepageService.get20RandomProducts();
            setProducts(data);
            setHasMore(false); // No pagination in random mode
        } catch (err) {
            console.error("Error fetching random products:", err);
            setError("Could not load products. Please try again later.");
        } finally {
            setLoading(false);
        }
    };

    // Trigger Discovery Mode when not searching
    useEffect(() => {
        if (!isSearching) {
            loadDiscoveryMode();
        }
    }, [isSearching]);

    // 2. SEARCH MODE (Hits the paginated backend)
    const executeSearch = async (pageNum: number) => {
        const isFirstPage = pageNum === 0;
        if (isFirstPage) setLoading(true);
        else setLoadingMore(true);

        try {
            const data = await HomepageService.filterProductsBy({
                page: pageNum,
                title: search.trim() || undefined,
                category: filterType !== 'all' ? filterType : undefined,
                minPrice: priceMin ? Number(priceMin) : undefined,
                maxPrice: priceMax ? Number(priceMax) : undefined,
            });

            if (isFirstPage) {
                setProducts(data);
            } else {
                setProducts(prev => [...prev, ...data]); // Append new page!
            }

            // If backend returned 20 items, there MIGHT be more. If less, we hit the end.
            setHasMore(data.length === 20);
        } catch (err) {
            console.error("Error executing search:", err);
            setError("Search failed. Please try again.");
        } finally {
            setLoading(false);
            setLoadingMore(false);
        }
    };

    // UI Event Handlers
    const handleSearchSubmit = () => {
        setIsSearching(true);
        setPage(0);
        executeSearch(0);
    };

    const handleClearSearch = () => {
        setSearch('');
        setFilterType('all');
        setPriceMin('');
        setPriceMax('');
        setIsSearching(false); // Triggers loadDiscoveryMode via useEffect
        setPage(0);
    };

    const handleLoadMore = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        executeSearch(nextPage);
    };

    const handleAddToCart = async (productId: string, quantity: number) => {
        try {
            await addToCart(productId, quantity);
            setAddedId(productId);
            setTimeout(() => setAddedId(null), 1500);
        } catch (err: any) {
            alert(err.message ?? "Failed to add item to cart.");
        }
    };

    if (loading && page === 0) {
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
                <button onClick={handleClearSearch} className="mt-4 text-primary hover:underline">Reset Store</button>
            </div>
        );
    }

    return (
        <div className="max-w-7xl mx-auto px-4 py-6">
            {/* Hero Banner (Same as before) */}
            <div className="bg-gradient-to-br from-primary to-accent/80 rounded-2xl p-8 mb-8 text-primary-foreground shadow-lg relative overflow-hidden">
                <div className="absolute top-0 right-0 -mr-20 -mt-20 w-64 h-64 rounded-full bg-white opacity-10 blur-3xl"></div>
                <div className="relative z-10">
                    <h1 className="text-3xl sm:text-4xl mb-3 font-bold drop-shadow-sm">Welcome to AIMS! 🎉</h1>
                    <p className="text-primary-foreground/80 mb-6 text-lg">Discover thousands of books, CDs, DVDs, and newspapers</p>
                    <div className="flex flex-col sm:flex-row gap-3 max-w-xl">
                        <div className="flex-1 relative">
                            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground" size={18}/>
                            <input
                                type="text"
                                placeholder="Search products..."
                                value={search}
                                onChange={e => setSearch(e.target.value)}
                                onKeyDown={e => e.key === 'Enter' && handleSearchSubmit()}
                                className="w-full bg-background text-foreground placeholder:text-muted-foreground pl-12 pr-4 py-3.5 rounded-xl outline-none focus:ring-2 focus:ring-primary/50 shadow-inner text-sm transition-all"
                            />
                        </div>
                        <button
                            onClick={handleSearchSubmit}
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
                            onClick={() => {
                                setFilterType(type as ProductTypeName | 'all');
                                // UX Bonus: Clicking a category automatically triggers a search!
                                setIsSearching(true);
                                setPage(0);
                                executeSearch(0);
                            }}
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
                    {isSearching && (
                        <button onClick={handleClearSearch} className="text-sm text-destructive hover:underline mr-4">
                            Clear Filters
                        </button>
                    )}

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
                <div className="bg-card border border-border rounded-xl p-5 mb-6 flex flex-wrap items-center gap-4 shadow-sm animate-in slide-in-from-top-2">
                    <span className="text-sm text-foreground font-medium">Price Range:</span>
                    <input
                        type="number"
                        placeholder="Min Price"
                        value={priceMin}
                        onChange={e => setPriceMin(e.target.value)}
                        className="bg-input-background border border-border text-foreground rounded-lg px-3 py-2 text-sm w-36 outline-none focus:border-primary focus:ring-1 focus:ring-primary"
                    />
                    <span className="text-muted-foreground">–</span>
                    <input
                        type="number"
                        placeholder="Max Price"
                        value={priceMax}
                        onChange={e => setPriceMax(e.target.value)}
                        className="bg-input-background border border-border text-foreground rounded-lg px-3 py-2 text-sm w-36 outline-none focus:border-primary focus:ring-1 focus:ring-primary"
                    />
                    <button
                        onClick={handleSearchSubmit}
                        className="text-sm bg-primary text-primary-foreground px-4 py-1.5 rounded-md font-medium"
                    >
                        Apply
                    </button>
                </div>
            )}

            {/* Title / Status */}
            <div className="flex items-center justify-between mb-4">
                <p className="text-sm text-muted-foreground">
                    {isSearching ? `Showing search results...` : `Showing 20 random products. Use search to find more.`}
                </p>
            </div>

            {/* Products Grid */}
            {products.length === 0 ? (
                <div className="text-center py-20 text-muted-foreground bg-card rounded-2xl border border-border border-dashed">
                    <Search size={48} className="mx-auto mb-4 opacity-30"/>
                    <p className="text-lg font-medium text-foreground">No matching products found</p>
                    <p className="text-sm mt-1">Try adjusting your search or filter criteria</p>
                </div>
            ) : (
                <>
                    <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-5 mb-8">
                        {products.map(product => (
                            <div key={product.id} className="relative">
                                <ProductCard product={product} onAddToCart={handleAddToCart}/>
                                {addedId === product.id && (
                                    <div className="absolute inset-0 bg-primary/20 backdrop-blur-sm rounded-2xl flex items-center justify-center z-10 animate-in fade-in zoom-in duration-200">
                                      <span className="bg-primary text-primary-foreground font-semibold text-sm px-4 py-2 rounded-full shadow-lg flex items-center gap-2">
                                        ✓ Added to Cart
                                      </span>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>

                    {/* Pagination: Load More Button */}
                    {hasMore && isSearching && (
                        <div className="flex justify-center mt-8 mb-12">
                            <button
                                onClick={handleLoadMore}
                                disabled={loadingMore}
                                className="px-8 py-3 rounded-xl border-2 border-primary text-primary font-bold hover:bg-primary/10 transition-colors flex items-center gap-2 disabled:opacity-50"
                            >
                                {loadingMore && <Loader2 size={18} className="animate-spin" />}
                                {loadingMore ? 'Loading...' : 'Load More Products'}
                            </button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
}