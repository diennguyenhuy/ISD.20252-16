import { useState, useCallback } from 'react';
import { ProductManagementService } from '../api/productManagementService';
import type { ProductSummary, ProductType } from '../models/product.interface';

export function useProductManagement() {
    const [products, setProducts] = useState<ProductSummary[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchProducts = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await ProductManagementService.getAllProducts();
            setProducts(data);
        } catch (err: any) {
            setError(err.message || 'Failed to load products');
        } finally {
            setLoading(false);
        }
    }, []);

    const getProduct = async (id: string): Promise<ProductType | null> => {
        setLoading(true);
        try {
            return await ProductManagementService.getProductById(id);
        } catch (err: any) {
            setError(err.message || 'Product not found');
            return null;
        } finally {
            setLoading(false);
        }
    };

    return {
        products,
        setProducts, // Để Optimistic UI update
        loading,
        error,
        fetchProducts,
        getProduct,
        createProduct: ProductManagementService.createProduct,
        updateProduct: ProductManagementService.updateProduct,
        deleteProduct: ProductManagementService.deleteProduct,
        adjustStock: ProductManagementService.adjustStock
    };
}