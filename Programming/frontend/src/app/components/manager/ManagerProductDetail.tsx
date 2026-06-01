import { useParams, useNavigate } from 'react-router';
import { useState, useEffect } from 'react';
import { ArrowLeft, Pencil, Trash2, Package, AlertTriangle, BookOpen, Disc, Tv, FileText, Loader2, CheckCircle } from 'lucide-react';
import { useProductManagement } from '../../hooks/useProductManagement';
import { formatVND, formatDate, formatDurationMinutes } from '../../data/mockData';
import type { ProductType, Book, CD, DVD, Newspaper } from '../../models/product.interface';
import AdjustStockModal from './AdjustStockModal';

const TYPE_COLORS: Record<string, string> = {
  BOOK: 'bg-primary/10 text-primary border-primary/20',
  CD: 'bg-secondary text-secondary-foreground border-border',
  DVD: 'bg-accent/50 text-accent-foreground border-accent',
  NEWSPAPER: 'bg-muted text-muted-foreground border-border',
};

function InfoRow({ label, value }: { label: string; value: string | number | boolean | undefined }) {
  if (value === undefined || value === '') return null;
  return (
      <div className="flex flex-col sm:flex-row sm:items-start justify-between py-3.5 border-b border-border/50 last:border-0 hover:bg-muted/30 transition-colors px-4 rounded-xl -mx-4 gap-1 sm:gap-4">
        <span className="text-muted-foreground text-sm font-medium shrink-0">{label}</span>
        <span className="text-foreground text-sm font-bold text-left sm:text-right break-words flex-1">
        {typeof value === 'boolean' ? (value ? '✓ Yes' : '✗ No') : value}
      </span>
      </div>
  );
}

export default function ManagerProductDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { getProduct, deleteProducts, activateProduct } = useProductManagement();

  const [product, setProduct] = useState<ProductType | null>(null);
  const [loading, setLoading] = useState(true);

  const [showAdjust, setShowAdjust] = useState(false);

  const [confirmDelete, setConfirmDelete] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const [confirmActivate, setConfirmActivate] = useState(false);
  const [isActivating, setIsActivating] = useState(false);

  useEffect(() => {
    if (id) {
      getProduct(id).then(data => {
        setProduct(data);
        setLoading(false);
      });
    }
  }, [id]);

  if (loading) {
    return <div className="py-32 flex justify-center"><Loader2 className="animate-spin text-primary" size={48} /></div>;
  }

  if (!product) {
    return (
        <div className="max-w-3xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
          <Package size={64} className="mx-auto mb-6 text-muted-foreground opacity-30" />
          <p className="text-xl text-foreground font-bold mb-2">Product Not Found</p>
          <p className="text-muted-foreground mb-6">This product does not exist or has been deleted from the system.</p>
          <button onClick={() => navigate('/manager')} className="text-primary font-semibold hover:text-accent-foreground underline underline-offset-4">
            Back to List
          </button>
        </div>
    );
  }

  const status = (product as any).status || 'ACTIVE';

  const handleDelete = async () => {
    setIsDeleting(true);
    await deleteProducts([product.id]);

    const updated = await getProduct(product.id);
    setProduct(updated);

    setConfirmDelete(false);
    setIsDeleting(false);
  };

  const handleActivate = async () => {
    setIsActivating(true);
    await activateProduct(product.id);

    const updated = await getProduct(product.id);
    setProduct(updated);

    setConfirmActivate(false);
    setIsActivating(false);
  };

  const pType = product.productType.toUpperCase();
  const typeLabel = { BOOK: 'Book', CD: 'CD', DVD: 'DVD', NEWSPAPER: 'Newspaper/Magazine' }[pType] || pType;
  const TypeIcon = { BOOK: BookOpen, CD: Disc, DVD: Tv, NEWSPAPER: FileText }[pType] || Package;

  const isOutOfStock = product.stockQuantity === 0;
  const isLowStock = product.stockQuantity <= 5 && product.stockQuantity > 0;

  const stockContainerClass = isOutOfStock ? 'bg-destructive/5 border-destructive/20' : isLowStock ? 'bg-amber-500/5 border-amber-500/20' : 'bg-primary/5 border-primary/20';
  const stockTextClass = isOutOfStock ? 'text-destructive' : isLowStock ? 'text-amber-500 dark:text-amber-400' : 'text-primary';

  const statusContainerClass = status === 'ACTIVE' ? 'bg-emerald-500/5 border-emerald-500/20' : status === 'DEACTIVATED' ? 'bg-amber-500/5 border-amber-500/20' : 'bg-destructive/5 border-destructive/20 bg-muted/50';
  const statusTextClass = status === 'ACTIVE' ? 'text-emerald-600' : status === 'DEACTIVATED' ? 'text-amber-600' : 'text-destructive';

  return (
      <div className="max-w-4xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500">

        {/* Header & Actions */}
        <div className="flex flex-col md:flex-row md:items-center gap-4 mb-8">
          <div className="flex items-center gap-4 flex-1 min-w-0">
            <button onClick={() => navigate('/manager')} className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0">
              <ArrowLeft size={20} />
            </button>
            <div className="min-w-0">
              <h2 className={`text-2xl font-bold tracking-tight truncate ${status === 'DELETED' ? 'text-muted-foreground line-through' : 'text-foreground'}`}>
                Product Details
              </h2>
              <p className="text-sm text-muted-foreground font-medium truncate mt-0.5">Barcode: <span className="font-mono">{product.barcode}</span></p>
            </div>
          </div>

          <div className="flex flex-wrap gap-2 md:gap-3 shrink-0">
            {status === 'DEACTIVATED' && (
                <button
                    onClick={() => setConfirmActivate(true)}
                    className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-emerald-500/30 text-emerald-600 font-semibold hover:bg-emerald-500/10 transition-colors shadow-sm bg-card"
                >
                  <CheckCircle size={16} /> Activate
                </button>
            )}

            <button
                onClick={() => setShowAdjust(true)}
                disabled={status === 'DELETED'}
                title={status === 'DELETED' ? "Cannot adjust stock of deleted product" : "Adjust Stock"}
                className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-primary/30 text-primary font-semibold hover:bg-primary/10 transition-colors shadow-sm bg-card disabled:opacity-30 disabled:cursor-not-allowed"
            >
              <Package size={16} /> Adjust Stock
            </button>

            <button
                onClick={() => navigate(`/manager/products/edit/${product.id}`)}
                disabled={status === 'DELETED' || status === 'DEACTIVATED'}
                title={status === 'DEACTIVATED' ? "Activate product to edit" : status === 'DELETED' ? "Cannot edit deleted product" : "Edit Product"}
                className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-amber-500/30 text-amber-500 dark:text-amber-400 font-semibold hover:bg-amber-500/10 transition-colors shadow-sm bg-card disabled:opacity-30 disabled:cursor-not-allowed"
            >
              <Pencil size={16} /> Edit
            </button>

            {status !== 'DEACTIVATED' && (
                <button
                    onClick={() => setConfirmDelete(true)}
                    disabled={status === 'DELETED'}
                    title={status === 'DELETED' ? "Product is already deleted" : "Delete Product"}
                    className="flex items-center gap-2 px-4 py-2.5 rounded-xl border border-destructive/30 text-destructive font-semibold hover:bg-destructive/10 transition-colors shadow-sm bg-card disabled:opacity-30 disabled:cursor-not-allowed"
                >
                  <Trash2 size={16} /> Delete
                </button>
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 lg:gap-8">
          {/* Left Column: Image & Stock */}
          <div className="lg:col-span-1 space-y-6">
            <div className={`bg-card rounded-3xl border border-border shadow-lg overflow-hidden group ${status === 'DELETED' ? 'opacity-50 grayscale' : ''}`}>
              <img src={product.imageURL || '/favicon.png'} alt={product.title} className="w-full object-cover aspect-square group-hover:scale-105 transition-transform duration-700 ease-out bg-muted" />
            </div>

            <div className={`p-6 rounded-3xl border shadow-sm ${stockContainerClass}`}>
              <div className="flex items-center gap-2 mb-2">
                {isLowStock && <AlertTriangle size={18} className={stockTextClass} />}
                <span className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Stock Quantity</span>
              </div>
              <p className={`text-4xl font-extrabold tracking-tight ${stockTextClass}`}>{product.stockQuantity}</p>
              <p className={`text-sm font-bold mt-1.5 ${stockTextClass} opacity-80`}>
                {isOutOfStock ? 'Out of Stock' : isLowStock ? 'Low Stock - Restock needed' : 'In Stock'}
              </p>
            </div>

            <div className={`p-6 rounded-3xl border shadow-sm ${statusContainerClass}`}>
              <div className="flex items-center gap-2 mb-2">
                <span className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Product Status</span>
              </div>
              <div className="flex items-center gap-2.5">
                {status === 'ACTIVE' && <CheckCircle size={24} className={statusTextClass} />}
                {status === 'DEACTIVATED' && <AlertTriangle size={24} className={statusTextClass} />}
                {status === 'DELETED' && <Trash2 size={24} className={statusTextClass} />}
                <p className={`text-2xl font-extrabold tracking-tight uppercase ${statusTextClass}`}>{status}</p>
              </div>
            </div>
          </div>

          {/* Right Column: Details */}
          <div className="lg:col-span-2 space-y-6">
            <div className={`bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8 relative overflow-hidden ${status === 'DELETED' ? 'opacity-70' : ''}`}>
              <div className="absolute top-0 right-0 w-48 h-48 bg-primary/5 rounded-full blur-3xl -mr-24 -mt-24 pointer-events-none"></div>
              <div className="relative z-10">
                <div className="flex items-center gap-2 mb-4">
                <span className={`flex items-center gap-1.5 text-xs font-bold px-3 py-1 rounded-md border shadow-sm ${TYPE_COLORS[pType] || TYPE_COLORS['BOOK']}`}>
                  <TypeIcon size={14} /> {typeLabel}
                </span>
                </div>
                <h1 className={`text-2xl sm:text-3xl font-extrabold tracking-tight leading-tight mb-4 ${status === 'DELETED' ? 'text-muted-foreground line-through' : 'text-foreground'}`}>
                  {product.title}
                </h1>

                <div className="bg-primary/5 border border-primary/10 rounded-2xl p-4 inline-block w-full sm:w-auto">
                  <p className="text-sm text-muted-foreground font-medium mb-1">Current Price</p>
                  <p className="text-3xl text-primary font-extrabold tracking-tight">{formatVND(product.currentPrice)}</p>
                </div>

                {product.description && (
                    <div className="mt-6 pt-6 border-t border-border/50">
                      <p className="text-sm text-muted-foreground font-medium mb-2">Product Description</p>
                      <p className="text-foreground text-sm leading-relaxed">{product.description}</p>
                    </div>
                )}
              </div>
            </div>

            <div className={`flex flex-col gap-6 ${status === 'DELETED' ? 'opacity-70' : ''}`}>
              <div className="bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8">
                <h3 className="text-foreground font-bold text-lg mb-4 flex items-center gap-2 border-b border-border/50 pb-4">
                  <Package size={20} className="text-primary" /> General Information
                </h3>
                <div className="space-y-1">
                  <InfoRow label="Category" value={product.category} />
                  <InfoRow label="Weight" value={`${product.weight}kg`} />
                  <InfoRow label="Dimensions" value={`${product.length}x${product.width}x${product.height} cm`} />
                  <InfoRow label="Updated At" value={product.updatedAt ? formatDate(product.updatedAt) : undefined} />
                </div>
              </div>

              <div className="bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8">
                <h3 className="text-foreground font-bold text-lg mb-4 flex items-center gap-2 border-b border-border/50 pb-4">
                  <FileText size={20} className="text-primary" /> Technical Information
                </h3>
                <div className="space-y-1">
                  {pType === 'BOOK' && (() => {
                    const b = product as Book;
                    return <>
                      <InfoRow label="Authors" value={b.authors?.join(', ')} />
                      <InfoRow label="Publisher" value={b.publisher} />
                      <InfoRow label="Publication Year" value={b.publicationDate ? formatDate(b.publicationDate) : undefined} />
                      <InfoRow label="Pages" value={b.numberOfPages} />
                      <InfoRow label="Language" value={b.language} />
                      <InfoRow label="Genre" value={b.genre} />
                      <InfoRow label="Cover Type" value={b.coverType} />
                    </>;
                  })()}
                  {pType === 'CD' && (() => {
                    const c = product as CD;
                    return <>
                      <InfoRow label="Artists" value={c.artists?.join(', ')} />
                      <InfoRow label="Record Label" value={c.recordLabel} />
                      <InfoRow label="Release Date" value={c.releaseDate ? formatDate(c.releaseDate) : undefined} />
                      <InfoRow label="Genre" value={c.genre} />
                      <InfoRow label="Number of Tracks" value={c.tracks?.length} />
                    </>;
                  })()}
                  {pType === 'DVD' && (() => {
                    const d = product as DVD;
                    return <>
                      <InfoRow label="Studio" value={d.studio} />
                      <InfoRow label="Director" value={d.director} />
                      <InfoRow label="Runtime" value={d.runtime ? formatDurationMinutes(d.runtime) : undefined} />
                      <InfoRow label="Release Date" value={d.releaseDate ? formatDate(d.releaseDate) : undefined} />
                      <InfoRow label="Language" value={d.language} />
                      <InfoRow label="Genre" value={d.genre} />
                      <InfoRow label="Disc Type" value={d.discType} />
                      <InfoRow label="Subtitles" value={d.subtitles?.join(', ')} />
                    </>;
                  })()}
                  {pType === 'NEWSPAPER' && (() => {
                    const n = product as Newspaper;
                    return <>
                      <InfoRow label="Publisher" value={n.publisher} />
                      <InfoRow label="Release Date" value={n.publicationDate ? formatDate(n.publicationDate) : undefined} />
                      <InfoRow label="Language" value={n.language} />
                      <InfoRow label="Issue Number" value={n.issueNumber} />
                      <InfoRow label="Frequency" value={n.publicationFrequency} />
                      <InfoRow label="ISSN" value={n.ISSN} />
                    </>;
                  })()}
                </div>
              </div>
            </div>
          </div>
        </div>

        {showAdjust && <AdjustStockModal product={product as any} onClose={() => { setShowAdjust(false); getProduct(id!).then(setProduct); }} />}

        {/* MODAL ACTIVATE */}
        {confirmActivate && (
            <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
              <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                <div className="flex items-center gap-4 mb-5">
                  <div className="p-3 bg-emerald-500/10 rounded-2xl"><CheckCircle size={24} className="text-emerald-600" /></div>
                  <h3 className="text-lg font-bold text-foreground">Activate Product</h3>
                </div>
                <p className="text-muted-foreground text-sm mb-8 leading-relaxed">
                  Are you sure you want to reactivate <span className="text-foreground font-bold">"{product.title}"</span>? This will allow you to edit the product again.
                </p>
                <div className="flex gap-3">
                  <button disabled={isActivating} onClick={() => setConfirmActivate(false)} className="flex-1 py-3 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors shadow-sm disabled:opacity-50">
                    Cancel
                  </button>
                  <button disabled={isActivating} onClick={handleActivate} className="flex-1 py-3 rounded-xl bg-emerald-600 text-white font-bold hover:opacity-90 flex justify-center shadow-lg disabled:opacity-70 disabled:cursor-not-allowed">
                    {isActivating ? <Loader2 className="animate-spin" size={20}/> : 'Activate'}
                  </button>
                </div>
              </div>
            </div>
        )}

        {/* MODAL DELETE */}
        {confirmDelete && (
            <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
              <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                <div className="flex items-center gap-4 mb-5">
                  <div className="p-3 bg-destructive/10 rounded-2xl"><Trash2 size={24} className="text-destructive" /></div>
                  <h3 className="text-lg font-bold text-foreground">Delete Product</h3>
                </div>
                <p className="text-muted-foreground text-sm mb-6 leading-relaxed">
                  Are you sure you want to delete <span className="text-foreground font-bold">"{product.title}"</span>?
                </p>

                <div className="mb-8 p-3 rounded-xl border flex gap-3 text-sm font-medium bg-muted/50 border-border">
                  <AlertTriangle size={18} className="shrink-0 text-amber-500 mt-0.5" />
                  <p>
                    {product.stockQuantity > 0
                        ? "Because this product still has stock, it will be marked as DEACTIVATED instead."
                        : "It will be marked as DELETED permanently."}
                  </p>
                </div>

                <div className="flex gap-3">
                  <button disabled={isDeleting} onClick={() => setConfirmDelete(false)} className="flex-1 py-3 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors shadow-sm disabled:opacity-50">
                    Cancel
                  </button>
                  <button disabled={isDeleting} onClick={handleDelete} className="flex-1 py-3 rounded-xl bg-destructive text-destructive-foreground font-bold hover:opacity-90 flex justify-center shadow-lg disabled:opacity-70 disabled:cursor-not-allowed">
                    {isDeleting ? <Loader2 className="animate-spin" size={20}/> : 'Confirm Delete'}
                  </button>
                </div>
              </div>
            </div>
        )}
      </div>
  );
}