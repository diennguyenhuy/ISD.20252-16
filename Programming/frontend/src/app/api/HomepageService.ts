import { apiClient } from "./client";
import type { ProductSummary, Product } from "../models/product.interface";
import type { Page } from "./Page";

export interface ProductFilterParams {
    title: string | undefined;
    category: string | undefined;
    minPrice: number | undefined;
    maxPrice: number | undefined;
    page: number;
}

const HomepageService = {
    get20RandomProducts: async (): Promise<ProductSummary[]> => {
        const response= await apiClient.get<ProductSummary[]>('products/random');
        return response.data;
    },

    filterProductsBy: async ({page, title, category, minPrice, maxPrice}: ProductFilterParams): Promise<ProductSummary[]> => {
        let requestURL = `products?page=${page}`;
        if (title) requestURL += `&title=${encodeURIComponent(title)}`;
        if (category) requestURL += `&category=${category}`;
        if (minPrice) requestURL += `&minPrice=${minPrice}`;
        if (maxPrice) requestURL += `&maxPrice=${maxPrice}`;
        const response = await apiClient.get<Page<ProductSummary>>(requestURL);
        return response.data.content;
    },

    getProductDetail: async (productId: string): Promise<Product> => {
        const response = await apiClient.get<Product>(`products/${productId}`);
        return response.data;
    }
}

export default HomepageService;