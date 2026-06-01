import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Plus, Search, Pencil, Trash2, Package, BookOpen, Disc, Tv, FileText, AlertTriangle, ChevronDown, Loader2, CheckCircle } from 'lucide-react';
import { useProductManagement } from '../../hooks/useProductManagement';
import type { ProductSummary } from '../../models/product.interface';
import { formatVND } from '../../data/mockData';
import AdjustStockModal from './AdjustStockModal';

const TYPE_LABELS: Record<string, string> = { BOOK: 'Book', CD: 'CD', DVD: 'DVD', NEWSPAPER: 'Newspaper' };
const TYPE_ICONS: Record<string, React.ElementType> = { BOOK: BookOpen, CD: Disc, DVD: Tv, NEWSPAPER: FileText };
const TYPE_COLORS: Record<string, string> = {
  BOOK: 'bg-primary/10 text-primary border border-primary/20',
  CD: 'bg-secondary text-secondary-foreground border border-border',
  DVD: 'bg-accent/50 text-accent-foreground border border-accent',
  NEWSPAPER: 'bg-muted text-muted-foreground border border-border',
};

export default function ManagerHomePage() {
  const navigate = useNavigate();
  const { products, loading, fetchProducts, deleteProducts, activateProduct } = useProductManagement();

  const [search, setSearch] = useState('');
  const [filterType, setFilterType] = useState<string>('ALL');
  const [filterStatus, setFilterStatus] = useState<string>('ALL');

  const [selectedIds, setSelectedIds] = useState<string[]>([]);

  const [adjustProduct, setAdjustProduct] = useState<ProductSummary | null>(null);
  const [confirmDelete, setConfirmDelete] = useState<ProductSummary[] | null>(null);
  const [confirmActivate, setConfirmActivate] = useState<ProductSummary | null>(null);

  const [actionError, setActionError] = useState<string | null>(null);

  useEffect(() => {
    fetchProducts().catch(console.error);
  }, [fetchProducts]);

  useEffect(() => { setSelectedIds([]); }, [filterType, filterStatus, search]);

  const filtered = products.filter(p => {
    const q = search.toLowerCase();
    const matchSearch = !q || p.title.toLowerCase().includes(q) || (p.creators && p.creators.join(' ').toLowerCase().includes(q));
    const matchType = filterType === 'ALL' || p.productType.toUpperCase() === filterType;
    const pStatus = (p as any).status || 'ACTIVE';
    const matchStatus = filterStatus === 'ALL' || pStatus === filterStatus;

    return matchSearch && matchType && matchStatus;
  });

  const selectableProducts = filtered.filter(p => ((p as any).status || 'ACTIVE') !== 'DELETED');
  const isAllSelected = selectableProducts.length > 0 && selectedIds.length === selectableProducts.length;

  const toggleSelectAll = () => {
    if (isAllSelected) setSelectedIds([]);
    else setSelectedIds(selectableProducts.map(p => p.id));
  };

  const toggleSelect = (id: string) => {
    setSelectedIds(prev => prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]);
  };

  const handleDeleteBatch = async () => {
    if (confirmDelete) {
      setActionError(null);
      const res = await deleteProducts(confirmDelete.map(p => p.id)) as any;

      if (res && res.success) {
        setConfirmDelete(null);
        setSelectedIds([]);
      } else {
        setActionError(res.error || "Cannot delete products due to server error.");
        setConfirmDelete(null);
      }
    }
  };

  const handleActivate = async (id: string) => {
    await activateProduct(id);
    setConfirmActivate(null);
  };

  const inputClass = "border border-border rounded-xl px-4 py-2.5 text-sm outline-none focus:border-primary focus:ring-1 focus:ring-primary/50 bg-input-background text-foreground transition-all";

  if (loading && products.length === 0) {
    return <div className="flex justify-center items-center h-64"><Loader2 className="animate-spin text-primary" size={48} /></div>;
  }

  return (
      <div className="animate-in fade-in duration-500">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
          <div>
            <h2 className="text-foreground text-2xl font-bold tracking-tight">Product catalog</h2>
            <p className="text-muted-foreground text-sm mt-1 font-medium">{products.length} products total</p>
          </div>

          <div className="flex gap-3">
            {selectedIds.length > 0 && (
                <button
                    onClick={() => setConfirmDelete(filtered.filter(p => selectedIds.includes(p.id)))}
                    disabled={selectedIds.length > 10}
                    className="flex items-center justify-center gap-2 bg-destructive text-destructive-foreground px-5 py-2.5 rounded-xl hover:opacity-90 font-bold shadow-sm disabled:opacity-50 disabled:cursor-not-allowed transition-all"
                >
                  <Trash2 size={18} /> Delete Selected ({selectedIds.length})
                </button>
            )}

            <button onClick={() => navigate('/manager/products/add')} className="flex items-center justify-center gap-2 bg-primary text-primary-foreground px-5 py-2.5 rounded-xl hover:bg-accent font-bold shadow-sm">
              <Plus size={18} /> Add new product
            </button>
          </div>
        </div>

        {}
        {actionError && (
            <div className="mb-6 p-4 bg-destructive/10 border border-destructive/20 text-destructive rounded-xl flex items-center gap-3 font-bold animate-in slide-in-from-top-2">
              <AlertTriangle size={20} className="shrink-0" />
              {actionError}
              <button onClick={() => setActionError(null)} className="ml-auto underline text-sm hover:text-destructive/80">Dismiss</button>
            </div>
        )}

        {}
        {selectedIds.length > 10 && (
            <div className="mb-6 p-4 bg-amber-500/10 border border-amber-500/20 text-amber-600 rounded-xl flex items-center gap-3 font-semibold animate-in slide-in-from-top-2">
              <AlertTriangle size={20} className="shrink-0" />
              You can only delete a maximum of 10 products per request. Please deselect some items.
            </div>
        )}

        <div className="flex flex-wrap gap-3 mb-6">
          <div className="flex-1 min-w-[240px] relative">
            <Search size={18} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground" />
            <input type="text" placeholder="Search by name, creator..." value={search} onChange={e => setSearch(e.target.value)} className={`w-full pl-10 pr-4 ${inputClass}`} />
          </div>
          <div className="relative min-w-[180px]">
            <select value={filterStatus} onChange={e => setFilterStatus(e.target.value)} className={`w-full appearance-none pr-10 cursor-pointer ${inputClass}`}>
              <option value="ALL">All Statuses</option>
              <option value="ACTIVE">Active</option>
              <option value="DEACTIVATED">Deactivated</option>
              <option value="DELETED">Deleted</option>
            </select>
            <ChevronDown size={16} className="absolute right-3.5 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none" />
          </div>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-8">
          {(['BOOK', 'CD', 'DVD', 'NEWSPAPER']).map(type => {
            const count = products.filter(p => p.productType.toUpperCase() === type).length;
            const Icon = TYPE_ICONS[type] || Package;
            const isActive = filterType === type;

            return (
                <button key={type} onClick={() => setFilterType(isActive ? 'ALL' : type)} className={`flex items-center gap-3.5 p-4 rounded-2xl border transition-all duration-200 text-left ${isActive ? 'border-primary bg-primary/10 shadow-sm shadow-primary/5' : 'border-border bg-card hover:border-primary/40 hover:bg-muted/30 shadow-sm'}`}>
                  <div className={`p-2.5 rounded-xl shadow-inner ${isActive ? 'bg-primary text-primary-foreground' : TYPE_COLORS[type]}`}>
                    <Icon size={18} />
                  </div>
                  <div>
                    <p className={`text-xs font-semibold mb-0.5 ${isActive ? 'text-primary' : 'text-muted-foreground'}`}>{TYPE_LABELS[type]}</p>
                    <p className="text-xl font-extrabold text-foreground tracking-tight">{count}</p>
                  </div>
                </button>
            );
          })}
        </div>

        <div className="bg-card rounded-2xl border border-border shadow-md overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
              <tr className="bg-muted/30 border-b border-border">
                <th className="px-5 py-4 w-12 text-center">
                  <input
                      type="checkbox"
                      checked={isAllSelected && selectableProducts.length > 0}
                      onChange={toggleSelectAll}
                      disabled={selectableProducts.length === 0}
                      className="w-4 h-4 rounded border-border text-primary focus:ring-primary cursor-pointer disabled:cursor-not-allowed"
                  />
                </th>
                <th className="text-left px-5 py-4 font-semibold text-muted-foreground uppercase text-xs whitespace-nowrap">Product</th>
                <th className="text-left px-5 py-4 font-semibold text-muted-foreground uppercase text-xs">Type</th>
                <th className="text-right px-5 py-4 font-semibold text-muted-foreground uppercase text-xs">Price</th>
                <th className="text-center px-5 py-4 font-semibold text-muted-foreground uppercase text-xs">Stock</th>
                <th className="text-center px-5 py-4 font-semibold text-muted-foreground uppercase text-xs">Status</th>
                <th className="text-right px-5 py-4 font-semibold text-muted-foreground uppercase text-xs">Actions</th>
              </tr>
              </thead>
              <tbody>
              {filtered.map(product => {
                const uType = product.productType.toUpperCase();
                const Icon = TYPE_ICONS[uType] || Package;
                const status = (product as any).status || 'ACTIVE';
                const isSelected = selectedIds.includes(product.id);

                return (
                    <tr key={product.id} className={`border-b border-border/50 hover:bg-muted/30 cursor-pointer transition-colors ${status === 'DELETED' ? 'opacity-60 bg-muted/10' : ''} ${isSelected ? 'bg-primary/5' : ''}`} onClick={() => navigate(`/manager/products/${product.id}`)}>
                      <td className="px-5 py-3.5 w-12 text-center" onClick={e => e.stopPropagation()}>
                        <input
                            type="checkbox"
                            checked={isSelected}
                            onChange={() => toggleSelect(product.id)}
                            disabled={status === 'DELETED'}
                            className="w-4 h-4 rounded border-border text-primary focus:ring-primary cursor-pointer disabled:cursor-not-allowed"
                        />
                      </td>
                      <td className="px-5 py-3.5 min-w-[250px]">
                        <div className="flex items-center gap-4">
                          <div className="w-12 h-12 rounded-xl border border-border bg-muted overflow-hidden shrink-0">
                            <img src={product.imageURL || '/favicon.png'} alt={product.title} className="w-full h-full object-cover" />
                          </div>
                          <div className="min-w-0">
                            <p className={`text-foreground font-bold line-clamp-1 mb-0.5 ${status === 'DELETED' ? 'line-through text-muted-foreground' : ''}`}>{product.title}</p>
                            <p className="text-xs text-muted-foreground font-mono truncate">{product.id.split('-')[0]}</p>
                          </div>
                        </div>
                      </td>
                      <td className="px-5 py-3.5">
                      <span className={`inline-flex items-center gap-1.5 text-xs px-2.5 py-1 rounded-md font-semibold ${TYPE_COLORS[uType] || TYPE_COLORS['BOOK']}`}>
                        <Icon size={12} /> {TYPE_LABELS[uType] || 'Product'}
                      </span>
                      </td>
                      <td className="px-5 py-3.5 text-right font-bold">{formatVND(product.currentPrice)}</td>
                      <td className="px-5 py-3.5 text-center">
                      <span className={`inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full font-bold border ${product.stockQuantity === 0 ? 'bg-destructive/10 text-destructive border-destructive/20' : 'bg-primary/10 text-primary border-primary/20'}`}>
                        {product.stockQuantity <= 5 && product.stockQuantity > 0 && <AlertTriangle size={12} />}
                        {product.stockQuantity === 0 ? 'Out of stock' : product.stockQuantity}
                      </span>
                      </td>
                      <td className="px-5 py-3.5 text-center">
                      <span className={`inline-flex items-center text-xs px-2.5 py-1 rounded-full font-bold border 
                        ${status === 'ACTIVE' ? 'bg-emerald-500/10 text-emerald-600 border-emerald-500/20' :
                          status === 'DEACTIVATED' ? 'bg-amber-500/10 text-amber-600 border-amber-500/20' :
                              'bg-destructive/10 text-destructive border-destructive/20'}`}>
                        {status}
                      </span>
                      </td>
                      <td className="px-5 py-3.5">
                        <div className="flex items-center justify-end gap-1.5" onClick={e => e.stopPropagation()}>

                          <button onClick={() => setAdjustProduct(product)} disabled={status === 'DELETED'} title="Adjust Stock" className="p-2 text-muted-foreground hover:text-primary rounded-lg transition-colors disabled:opacity-30 disabled:cursor-not-allowed"><Package size={16} /></button>

                          <button onClick={() => navigate(`/manager/products/edit/${product.id}`)} disabled={status === 'DELETED' || status === 'DEACTIVATED'} title="Edit Product" className="p-2 text-muted-foreground hover:text-amber-500 rounded-lg transition-colors disabled:opacity-30 disabled:cursor-not-allowed"><Pencil size={16} /></button>

                          {status === 'DEACTIVATED' ? (
                              <button onClick={() => setConfirmActivate(product)} title="Activate Product" className="p-2 text-amber-600 hover:text-emerald-600 hover:bg-emerald-500/10 rounded-lg transition-colors"><CheckCircle size={16} /></button>
                          ) : (
                              <button onClick={() => setConfirmDelete([product])} disabled={status === 'DELETED'} title="Delete Product" className="p-2 text-muted-foreground hover:text-destructive rounded-lg transition-colors disabled:opacity-30 disabled:cursor-not-allowed"><Trash2 size={16} /></button>
                          )}
                        </div>
                      </td>
                    </tr>
                );
              })}
              </tbody>
            </table>
          </div>
        </div>

        {adjustProduct && <AdjustStockModal product={adjustProduct} onClose={() => { setAdjustProduct(null); fetchProducts().catch(console.error); }} />}

        {confirmActivate && (
            <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in">
              <div className="bg-card rounded-3xl p-8 max-w-sm w-full shadow-2xl border">
                <div className="flex items-center gap-4 mb-5"><div className="p-3 bg-emerald-500/10 rounded-2xl"><CheckCircle size={24} className="text-emerald-600" /></div><h3 className="text-lg font-bold">Activate product</h3></div>
                <p className="text-muted-foreground text-sm mb-8">Are you sure you want to reactivate <span className="font-bold text-foreground">{confirmActivate.title}</span>?</p>
                <div className="flex gap-3">
                  <button onClick={() => setConfirmActivate(null)} className="flex-1 py-3 rounded-xl border bg-card font-semibold hover:bg-muted">Cancel</button>
                  <button onClick={() => void handleActivate(confirmActivate.id)} className="flex-1 py-3 rounded-xl bg-emerald-600 text-white font-bold hover:opacity-90">Activate</button>
                </div>
              </div>
            </div>
        )}

        {confirmDelete && confirmDelete.length > 0 && (
            <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in">
              <div className="bg-card rounded-3xl p-8 max-w-sm w-full shadow-2xl border">
                <div className="flex items-center gap-4 mb-5">
                  <div className="p-3 bg-destructive/10 rounded-2xl"><Trash2 size={24} className="text-destructive" /></div>
                  <h3 className="text-lg font-bold">Delete {confirmDelete.length > 1 ? 'Products' : 'Product'}</h3>
                </div>

                <p className="text-muted-foreground text-sm mb-6 leading-relaxed">
                  Are you sure you want to delete {confirmDelete.length > 1 ? <span className="font-bold text-foreground">{confirmDelete.length} selected products</span> : <span><span className="font-bold text-foreground">{confirmDelete[0]?.title}</span></span>}?
                </p>

                <div className="mb-8 p-3 rounded-xl border flex gap-3 text-sm font-medium bg-muted/50">
                  <AlertTriangle size={18} className="shrink-0 text-amber-500 mt-0.5" />
                  <p>Products with active stock (&gt;0) will be marked as DEACTIVATED instead. Out of stock products will be DELETED permanently.</p>
                </div>

                <div className="flex gap-3">
                  <button onClick={() => setConfirmDelete(null)} className="flex-1 py-3 rounded-xl border bg-card font-semibold hover:bg-muted">Cancel</button>
                  <button onClick={handleDeleteBatch} className="flex-1 py-3 rounded-xl bg-destructive text-white font-bold hover:opacity-90">Confirm</button>
                </div>
              </div>
            </div>
        )}
      </div>
  );
}