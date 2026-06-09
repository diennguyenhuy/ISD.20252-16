import type { ReactNode } from 'react';
import type { ProductTypeName } from '../../../models/product.interface';

export abstract class ProductFormSection<C, U> {
    abstract readonly productType: ProductTypeName;
    abstract populateFromProduct(product: any): void;
    abstract renderCreateFields(inputClass: (err?: string) => string, Field: FieldComponent, isSubmitting: boolean): ReactNode;
    abstract renderEditFields(inputClass: (err?: string) => string, Field: FieldComponent, isSubmitting: boolean): ReactNode;
    abstract buildCreatePayload(): C;
    abstract buildUpdatePayload(): U;
}

export type FieldComponent = React.ComponentType<{
    label: string;
    required?: boolean;
    error?: string;
    children: ReactNode;
}>;
