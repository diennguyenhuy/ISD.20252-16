import { apiClient } from './client';
import type { ProductSummary, ProductType } from '../models/product.interface';

// Payload gửi lên Backend CUD (Luôn sử dụng UPPERCASE cho productType để khớp với Java Enum/Jackson)
export interface ProductRequestPayload {
    productType: 'BOOK' | 'CD' | 'DVD' | 'NEWSPAPER';

    // Thuộc tính chung (Product)
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

    // Thuộc tính của Sách (Book) & Báo (Newspaper)
    publisher?: string;
    publicationDate?: string; // Format: YYYY-MM-DD
    language?: string;

    // Thuộc tính riêng của Sách (Book)
    authors?: string[];
    coverType?: 'HARDCOVER' | 'PAPERBACK';
    numberOfPages?: number;

    // Thuộc tính riêng của Báo (Newspaper)
    editorInChief?: string;
    issueNumber?: string;
    publicationFrequency?: string;
    ISSN?: string;
    sections?: string[];

    // Thuộc tính chung cho Nhạc/Phim (CD/DVD)
    releaseDate?: string; // Format: YYYY-MM-DD
    genre?: string;

    // Thuộc tính riêng của Đĩa nhạc (CD)
    artists?: string[];
    recordLabel?: string;
    tracks?: { title: string; length: number }[];

    // Thuộc tính riêng của Phim (DVD)
    discType?: 'HD_DVD' | 'BLU_RAY';
    director?: string;
    runtime?: number;
    studio?: string;
    subtitles?: string; // DTO backend nhận String phân tách bằng dấu phẩy
}

export const ProductManagementService = {
    getAllProducts: async (): Promise<ProductSummary[]> => {
        const response = await apiClient.get<ProductSummary[]>('manager/products');
        return response.data;
    },

    // Trả về ProductType (Sẽ tự động map về đúng Book | CD | DVD | Newspaper dựa vào data)
    getProductById: async (id: string): Promise<ProductType> => {
        const response = await apiClient.get<ProductType>(`manager/products/${id}`);
        return response.data;
    },

    createProduct: async (data: ProductRequestPayload): Promise<ProductType> => {
        const response = await apiClient.post<ProductType>('manager/products', data);
        return response.data;
    },

    updateProduct: async (id: string, data: Partial<ProductRequestPayload>): Promise<ProductType> => {
        const response = await apiClient.patch<ProductType>(`manager/products/${id}`, data);
        return response.data;
    },

    deleteProduct: async (id: string): Promise<void> => {
        await apiClient.delete('manager/products', { data: { productIds: [id] } });
    },

    adjustStock: async (id: string, delta: number, reason: string): Promise<void> => {
        await apiClient.post(`manager/products`, { delta, reason });
    }
};