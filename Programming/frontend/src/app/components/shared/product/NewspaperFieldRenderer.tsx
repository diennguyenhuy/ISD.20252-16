import {ProductFieldRenderer} from "./ProductFieldRenderer";
import type {Newspaper} from "../../../models/product.interface";
import {InfoRow} from "./InfoRow";
import {formatDate} from "../../../data/formatter";

export class NewspaperFieldRenderer extends ProductFieldRenderer<Newspaper> {
    render(product: Newspaper) {
        return (
            <>
                <InfoRow label="Editor In Chief" value={product.editorInChief}/>
                <InfoRow label="Publisher"    value={product.publisher} />
                <InfoRow label="Publish Date" value={product.publicationDate ? formatDate(product.publicationDate) : undefined} />
                <InfoRow label="Language"     value={product.language} />
                <InfoRow label="Issue Number" value={product.issueNumber} />
                <InfoRow label="Frequency"    value={product.publicationFrequency} />
                <InfoRow label="ISSN"         value={product.ISSN} />
            </>
        );
    }
}