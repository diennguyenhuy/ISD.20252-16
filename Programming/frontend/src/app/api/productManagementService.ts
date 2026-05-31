import { apiClient } from './client';
import type { ProductSummary, ProductType } from '../models/product.interface';

export interface ProductRequestPayload {
    productType: 'BOOK' | 'CD' | 'DVD' | 'NEWSPAPER';
    title: string;
    category?: string;
    description?: string;
    barcode: string;
    originalValue: number;
    currentPrice: number;
    stockQuantity: number;
    imageURL?: string;
    weight: number;
    height: number;
    width: number;
    length: number;

    publisher?: string;
    publicationDate?: string;
    language?: string;

    authors?: string[];
    coverType?: 'HARDCOVER' | 'PAPERBACK';
    numberOfPages?: number;

    editorInChief?: string;
    issueNumber?: string;
    publicationFrequency?: string;
    ISSN?: string;
    sections?: string[];

    releaseDate?: string;
    genre?: string;

    artists?: string[];
    recordLabel?: string;
    tracks?: { title: string; length: number }[];

    discType?: 'HD_DVD' | 'BLU_RAY';
    director?: string;
    runtime?: number;
    studio?: string;
    subtitles?: string[]; // Đã sửa thành mảng string
    [key: string]: any;
}

export const ProductManagementService = {
    getAllProducts: async (): Promise<ProductSummary[]> => {
        const response = await apiClient.get<ProductSummary[]>('manager/products');
        return response.data;
    },

    getProductById: async (id: string): Promise<ProductType> => {
        const response = await apiClient.get<ProductType>(`manager/products/${id}`);
        return response.data;
    },

    createProduct: async (data: ProductRequestPayload): Promise<ProductType> => {
        const response = await apiClient.post<ProductType>('manager/products', data);
        return response.data;
    },

    updateProduct: async (id: string, data: Partial<ProductRequestPayload>): Promise<ProductType> => {
        const response = await apiClient.put<ProductType>(`manager/products/${id}`, data);
        return response.data;
    },

    deleteProduct: async (id: string): Promise<void> => {
        await apiClient.delete('manager/products', {
            data: { productIds: [id] },
            headers: { 'Content-Type': 'application/json' }
        });
    },

    activateProduct: async (id: string): Promise<void> => {
        await apiClient.patch(`manager/products/${id}/activate`);
    }
};