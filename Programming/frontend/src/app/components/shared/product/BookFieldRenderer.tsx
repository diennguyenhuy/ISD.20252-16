import {ProductFieldRenderer} from "./ProductFieldRenderer";
import type {Book} from "../../../models/product.interface";
import {formatDate} from "../../../data/formatter";
import {InfoRow} from "./InfoRow";

export class BookFieldRenderer extends ProductFieldRenderer<Book> {
    render(product: Book) {
        return (
            <>
                <InfoRow label="Authors"       value={product.authors?.join(', ')} />
                <InfoRow label="Publisher"     value={product.publisher} />
                <InfoRow label="Publish Date"  value={product.publicationDate ? formatDate(product.publicationDate) : undefined} />
                <InfoRow label="Pages"         value={product.numberOfPages} />
                <InfoRow label="Language"      value={product.language} />
                <InfoRow label="Cover Type"    value={product.coverType} />
                <InfoRow label="Genre"         value={product.genre} />
            </>
        );
    }
}