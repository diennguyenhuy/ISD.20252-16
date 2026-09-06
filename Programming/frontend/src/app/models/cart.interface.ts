import type {ProductTypeName} from "./product.interface";

export interface Cart {
    items: CartItem[];
    totalQuantity: number;
    totalPrice: number;
}

export interface CartItem {
    productId: string;
    productTitle: string;
    stockQuantity: number;
    productImage?: string;
    productType: ProductTypeName;
    quantity: number;
    unitPrice: number;
    itemTotalPrice: number;
}