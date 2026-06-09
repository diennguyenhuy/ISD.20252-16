import {ProductFieldRenderer} from "./ProductFieldRenderer";
import type {DVD} from "../../../models/product.interface";
import {InfoRow} from "./InfoRow";
import {formatDate, formatDurationMinutes} from "../../../data/formatter";

export class DVDFieldRenderer extends ProductFieldRenderer<DVD> {
    render(product: DVD) {
        return (
            <>
                <InfoRow label="Studio"       value={product.studio} />
                <InfoRow label="Director"     value={product.director} />
                <InfoRow label="Runtime"      value={product.runtime ? formatDurationMinutes(product.runtime) : undefined} />
                <InfoRow label="Release Date" value={product.releaseDate ? formatDate(product.releaseDate) : undefined} />
                <InfoRow label="Language"     value={product.language} />
                <InfoRow label="Genre"        value={product.genre} />
                <InfoRow label="Disc Type"    value={product.discType} />
                <InfoRow label="Subtitles"    value={product.subtitles?.join(', ')} />
            </>
        );
    }
}