import type { ProductFormSection } from './ProductFormSection';
import { BookFormSection, useBookFormSection } from './BookFormSection';
import { CDFormSection, useCDFormSection } from './CDFormSection';
import { DVDFormSection, useDVDFormSection } from './DVDFormSection';
import { NewspaperFormSection, useNewspaperFormSection } from './NewspaperFormSection';
import type { ProductTypeName } from "../../../models/product.interface";

export const PRODUCT_TYPES: ProductTypeName[] = ['Book', 'CD', 'DVD', 'Newspaper'] as const;

export function useAllFormSections(): Record<ProductTypeName, ProductFormSection<any, any>> {
    return {
        Book: useBookFormSection(),
        CD: useCDFormSection(),
        DVD: useDVDFormSection(),
        Newspaper: useNewspaperFormSection()
    };
}
