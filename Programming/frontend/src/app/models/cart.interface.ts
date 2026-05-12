import type {ProductSummary} from "./product.interface";

export interface Cart {
    items: CartItem[];
    totalQuantity: number;
    totalPrice: number;
}

export interface CartItem {
    product: ProductSummary;
    quantity: number;
    itemTotalPrice: number;
}