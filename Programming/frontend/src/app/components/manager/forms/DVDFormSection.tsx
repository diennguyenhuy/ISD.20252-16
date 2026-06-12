import { useState } from 'react';
import { ProductFormSection, type FieldComponent } from './ProductFormSection';
import type { DVD, DiscType } from '../../../models/product.interface';

// @ts-ignore
export class DVDFormSection extends ProductFormSection<
    ReturnType<DVDFormSection['buildCreatePayload']>,
    Partial<ReturnType<DVDFormSection['buildCreatePayload']>>
> {
    readonly productType = 'DVD';

    director: string = '';
    studio: string = '';
    runtime: string = '';
    language: string = '';
    subtitles: string = '';
    genre: string = '';
    releaseDate: string = '';
    discType: DiscType = 'BLU_RAY';

    private _originalState: Record<string, string> = {};

    setDirector!: (v: string) => void;
    setStudio!: (v: string) => void;
    setRuntime!: (v: string) => void;
    setLanguage!: (v: string) => void;
    setSubtitles!: (v: string) => void;
    setGenre!: (v: string) => void;
    setReleaseDate!: (v: string) => void;
    setDiscType!: (v: DiscType) => void;

    populateFromProduct(product: DVD) {
        const subtitlesStr = product.subtitles?.join(', ') ?? '';
        const runtimeStr = String(product.runtime ?? '');

        // 1. Populate UI State
        this.setDirector(product.director ?? '');
        this.setStudio(product.studio ?? '');
        this.setRuntime(runtimeStr);
        this.setLanguage(product.language ?? '');
        this.setSubtitles(subtitlesStr);
        this.setGenre(product.genre ?? '');
        this.setReleaseDate(product.releaseDate ?? '');
        this.setDiscType(product.discType ?? 'BLU_RAY');

        // 2. Save Snapshot for diffing later
        this._originalState = {
            director: product.director ?? '',
            studio: product.studio ?? '',
            runtime: runtimeStr,
            language: product.language ?? '',
            subtitles: subtitlesStr,
            genre: product.genre ?? '',
            releaseDate: product.releaseDate ?? '',
            discType: product.discType ?? 'BLU_RAY'
        };
    }

    renderCreateFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
                <Field label="Director" required>
                    <input disabled={isSubmitting} value={this.director} onChange={e => this.setDirector(e.target.value)} className={ic()} />
                </Field>
                <Field label="Studio" required>
                    <input disabled={isSubmitting} value={this.studio} onChange={e => this.setStudio(e.target.value)} className={ic()} />
                </Field>
                <Field label="Runtime (minutes)" required>
                    <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={this.runtime} onChange={e => this.setRuntime(e.target.value)} className={ic()} />
                </Field>
                <Field label="Disc Type" required>
                    <select disabled={isSubmitting} value={this.discType} onChange={e => this.setDiscType(e.target.value as any)} className={ic()}>
                        <option value="BLU_RAY">Blu-ray</option>
                        <option value="HD_DVD">HD-DVD</option>
                    </select>
                </Field>
                <Field label="Language" required>
                    <input disabled={isSubmitting} value={this.language} onChange={e => this.setLanguage(e.target.value)} className={ic()} />
                </Field>
                <Field label="Genre">
                    <input disabled={isSubmitting} value={this.genre} onChange={e => this.setGenre(e.target.value)} className={ic()} />
                </Field>
                <Field label="Release Date">
                    <input disabled={isSubmitting} type="date" value={this.releaseDate} onChange={e => this.setReleaseDate(e.target.value)} className={ic()} />
                </Field>
                <div className="sm:col-span-2">
                    <Field label="Subtitles" required>
                        <input
                            disabled={isSubmitting}
                            value={this.subtitles}
                            onChange={e => this.setSubtitles(e.target.value)}
                            placeholder="Comma-separated, e.g. English, Vietnamese, French"
                            className={ic()}
                        />
                    </Field>
                </div>
            </div>
        );
    }

    renderEditFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">
                <Field label="Director">
                    <input disabled={isSubmitting} value={this.director} onChange={e => this.setDirector(e.target.value)} className={ic()} />
                </Field>
                <Field label="Studio">
                    <input disabled={isSubmitting} value={this.studio} onChange={e => this.setStudio(e.target.value)} className={ic()} />
                </Field>
                <Field label="Runtime (minutes)">
                    <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={this.runtime} onChange={e => this.setRuntime(e.target.value)} className={ic()} />
                </Field>
                <Field label="Disc Type">
                    <select disabled value={this.discType} onChange={e => this.setDiscType(e.target.value as any)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`}>
                        <option value="BLU_RAY">Blu-ray</option>
                        <option value="HD_DVD">HD-DVD</option>
                    </select>
                </Field>
                <Field label="Language">
                    <input disabled={isSubmitting} value={this.language} onChange={e => this.setLanguage(e.target.value)} className={ic()} />
                </Field>
                <Field label="Genre">
                    <input disabled={isSubmitting} value={this.genre} onChange={e => this.setGenre(e.target.value)} className={ic()} />
                </Field>
                <Field label="Release Date">
                    <input disabled type="date" value={this.releaseDate} onChange={e => this.setReleaseDate(e.target.value)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                </Field>
                <div className="sm:col-span-2">
                    <Field label="Subtitles">
                        <input
                            disabled={isSubmitting}
                            value={this.subtitles}
                            onChange={e => this.setSubtitles(e.target.value)}
                            placeholder="Comma-separated"
                            className={ic()}
                        />
                    </Field>
                </div>
            </div>
        );
    }

    buildCreatePayload() {
        return {
            director: this.director,
            studio: this.studio,
            runtime: Number(this.runtime),
            discType: this.discType,
            language: this.language,
            genre: this.genre || undefined,
            releaseDate: this.releaseDate || undefined,
            subtitles: this.subtitles.split(',').map(s => s.trim()).filter(Boolean),
        };
    }

    buildUpdatePayload() {
        const payload: Record<string, any> = {};

        if (this.director !== this._originalState.director) {
            payload.director = this.director;
        }
        if (this.studio !== this._originalState.studio) {
            payload.studio = this.studio;
        }
        if (this.runtime !== this._originalState.runtime) {
            // Send null or undefined if cleared
            payload.runtime = this.runtime ? Number(this.runtime) : null;
        }
        if (this.language !== this._originalState.language) {
            payload.language = this.language;
        }
        if (this.genre !== this._originalState.genre) {
            payload.genre = this.genre;
        }
        if (this.subtitles !== this._originalState.subtitles) {
            payload.subtitles = this.subtitles
                ? this.subtitles.split(',').map(s => s.trim()).filter(Boolean)
                : []; // Clears the array in DB if completely wiped out by user
        }

        return payload;
    }
}

export function useDVDFormSection(): DVDFormSection {
    const section = useState(() => new DVDFormSection())[0];

    const [director, setDirector] = useState('');
    const [studio, setStudio] = useState('');
    const [runtime, setRuntime] = useState('');
    const [language, setLanguage] = useState('');
    const [subtitles, setSubtitles] = useState('');
    const [genre, setGenre] = useState('');
    const [releaseDate, setReleaseDate] = useState('');
    const [discType, setDiscType] = useState<DiscType>('BLU_RAY');

    section.director = director; section.setDirector = setDirector;
    section.studio = studio; section.setStudio = setStudio;
    section.runtime = runtime; section.setRuntime = setRuntime;
    section.language = language; section.setLanguage = setLanguage;
    section.subtitles = subtitles; section.setSubtitles = setSubtitles;
    section.genre = genre; section.setGenre = setGenre;
    section.releaseDate = releaseDate; section.setReleaseDate = setReleaseDate;
    section.discType = discType; section.setDiscType = setDiscType;

    return section;
}
