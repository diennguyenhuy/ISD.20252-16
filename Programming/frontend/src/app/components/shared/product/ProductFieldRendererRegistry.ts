import type {Product, ProductTypeName} from "../../../models";
import type {ProductFieldRenderer} from "./ProductFieldRenderer";
import type { ReactNode } from 'react';
import {BookFieldRenderer} from "./BookFieldRenderer";
import {NewspaperFieldRenderer} from "./NewspaperFieldRenderer";
import {CDFieldRenderer} from "./CDFieldRenderer";
import {DVDFieldRenderer} from "./DVDFieldRenderer";

type RenderMap = {
    [K in ProductTypeName]: ProductFieldRenderer<Product>;
};

const RENDERER_MAP: RenderMap = {
    Book: new BookFieldRenderer(),
    Newspaper: new NewspaperFieldRenderer(),
    CD: new CDFieldRenderer(),
    DVD: new DVDFieldRenderer(),
};

export const ProductFieldRendererRegistry = {
    renderFields: (product: Product): ReactNode => {
        return RENDERER_MAP[product.productType].render(product);
    }
}
