import { Music2 } from 'lucide-react';
import {ProductFieldRenderer} from "./ProductFieldRenderer";
import type {CD, Track} from "../../../models/product.interface";
import {InfoRow} from "./InfoRow";
import {formatDate, formatDurationSeconds} from "../../../data/formatter";

export class CDFieldRenderer extends ProductFieldRenderer<CD> {
    render(product: CD) {
        return (
            <>
                <InfoRow label="Artists"      value={product.artists?.join(', ')} />
                <InfoRow label="Record Label" value={product.recordLabel} />
                <InfoRow label="Release Date" value={product.releaseDate ? formatDate(product.releaseDate) : undefined} />
                <InfoRow label="Genre"        value={product.genre} />
                {product.tracks?.length > 0 && (
                    <div className="flex py-3 border-b border-border last:border-0 px-2 gap-0 flex-col">
                        <span className="text-muted-foreground text-sm font-medium mb-2">
                            Tracks ({product.tracks.length})
                        </span>
                        <ol className="space-y-1 w-full">
                            {product.tracks.map((track, idx) => (
                                <TrackRow key={idx} track={track} index={idx + 1} />
                            ))}
                        </ol>
                    </div>
                )}
            </>
        );
    }
}

function TrackRow({ track, index }: { track: Track; index: number }) {
    return (
        <li className="flex items-center gap-2 py-1 px-2 rounded-lg hover:bg-muted/30 transition-colors group">
            <span className="text-xs text-muted-foreground font-mono w-5 shrink-0 text-right">
                {index}
            </span>
            <Music2 size={12} className="text-muted-foreground/50 shrink-0 group-hover:text-primary/50 transition-colors" />
            <span className="text-sm text-foreground font-medium flex-1 truncate">
                {track.title}
            </span>
            <span className="text-xs text-muted-foreground font-mono shrink-0">
                {formatDurationSeconds(track.length)}
            </span>
        </li>
    );
}