import { BookOpen, Disc, FileText, Tv } from 'lucide-react';
import type { ProductTypeName } from '../../../models/product.interface';

export const PRODUCT_TYPE_META: Record<ProductTypeName, {
    label: string;
    icon: React.ElementType;
    color: string;
}> = {
    Book:      { label: 'Book',               icon: BookOpen, color: 'bg-primary/10 text-primary border-primary/20' },
    CD:        { label: 'CD',                 icon: Disc,     color: 'bg-secondary text-secondary-foreground border-border' },
    DVD:       { label: 'DVD',                icon: Tv,       color: 'bg-accent/50 text-accent-foreground border-accent' },
    Newspaper: { label: 'Newspaper/Magazine', icon: FileText, color: 'bg-muted text-muted-foreground border-border' },
};