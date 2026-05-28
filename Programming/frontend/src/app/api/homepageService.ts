import { apiClient } from "./client";
import type { ProductSummary, ProductType } from "../models/product.interface";

export interface ProductFilterParams {
    title: string | undefined;
    category: string | undefined;
    minPrice: number | undefined;
    maxPrice: number | undefined;
    page: number;
}

const HomepageService = {
    get20RandomProducts: async (): Promise<ProductSummary[]> => {
        const response= await apiClient.get<ProductSummary[]>('products/initiate');
        return response.data;
    },

    filterProductsBy: async ({page, title, category, minPrice, maxPrice}: ProductFilterParams): Promise<ProductSummary[]> => {
        let requestURL = `products?page=${page}`;
        if (title) requestURL += `&title=${encodeURIComponent(title)}`;
        if (category) requestURL += `&category=${category}`;
        if (minPrice) requestURL += `&minPrice=${minPrice}`;
        if (maxPrice) requestURL += `&maxPrice=${maxPrice}`;
        const response = await apiClient.get<ProductSummary[]>(requestURL);
        return response.data;
    },

    getProductDetail: async (productId: string): Promise<ProductType> => {
        const response = await apiClient.get<ProductType>(`products/{id}`);
        return response.data;
    }
}

export default HomepageService;