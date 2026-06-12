import type { Book, CD, DVD, Newspaper, Product, ProductTypeName } from '../../../models/product.interface';

type CreatorExtractor = (product: Product) => string | undefined;

const CREATOR_MAP: Partial<Record<ProductTypeName, { label: string; extract: CreatorExtractor }>> = {
    Book:      { label: 'Author',          extract: p => (p as Book).authors?.join(', ') },
    CD:        { label: 'Artist',          extract: p => (p as CD).artists?.join(', ') },
    DVD:       { label: 'Studio',        extract: p => (p as DVD).studio },
    Newspaper: { label: 'Publisher', extract: p => (p as Newspaper).publisher },
};

interface ProductCreatorLineProps {
    product: Product;
}

export function ProductCreatorLine({ product }: ProductCreatorLineProps) {
    const entry = CREATOR_MAP[product.productType];

    if (!entry) return null;

    const value = entry.extract(product);
    if (!value) return null;

    return (
        <p className="text-muted-foreground">
            {entry.label}:{' '}
            <strong className="text-foreground font-semibold">{value}</strong>
        </p>
    );
}
