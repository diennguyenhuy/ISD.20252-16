export type ProductTypeName = 'Book' | 'Newspaper' | 'CD' | 'DVD';

export type ProductType = Book | Newspaper | CD | DVD;

export type ProductStatus = 'ACTIVE' | 'DEACTIVATED' | 'DELETED';

export type ProductSummary = Pick<Product, 'id' | 'title' | 'originalValue' | 'currentPrice' | 'stockQuantity' | 'productType' | 'imageURL'> & { creators: string[] };

export interface Product {
    readonly id: string;
    title: string;
    category: string;
    description: string;
    height: number;
    width: number;
    length: number;
    weight: number;
    barcode: string;
    originalValue: number;
    currentPrice: number;
    stockQuantity: number;
    status: ProductStatus;
    imageURL: string;
    readonly addedAt?: string;
    readonly updatedAt?: string;
    productType: ProductTypeName;
}

export interface PrintableProduct extends Product {
    publisher: string;
    publicationDate: string;
    language?: string;
}

export type CoverType = 'HARDCOVER' | 'PAPERBACK';

export interface Book extends PrintableProduct {
    readonly productType: 'Book';
    authors: string[];
    coverType: CoverType;
    numberOfPages?: number;
    genre?: string;
}

export interface Newspaper extends PrintableProduct {
    readonly productType: 'Newspaper';
    editorInChief: string;
    issueNumber?: number;
    publicationFrequency?: string;
    ISSN?: string;
    sections?: string[];
}

export interface CD extends Product {
    readonly productType: 'CD';
    artists: string[];
    recordLabel: string;
    tracks: Track[];
    genre: string;
    releaseDate?: string;
}

export type DiscType = 'HD_DVD' | 'BLU_RAY';

export interface Track {
    id: string;
    title: string;
    length: number; // length in seconds
}

export interface DVD extends Product {
    readonly productType: 'DVD';
    discType: DiscType;
    director: string;
    runtime: number; // runtime in minutes
    studio: string;
    language: string;
    subtitles: string[];
    genre?: string;
    releaseDate?: string;
}
