import { AlertCircle } from 'lucide-react';

export function Field({ label, required, error, children }: {
    label: string;
    required?: boolean;
    error?: string;
    children: React.ReactNode;
}) {
    return (
        <div className="flex flex-col">
            <label className="text-sm font-bold text-foreground mb-2 flex items-center gap-1">
                {label} {required && <span className="text-destructive">*</span>}
            </label>
            {children}
            {error && (
                <p className="mt-1.5 text-xs text-destructive font-semibold flex items-center gap-1.5">
                    <AlertCircle size={14} className="shrink-0" /> {error}
                </p>
            )}
        </div>
    );
}

export function inputClass(err?: string): string {
    return `w-full border rounded-xl px-4 py-3 text-sm outline-none transition-all duration-200 text-foreground ${
        err ? 'border-destructive/50 bg-destructive/5'
            : 'border-border bg-input-background focus:border-primary focus:ring-1 focus:ring-primary/50'
    }`;
}
