import { useState, useCallback } from 'react';
import { ProductManagementService } from '../api/productManagementService';
import type { ProductRequestPayload } from '../api/productManagementService';
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

    const createProduct = async (data: ProductRequestPayload) => {
        try {
            await ProductManagementService.createProduct(data);
            await fetchProducts();
            return { success: true };
        } catch (err: any) {
            return { success: false, error: err.response?.data?.message || err.message };
        }
    };

    const updateProduct = async (id: string, data: Partial<ProductRequestPayload>) => {
        try {
            await ProductManagementService.updateProduct(id, data);
            await fetchProducts();
            return { success: true };
        } catch (err: any) {
            return { success: false, error: err.response?.data?.message || err.message };
        }
    };

    const deleteProducts = async (ids: string[]) => {
        try {
            await ProductManagementService.deleteProducts(ids);
            await fetchProducts();
            return { success: true };
        } catch (err: any) {
            return { success: false, error: err.response?.data?.message || err.message };
        }
    };

    const adjustStock = async (id: string, newStock: number, reason: string) => {
        try {
            const currentProduct = await ProductManagementService.getProductById(id);
            if (!currentProduct) {
                return { success: false, error: "Product not found" };
            }

            console.log(`[Adjust Stock] Product ID: ${id} | Reason: ${reason}`);

            // FIX TS2322: Use "unknown" cast to bypass strict exactOptionalPropertyTypes mapping
            const payload = {
                ...currentProduct,
                productType: currentProduct.productType.toUpperCase() as 'BOOK' | 'CD' | 'DVD' | 'NEWSPAPER',
                stockQuantity: newStock,
                currentPrice: currentProduct.currentPrice
            } as unknown as Partial<ProductRequestPayload>;

            await ProductManagementService.updateProduct(id, payload);
            await fetchProducts();
            return { success: true };
        } catch (err: any) {
            return { success: false, error: err.response?.data?.message || err.message };
        }
    };

    const activateProduct = async (id: string) => {
        try {
            await ProductManagementService.activateProduct(id);
            await fetchProducts(); // Refetch API to update product status
            return { success: true };
        } catch (err: any) {
            return { success: false, error: err.message };
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
        deleteProducts,
        adjustStock,
        activateProduct
    };
}