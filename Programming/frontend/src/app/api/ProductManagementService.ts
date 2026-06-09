import { apiClient } from './client';
import type { ProductSummary, Product, ProductTypeName, CoverType, DiscType } from '../models/product.interface';

const ProductManagementService = {

    getAllProducts: async (): Promise<ProductSummary[]> => {
        const response = await apiClient.get<ProductSummary[]>('manager/products');
        return response.data;
    },

    getProductById: async (id: string): Promise<Product> => {
        const response = await apiClient.get<Product>(`manager/products/${id}`);
        return response.data;
    },

    createProduct: async (data: CreateProductPayload): Promise<Product> => {
        const response = await apiClient.post<Product>('manager/products', data);
        return response.data;
    },

    updateProduct: async (id: string, data: UpdateProductPayload): Promise<Product> => {
        const response = await apiClient.patch<Product>(`manager/products/${id}`, data);
        return response.data;
    },

    adjustStock: async (id: string, delta: number, reason: string): Promise<void> => {
        const payload: AdjustStockPayload = { delta, reason };
        await apiClient.post(`manager/products/${id}/stock`, payload);
    },

    deleteProducts: async (ids: string[]): Promise<void> => {
        if (ids.length > 10) {
            throw new Error('Cannot delete more than 10 products in a single request.');
        }
        await apiClient.delete('manager/products', {
            data: ids,
            headers: { 'Content-Type': 'application/json' },
        });
    },

    deleteProduct: async (id: string): Promise<Product> => {
        const response = await apiClient.delete<Product>(`manager/products/${id}`);
        return response.data;
    },

    activateProduct: async (id: string): Promise<Product> => {
        const response = await apiClient.post<Product>(`manager/products/${id}/activate`);
        return response.data;
    },
};

export default ProductManagementService;

interface CreateProductBase {
    readonly productType: ProductTypeName;
    title: string;
    category: string;
    barcode: string;
    originalValue: number;
    currentPrice: number;
    stockQuantity: number;
    height: number;
    width: number;
    length: number;
    weight: number;
    description?: string;
    imageURL?: string;
}

interface UpdateProductBase {
    readonly productType: ProductTypeName;
    title?: string;
    category?: string;
    description?: string;
    imageURL?: string;
    currentPrice?: number;
    height?: number;
    width?: number;
    length?: number;
    weight?: number;
}

export interface CreateBookPayload extends CreateProductBase {
    readonly productType: 'Book';
    publisher: string;
    publicationDate: string;
    authors: string[];
    coverType: CoverType;
    numberOfPages?: number;
    language?: string;
    genre?: string;
}

export interface CreateCDPayload extends CreateProductBase {
    readonly productType: 'CD';
    artists: string[];
    recordLabel: string;
    genre: string;
    tracks: CreateTrackPayload[];
    releaseDate?: string;
}

export interface CreateTrackPayload {
    title: string;
    length: number; // seconds — matches CreateTrackRequest
}

export interface CreateDVDPayload extends CreateProductBase {
    readonly productType: 'DVD';
    director: string;
    studio: string;
    language: string;
    subtitles: string[];
    runtime: number;
    discType: DiscType;
    genre?: string;
    releaseDate?: string;
}

export interface CreateNewspaperPayload extends CreateProductBase {
    readonly productType: 'Newspaper';
    publisher: string;
    publicationDate: string;
    editorInChief: string;
    language?: string;
    issueNumber?: string;
    publicationFrequency?: string;
    ISSN?: string;
    sections?: string[];
}

export type CreateProductPayload =
    | CreateBookPayload
    | CreateCDPayload
    | CreateDVDPayload
    | CreateNewspaperPayload;

export interface UpdateBookPayload extends UpdateProductBase {
    readonly productType: 'Book';
    publisher?: string;
    language?: string;
    authors?: string[];
    numberOfPages?: number;
    genre?: string;
}

export interface UpdateCDPayload extends UpdateProductBase {
    readonly productType: 'CD';
    genre?: string;
    artists?: string[];
    recordLabel?: string;
    tracks?: UpdateTrackPayload[];
}

export interface UpdateTrackPayload {
    title?: string;
    length?: number;   // seconds
}

export interface UpdateDVDPayload extends UpdateProductBase {
    readonly productType: 'DVD';
    genre?: string;
    director?: string;
    runtime?: number;
    studio?: string;
    language?: string;
    subtitles?: string[];
}

export interface UpdateNewspaperPayload extends UpdateProductBase {
    readonly productType: 'Newspaper';
    publisher?: string;
    language?: string;
    editorInChief?: string;
    publicationFrequency?: string;
    sections?: string[];
}

export type UpdateProductPayload =
    | UpdateBookPayload
    | UpdateCDPayload
    | UpdateDVDPayload
    | UpdateNewspaperPayload;


export interface AdjustStockPayload {
    delta: number;
    reason: string;
}
