import { useState, useCallback } from 'react';
import ProductManagementService from '../../../api/ProductManagementService';
import type { CreateProductPayload, UpdateProductPayload } from '../../../api/ProductManagementService';
import type { ProductSummary, Product } from '../../../models/product.interface';

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

    const getProduct = async (id: string): Promise<Product | null> => {
        try {
            return await ProductManagementService.getProductById(id);
        } catch (err: any) {
            setError(err.message || 'Product not found');
            return null;
        }
    };

    const createProduct = async (data: CreateProductPayload) => {
        try {
            return await ProductManagementService.createProduct(data);
        } catch (err: any) {
            return { error: extractErrorMessage(err) };
        }
    };

    const updateProduct = async (id: string, data: UpdateProductPayload) => {
        try {
            return await ProductManagementService.updateProduct(id, data);
        } catch (err: any) {
            return { success: false, error: extractErrorMessage(err) };
        }
    };

    const adjustStock = async (id: string, delta: number, reason: string) => {
        try {
            await ProductManagementService.adjustStock(id, delta, reason);
            await fetchProducts();
            return { success: true };
        } catch (err: any) {
            return { success: false, error: extractErrorMessage(err) };
        }
    };

    const deleteProduct = async (id: string) => {
        try {
            return await ProductManagementService.deleteProduct(id);
        } catch (err: any) {
            return { success: false, error: extractErrorMessage(err) };
        }
    };

    const deleteProducts = async (ids: string[]) => {
        if (ids.length === 0) return { success: false, error: 'No products selected.' };
        if (ids.length > 10) {
            return {
                success: false,
                error: `Cannot delete more than 10 products at once (selected: ${ids.length}).`,
            };
        }
        try {
            await ProductManagementService.deleteProducts(ids);
            await fetchProducts();
            return { success: true };
        } catch (err: any) {
            return { success: false, error: extractErrorMessage(err) };
        }
    };

    const activateProduct = async (id: string) => {
        try {
            return await ProductManagementService.activateProduct(id);
        } catch (err: any) {
            return { success: false, error: extractErrorMessage(err) };
        }
    };

    return {
        products,
        setProducts,
        loading,
        error,
        fetchProducts,
        getProduct,
        createProduct,
        updateProduct,
        adjustStock,
        deleteProduct,
        deleteProducts,
        activateProduct,
    };
}

export function extractErrorMessage(err: any): string {
    return (
        err.response?.data?.message ||
        err.response?.data?.error  ||
        err.message                ||
        'An unexpected error occurred.'
    );
}
