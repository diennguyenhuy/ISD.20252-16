import { apiClient } from "./client";
import type { ProductSummary, ProductType } from "../models/product.interface";

const PRODUCTS_URL = "/products";

const HomepageService = {
    getProductList: async (): Promise<ProductSummary[]> => {
        const response= await apiClient.get<ProductSummary[]>(PRODUCTS_URL);
        return response.data;
    },

    filterProductsBy: async (page: number, title?: string, category?: string, minPrice?: number, maxPrice?: number): Promise<ProductSummary[]> => {
        let requestURL = `${PRODUCTS_URL}?page=${page}`;
        if (title) requestURL += `&title=${title}`;
        if (category) requestURL += `&category=${category}`;
        if (minPrice) requestURL += `&minPrice=${minPrice}`;
        if (maxPrice) requestURL += `&maxPrice=${maxPrice}`;
        const response = await apiClient.get<ProductSummary[]>(requestURL);
        return response.data;
    },

    getProductDetail: async (productId: string): Promise<ProductType> => {
        const response = await apiClient.get<ProductType>(`${PRODUCTS_URL}/${productId}`);
        return response.data;
    }
}

export default HomepageService;