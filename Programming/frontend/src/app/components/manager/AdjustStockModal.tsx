import { useState } from 'react';
import { X, Package, AlertCircle, Plus, Minus, CheckCircle2, Loader2 } from 'lucide-react';
import { useProductManagement } from '../../hooks/useProductManagement';
import type { ProductSummary } from '../../models/product.interface';

interface Props {
  product: ProductSummary;
  onClose: () => void;
}

export default function AdjustStockModal({ product, onClose }: Props) {
  const { adjustStock } = useProductManagement();
  const [delta, setDelta] = useState(0);
  const [reason, setReason] = useState('');
  const [errors, setErrors] = useState<{ delta?: string; reason?: string }>({});
  const [success, setSuccess] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const newStock = product.stockQuantity + delta;

  const validate = () => {
    const errs: typeof errors = {};
    if (delta === 0) errs.delta = 'Please enter adjustment quantity (not 0)';
    if (newStock < 0) errs.delta = `Stock cannot be negative (current: ${product.stockQuantity})`;
    if (!reason.trim()) errs.reason = 'Please enter adjustment reason';
    setErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;
    setIsSubmitting(true);

    const res = await adjustStock(product.id, newStock, reason) as any;
    setIsSubmitting(false);

    if (res && res.success) {
      setSuccess(true);
      setTimeout(() => {
        onClose();
      }, 1500);
    } else {
      setErrors({ reason: res?.error || 'Server error occurred. See Network tab.' });
    }
  };

  const previewState = newStock < 0 ? 'invalid' : newStock === 0 ? 'empty' : 'valid';
  const previewClasses = {
    invalid: 'bg-destructive/10 border-destructive/20 text-destructive',
    empty: 'bg-amber-500/10 border-amber-500/20 text-amber-500 dark:text-amber-400',
    valid: 'bg-primary/10 border-primary/20 text-primary'
  }[previewState];

  return (
      <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
        <div className="bg-card rounded-3xl w-full max-w-md shadow-2xl border border-border animate-in zoom-in-95 duration-200 overflow-hidden">
          <div className="flex items-center justify-between p-5 border-b border-border bg-muted/10">
            <div className="flex items-center gap-3">
              <div className="p-2.5 bg-primary/10 rounded-xl shadow-sm border border-primary/10">
                <Package size={20} className="text-primary" />
              </div>
              <div>
                <h3 className="font-bold text-foreground text-lg tracking-tight">Adjust Stock</h3>
                <p className="text-xs font-medium text-muted-foreground mt-0.5 line-clamp-1">{product.title}</p>
              </div>
            </div>
            <button onClick={onClose} disabled={isSubmitting} className="p-2 text-muted-foreground hover:text-foreground hover:bg-muted rounded-xl transition-colors disabled:opacity-50">
              <X size={20} />
            </button>
          </div>

          {success ? (
              <div className="p-10 text-center animate-in fade-in zoom-in duration-300">
                <div className="inline-flex items-center justify-center w-20 h-20 rounded-full bg-primary/10 border-2 border-primary/20 mb-4 shadow-inner">
                  <CheckCircle2 size={40} className="text-primary" />
                </div>
                <p className="text-foreground font-bold text-lg">Stock updated successfully!</p>
                <p className="text-sm font-medium text-muted-foreground mt-2">New stock recorded: <span className="text-primary font-bold text-base">{newStock}</span></p>
              </div>
          ) : (
              <div className="p-6 space-y-6">
                <div className="flex items-center justify-between bg-muted/30 border border-border/50 rounded-2xl p-4">
                  <span className="text-sm font-medium text-muted-foreground">Current Stock</span>
                  <span className="text-2xl font-extrabold text-foreground">{product.stockQuantity}</span>
                </div>

                <div>
                  <label className="flex text-sm font-bold text-foreground mb-1.5 items-center gap-1">
                    Change Quantity <span className="text-destructive">*</span>
                  </label>
                  <p className="text-xs font-medium text-muted-foreground mb-3">Enter positive number to increase, negative to decrease stock</p>

                  <div className="flex items-center gap-3">
                    <button onClick={() => { setDelta(d => d - 1); setErrors(p => ({ ...p, delta: '' })); }} className="w-12 h-12 rounded-xl bg-destructive/10 border border-destructive/20 text-destructive flex items-center justify-center font-bold">
                      <Minus size={20} />
                    </button>
                    <input
                        type="number"
                        onWheel={e => e.currentTarget.blur()} // CHỐNG LĂN CHUỘT
                        value={delta}
                        onChange={e => { setDelta(Number(e.target.value)); setErrors(p => ({ ...p, delta: '' })); }}
                        className={`flex-1 border rounded-xl px-4 py-3 text-center text-xl font-bold outline-none transition-all shadow-inner bg-input-background text-foreground ${errors.delta ? 'border-destructive/50 focus:border-destructive' : 'border-border focus:border-primary'}`}
                    />
                    <button onClick={() => { setDelta(d => d + 1); setErrors(p => ({ ...p, delta: '' })); }} className="w-12 h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-600 flex items-center justify-center font-bold">
                      <Plus size={20} />
                    </button>
                  </div>
                  {errors.delta && <p className="mt-2 text-xs font-semibold text-destructive flex items-center gap-1.5"><AlertCircle size={14} className="shrink-0" /> {errors.delta}</p>}
                </div>

                <div className={`flex items-center justify-between rounded-2xl p-4 border transition-colors duration-300 shadow-sm ${previewClasses}`}>
                  <span className="text-sm font-bold opacity-90">Expected Stock</span>
                  <span className="text-3xl font-extrabold tracking-tight">{newStock}</span>
                </div>

                <div>
                  <label className="flex text-sm font-bold text-foreground mb-2 items-center gap-1">
                    Adjustment Reason <span className="text-destructive">*</span>
                  </label>
                  <textarea
                      placeholder="E.g.: Regular inventory, damaged goods..."
                      value={reason}
                      onChange={e => { setReason(e.target.value); setErrors(p => ({ ...p, reason: '' })); }}
                      rows={3}
                      className={`w-full border rounded-xl px-4 py-3 text-sm font-medium outline-none resize-none bg-input-background text-foreground ${errors.reason ? 'border-destructive/50 focus:border-destructive' : 'border-border focus:border-primary'}`}
                  />
                  {errors.reason && <p className="mt-2 text-xs font-semibold text-destructive flex items-center gap-1.5"><AlertCircle size={14} className="shrink-0" /> {errors.reason}</p>}
                </div>

                <div className="flex gap-3 pt-2">
                  <button onClick={onClose} disabled={isSubmitting} className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted disabled:opacity-50">
                    Cancel
                  </button>
                  <button onClick={handleSubmit} disabled={isSubmitting} className="flex-1 py-3.5 rounded-xl bg-primary text-primary-foreground font-bold hover:bg-accent flex items-center justify-center gap-2 shadow-md disabled:opacity-70 disabled:cursor-not-allowed">
                    {isSubmitting && <Loader2 size={18} className="animate-spin" />}
                    {isSubmitting ? 'Processing...' : 'Confirm'}
                  </button>
                </div>
              </div>
          )}
        </div>
      </div>
  );
}