import type {ReactNode} from "react";
import type {Product} from "../../../models/product.interface";

export abstract class ProductFieldRenderer<P extends Product> {
    abstract render(product: P): ReactNode;
}