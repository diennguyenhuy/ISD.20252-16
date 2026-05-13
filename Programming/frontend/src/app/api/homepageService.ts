import { apiClient } from "./client";
import type { ProductSummary, ProductType } from "../models/product.interface";

const PRODUCTS_URL = "/products";

const HomepageService = {
    getProductList: async (): Promise<ProductSummary[]> => {
        const response= await apiClient.get<ProductSummary[]>(PRODUCTS_URL);
        return response.data;
    },

    getProductDetail: async (productId: string): Promise<ProductType> => {
        const response = await apiClient.get<ProductType>(`${PRODUCTS_URL}/${productId}`);
        return response.data;
    }
}

export default HomepageService;