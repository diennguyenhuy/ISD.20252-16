import { FileText, Package } from 'lucide-react';
import type { Product } from "../../../models/product.interface";
import { productFieldRendererRegistry } from './ProductFieldRendererRegistry';
import { InfoRow } from './InfoRow';
import {formatDateTime, formatVND} from "../../../data/formatter";

interface ProductInfoTableProps {
    product: Product;
    showManagerFields?: boolean;
}

export function ProductInfoTable({ product, showManagerFields = false }: ProductInfoTableProps) {
    return (
        <div className="mt-12 bg-card rounded-3xl border border-border shadow-sm overflow-hidden">

            {/* ── General fields (shared by all product types) ── */}
            <div className="bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8">
                <h3 className="text-foreground font-bold text-lg mb-4 flex items-center gap-2 border-b border-border/50 pb-4">
                    <Package size={20} className="text-primary" />
                    General Information
                </h3>
                <div className="space-y-1">
                    <InfoRow label="Category"   value={product.category} />
                    <InfoRow label="Barcode"     value={product.barcode} />
                    <InfoRow label="Weight"      value={`${product.weight} kg`} />
                    <InfoRow label="Dimensions"  value={`${product.length} × ${product.width} × ${product.height} cm`} />
                    <InfoRow label="Original value" value={formatVND(product.originalValue)}/>
                    {showManagerFields && (
                        <>
                            <InfoRow
                                label="Created At"
                                value={product.createdAt ? formatDateTime(product.createdAt) : undefined}
                            />
                            <InfoRow
                                label="Updated At"
                                value={product.updatedAt ? formatDateTime(product.updatedAt) : undefined}
                            />
                        </>
                    )}
                </div>
            </div>

            {/* ── Type-specific fields — fully delegated to the registry ── */}
            <div className="bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8 mt-6">
                <h3 className="text-foreground font-bold text-lg mb-4 flex items-center gap-2 border-b border-border/50 pb-4">
                    <FileText size={20} className="text-primary" />
                    Specific Information
                </h3>
                <div className="space-y-1">
                    {productFieldRendererRegistry.renderFields(product)}
                </div>
            </div>

        </div>
    );
}
