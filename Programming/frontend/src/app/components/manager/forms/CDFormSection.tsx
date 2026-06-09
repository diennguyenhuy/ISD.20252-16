import { useState } from 'react';
import { ProductFormSection, type FieldComponent } from './ProductFormSection';
import type { CD, Track } from '../../../models/product.interface';
import { Plus, Trash2 } from 'lucide-react';

export interface TrackFormRow {
    title: string;
    length: string;   // seconds, stored as string while editing for input compatibility
}

// @ts-ignore
export class CDFormSection extends ProductFormSection<
    ReturnType<CDFormSection['buildCreatePayload']>,
    ReturnType<CDFormSection['buildUpdatePayload']>
> {
    readonly productType = 'CD';

    artists: string = '';
    recordLabel: string = '';
    genre: string = '';
    releaseDate: string = '';
    tracks: TrackFormRow[] = [];

    setArtists!: (v: string) => void;
    setRecordLabel!: (v: string) => void;
    setGenre!: (v: string) => void;
    setReleaseDate!: (v: string) => void;
    setTracks!: (v: TrackFormRow[] | ((prev: TrackFormRow[]) => TrackFormRow[])) => void;

    populateFromProduct(product: CD) {
        this.setArtists(product.artists?.join(', ') ?? '');
        this.setRecordLabel(product.recordLabel ?? '');
        this.setGenre(product.genre ?? '');
        this.setReleaseDate(product.releaseDate ?? '');
        this.setTracks(
            (product.tracks ?? []).map((t: Track) => ({
                title: t.title,
                length: String(t.length),
            }))
        );
    }

    private addTrack() {
        this.setTracks(prev => [...prev, { title: '', length: '' }]);
    }

    private removeTrack(idx: number) {
        this.setTracks(prev => prev.filter((_, i) => i !== idx));
    }

    private updateTrack(idx: number, field: keyof TrackFormRow, value: string) {
        this.setTracks(prev =>
            prev.map((t, i) => i === idx ? { ...t, [field]: value } : t)
        );
    }

    private renderTrackEditor(ic: (e?: string) => string, isEdit: boolean, isSubmitting: boolean) {
        return (
            <div className="col-span-2 flex flex-col gap-2">
                <div className="flex items-center justify-between mb-1">
                    <span className="text-sm font-bold text-foreground">
                        Tracks {!isEdit && <span className="text-destructive">*</span>}
                        <span className="text-muted-foreground font-normal ml-1">
                            ({this.tracks.length})
                        </span>
                    </span>
                    <button
                        disabled={isSubmitting}
                        type="button"
                        onClick={() => this.addTrack()}
                        className="flex items-center gap-1.5 text-xs font-bold text-primary hover:text-primary/80 px-3 py-1.5 rounded-lg border border-primary/30 hover:bg-primary/5 transition-colors"
                    >
                        <Plus size={14} /> Add Track
                    </button>
                </div>

                {this.tracks.length === 0 ? (
                    <div className="border-2 border-dashed border-border rounded-xl p-6 text-center text-muted-foreground text-sm">
                        No tracks yet. Click "Add Track" to begin.
                    </div>
                ) : (
                    <div className="space-y-2">
                        <div className="grid grid-cols-[2rem_1fr_7rem_2rem] gap-2 px-2">
                            <span />
                            <span className="text-xs font-semibold text-muted-foreground">Title</span>
                            <span className="text-xs font-semibold text-muted-foreground">Duration (sec)</span>
                            <span />
                        </div>
                        {this.tracks.map((track, idx) => (
                            <div
                                key={idx}
                                className="grid grid-cols-[2rem_1fr_7rem_2rem] gap-2 items-center bg-muted/30 rounded-xl px-2 py-2 border border-border/50"
                            >
                                <span className="text-xs text-muted-foreground font-mono text-center">
                                    {idx + 1}
                                </span>
                                <input
                                    disabled={isSubmitting}
                                    value={track.title}
                                    onChange={e => this.updateTrack(idx, 'title', e.target.value)}
                                    placeholder="Track title"
                                    className={ic()}
                                />
                                <input
                                    disabled={isSubmitting}
                                    type="number"
                                    min={1}
                                    onWheel={e => e.currentTarget.blur()}
                                    value={track.length}
                                    onChange={e => this.updateTrack(idx, 'length', e.target.value)}
                                    placeholder="e.g. 214"
                                    className={ic()}
                                />
                                <button
                                    disabled={isSubmitting}
                                    type="button"
                                    onClick={() => this.removeTrack(idx)}
                                    className="p-1.5 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                                >
                                    <Trash2 size={14} />
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        );
    }

    renderCreateFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-2 gap-5">
                <div className="col-span-2">
                    <Field label="Artists" required>
                        <input
                            disabled={isSubmitting}
                            value={this.artists}
                            onChange={e => this.setArtists(e.target.value)}
                            placeholder="Comma-separated, e.g. The Beatles, John Lennon"
                            className={ic()}
                        />
                    </Field>
                </div>
                <Field label="Record Label" required>
                    <input disabled={isSubmitting} value={this.recordLabel} onChange={e => this.setRecordLabel(e.target.value)} className={ic()} />
                </Field>
                <Field label="Genre" required>
                    <input disabled={isSubmitting} value={this.genre} onChange={e => this.setGenre(e.target.value)} className={ic()} />
                </Field>
                <Field label="Release Date">
                    <input disabled={isSubmitting} type="date" value={this.releaseDate} onChange={e => this.setReleaseDate(e.target.value)} className={ic()} />
                </Field>
                {this.renderTrackEditor(ic, false, isSubmitting)}
            </div>
        );
    }

    renderEditFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-2 gap-5">
                <div className="col-span-2">
                    <Field label="Artists">
                        <input
                            disabled={isSubmitting}
                            value={this.artists}
                            onChange={e => this.setArtists(e.target.value)}
                            placeholder="Comma-separated"
                            className={ic()}
                        />
                    </Field>
                </div>
                <Field label="Record Label">
                    <input disabled={isSubmitting} value={this.recordLabel} onChange={e => this.setRecordLabel(e.target.value)} className={ic()} />
                </Field>
                <Field label="Genre">
                    <input disabled={isSubmitting} value={this.genre} onChange={e => this.setGenre(e.target.value)} className={ic()} />
                </Field>
                <Field label="Release Date">
                    <input disabled type="date" value={this.releaseDate} onChange={e => this.setReleaseDate(e.target.value)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                </Field>
                {this.renderTrackEditor(ic, true, isSubmitting)}
            </div>
        );
    }

    buildCreatePayload() {
        return {
            artists: this.artists.split(',').map(s => s.trim()).filter(Boolean),
            recordLabel: this.recordLabel,
            genre: this.genre,
            releaseDate: this.releaseDate || undefined,
            tracks: this.tracks.map(t => ({
                title: t.title,
                length: Number(t.length),
            })),
        };
    }

    buildUpdatePayload() {
        const artists = this.artists.split(',').map(s => s.trim()).filter(Boolean);
        return {
            artists: artists.length ? artists : undefined,
            recordLabel: this.recordLabel || undefined,
            genre: this.genre || undefined,
            // Value Object — same shape as create; full array replaces stored list
            tracks: this.tracks.length
                ? this.tracks.map(t => ({
                    title: t.title,
                    length: Number(t.length),
                }))
                : undefined,
        };
    }
}

export function useCDFormSection(): CDFormSection {
    const section = useState(() => new CDFormSection())[0];

    const [artists, setArtists] = useState('');
    const [recordLabel, setRecordLabel] = useState('');
    const [genre, setGenre] = useState('');
    const [releaseDate, setReleaseDate] = useState('');
    const [tracks, setTracks] = useState<TrackFormRow[]>([]);

    section.artists = artists; section.setArtists = setArtists;
    section.recordLabel = recordLabel; section.setRecordLabel = setRecordLabel;
    section.genre = genre; section.setGenre = setGenre;
    section.releaseDate = releaseDate; section.setReleaseDate = setReleaseDate;
    section.tracks = tracks; section.setTracks = setTracks as any;

    return section;
}
