import { useState } from 'react';
import { ProductFormSection, type FieldComponent } from './ProductFormSection';
import type { Book, CoverType } from '../../../models/product.interface';

// @ts-ignore
export class BookFormSection extends ProductFormSection<
    ReturnType<BookFormSection['buildCreatePayload']>,
    ReturnType<BookFormSection['buildUpdatePayload']>
> {
    readonly productType = 'Book';

    authors: string = '';
    publisher: string = '';
    publicationDate: string = '';
    numberOfPages: string = '';
    language: string = '';
    genre: string = '';
    coverType: CoverType = 'PAPERBACK';

    // Setters injected by the form screen after useState wiring
    setAuthors!: (v: string) => void;
    setPublisher!: (v: string) => void;
    setPublicationDate!: (v: string) => void;
    setNumberOfPages!: (v: string) => void;
    setLanguage!: (v: string) => void;
    setGenre!: (v: string) => void;
    setCoverType!: (v: CoverType) => void;

    populateFromProduct(product: Book) {
        this.setAuthors(product.authors?.join(', ') ?? '');
        this.setPublisher(product.publisher ?? '');
        this.setPublicationDate(product.publicationDate ?? '');
        this.setNumberOfPages(String(product.numberOfPages ?? ''));
        this.setLanguage(product.language ?? '');
        this.setGenre(product.genre ?? '');
        this.setCoverType(product.coverType ?? 'PAPERBACK');
    }

    renderCreateFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-2 gap-5">
                <div className="col-span-2">
                    <Field label="Authors" required>
                        <input
                            disabled={isSubmitting}
                            value={this.authors}
                            onChange={e => this.setAuthors(e.target.value)}
                            placeholder="Comma-separated, e.g. J.K. Rowling, George R.R. Martin"
                            className={ic()}
                        />
                    </Field>
                </div>
                <Field label="Publisher" required>
                    <input disabled={isSubmitting} value={this.publisher} onChange={e => this.setPublisher(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publication Date" required>
                    <input disabled={isSubmitting} type="date" value={this.publicationDate} onChange={e => this.setPublicationDate(e.target.value)} className={ic()} />
                </Field>
                <Field label="Cover Type" required>
                    <select disabled={isSubmitting} value={this.coverType} onChange={e => this.setCoverType(e.target.value as any)} className={ic()}>
                        <option value="PAPERBACK">Paperback</option>
                        <option value="HARDCOVER">Hardcover</option>
                    </select>
                </Field>
                <Field label="Number of Pages">
                    <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={this.numberOfPages} onChange={e => this.setNumberOfPages(e.target.value)} className={ic()} />
                </Field>
                <Field label="Language">
                    <input disabled={isSubmitting} value={this.language} onChange={e => this.setLanguage(e.target.value)} className={ic()} />
                </Field>
                <Field label="Genre">
                    <input disabled={isSubmitting} value={this.genre} onChange={e => this.setGenre(e.target.value)} className={ic()} />
                </Field>
            </div>
        );
    }

    renderEditFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-2 gap-5">
                <div className="col-span-2">
                    <Field label="Authors">
                        <input
                            disabled={isSubmitting}
                            value={this.authors}
                            onChange={e => this.setAuthors(e.target.value)}
                            placeholder="Comma-separated"
                            className={ic()}
                        />
                    </Field>
                </div>
                <Field label="Publisher">
                    <input disabled={isSubmitting} value={this.publisher} onChange={e => this.setPublisher(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publication Date">
                    <input disabled type="date" value={this.publicationDate} onChange={e => this.setPublicationDate(e.target.value)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                </Field>
                <Field label="Cover Type">
                    <select disabled value={this.coverType} onChange={e => this.setCoverType(e.target.value as any)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`}>
                        <option value="PAPERBACK">Paperback</option>
                        <option value="HARDCOVER">Hardcover</option>
                    </select>
                </Field>
                <Field label="Language">
                    <input disabled={isSubmitting} value={this.language} onChange={e => this.setLanguage(e.target.value)} className={ic()} />
                </Field>
                <Field label="Number of Pages">
                    <input disabled={isSubmitting} type="number" onWheel={e => e.currentTarget.blur()} value={this.numberOfPages} onChange={e => this.setNumberOfPages(e.target.value)} className={ic()} />
                </Field>
                <Field label="Genre">
                    <input disabled={isSubmitting} value={this.genre} onChange={e => this.setGenre(e.target.value)} className={ic()} />
                </Field>
            </div>
        );
    }

    buildCreatePayload() {
        return {
            authors: this.authors.split(',').map(s => s.trim()).filter(Boolean),
            publisher: this.publisher,
            publicationDate: this.publicationDate,
            coverType: this.coverType,
            numberOfPages: this.numberOfPages ? Number(this.numberOfPages) : undefined,
            language: this.language || undefined,
            genre: this.genre || undefined,
        };
    }

    buildUpdatePayload() {
        const authors = this.authors.split(',').map(s => s.trim()).filter(Boolean);
        return {
            authors: authors.length ? authors : undefined,
            publisher: this.publisher || undefined,
            language: this.language || undefined,
            numberOfPages: this.numberOfPages ? Number(this.numberOfPages) : undefined,
            genre: this.genre || undefined,
        };
    }
}

export function useBookFormSection(): BookFormSection {
    const section = useState(() => new BookFormSection())[0];

    const [authors, setAuthors] = useState('');
    const [publisher, setPublisher] = useState('');
    const [publicationDate, setPublicationDate] = useState('');
    const [numberOfPages, setNumberOfPages] = useState('');
    const [language, setLanguage] = useState('');
    const [genre, setGenre] = useState('');
    const [coverType, setCoverType] = useState<CoverType>('PAPERBACK');

    section.authors = authors; section.setAuthors = setAuthors;
    section.publisher = publisher; section.setPublisher = setPublisher;
    section.publicationDate = publicationDate; section.setPublicationDate = setPublicationDate;
    section.numberOfPages = numberOfPages; section.setNumberOfPages = setNumberOfPages;
    section.language = language; section.setLanguage = setLanguage;
    section.genre = genre; section.setGenre = setGenre;
    section.coverType = coverType; section.setCoverType = setCoverType;

    return section;
}
