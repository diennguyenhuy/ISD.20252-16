import { useState } from 'react';
import { ProductFormSection, type FieldComponent } from './ProductFormSection';
import type { Newspaper } from '../../../models/product.interface';

// @ts-ignore
export class NewspaperFormSection extends ProductFormSection<
    ReturnType<NewspaperFormSection['buildCreatePayload']>,
    ReturnType<NewspaperFormSection['buildUpdatePayload']>
> {
    readonly productType = 'Newspaper';

    publisher: string = '';
    publicationDate: string = '';
    editorInChief: string = '';
    language: string = '';
    issueNumber: string = '';
    publicationFrequency: string = '';
    ISSN: string = '';
    sections: string = '';

    setPublisher!: (v: string) => void;
    setPublicationDate!: (v: string) => void;
    setEditorInChief!: (v: string) => void;
    setLanguage!: (v: string) => void;
    setIssueNumber!: (v: string) => void;
    setPublicationFrequency!: (v: string) => void;
    setISSN!: (v: string) => void;
    setSections!: (v: string) => void;

    populateFromProduct(product: Newspaper) {
        this.setPublisher(product.publisher ?? '');
        this.setPublicationDate(product.publicationDate ?? '');
        this.setEditorInChief(product.editorInChief ?? '');
        this.setLanguage(product.language ?? '');
        this.setIssueNumber(product.issueNumber ? String(product.issueNumber) : '');
        this.setPublicationFrequency(product.publicationFrequency ?? '');
        this.setISSN(product.ISSN ?? '');
        this.setSections(product.sections?.join(', ') ?? '');
    }

    renderCreateFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-2 gap-5">
                <Field label="Editor in Chief" required>
                    <input disabled={isSubmitting} value={this.editorInChief} onChange={e => this.setEditorInChief(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publisher" required>
                    <input disabled={isSubmitting} value={this.publisher} onChange={e => this.setPublisher(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publication Date" required>
                    <input disabled={isSubmitting} type="date" value={this.publicationDate} onChange={e => this.setPublicationDate(e.target.value)} className={ic()} />
                </Field>
                <Field label="Language">
                    <input disabled={isSubmitting} value={this.language} onChange={e => this.setLanguage(e.target.value)} className={ic()} />
                </Field>
                <Field label="Issue Number">
                    <input disabled={isSubmitting} value={this.issueNumber} onChange={e => this.setIssueNumber(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publication Frequency">
                    <input disabled={isSubmitting} value={this.publicationFrequency} onChange={e => this.setPublicationFrequency(e.target.value)} placeholder="e.g. Weekly, Monthly" className={ic()} />
                </Field>
                <Field label="ISSN">
                    <input disabled={isSubmitting} value={this.ISSN} onChange={e => this.setISSN(e.target.value)} className={ic()} />
                </Field>
                <div className="col-span-2">
                    <Field label="Sections">
                        <input
                            disabled={isSubmitting}
                            value={this.sections}
                            onChange={e => this.setSections(e.target.value)}
                            placeholder="Comma-separated, e.g. Politics, Technology, Sports"
                            className={ic()}
                        />
                    </Field>
                </div>
            </div>
        );
    }

    renderEditFields(ic: (e?: string) => string, Field: FieldComponent, isSubmitting: boolean) {
        return (
            <div className="grid grid-cols-2 gap-5">
                <Field label="Editor in Chief">
                    <input disabled={isSubmitting} value={this.editorInChief} onChange={e => this.setEditorInChief(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publisher">
                    <input disabled={isSubmitting} value={this.publisher} onChange={e => this.setPublisher(e.target.value)} className={ic()} />
                </Field>
                <Field label="Publication Date">
                    <input disabled type="date" value={this.publicationDate} onChange={e => this.setPublicationDate(e.target.value)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                </Field>
                <Field label="Language">
                    <input disabled={isSubmitting} value={this.language} onChange={e => this.setLanguage(e.target.value)} className={ic()} />
                </Field>
                <Field label="Issue Number">
                    <input disabled value={this.issueNumber} onChange={e => this.setIssueNumber(e.target.value)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                </Field>
                <Field label="Publication Frequency">
                    <input disabled={isSubmitting} value={this.publicationFrequency} onChange={e => this.setPublicationFrequency(e.target.value)} placeholder="e.g. Weekly, Monthly" className={ic()} />
                </Field>
                <Field label="ISSN">
                    <input disabled value={this.ISSN} onChange={e => this.setISSN(e.target.value)} className={`${ic()} opacity-70 bg-muted/50 cursor-not-allowed`} />
                </Field>
                <div className="col-span-2">
                    <Field label="Sections">
                        <input
                            disabled={isSubmitting}
                            value={this.sections}
                            onChange={e => this.setSections(e.target.value)}
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
            publisher: this.publisher,
            publicationDate: this.publicationDate,
            editorInChief: this.editorInChief,
            language: this.language || undefined,
            issueNumber: this.issueNumber || undefined,
            publicationFrequency: this.publicationFrequency || undefined,
            ISSN: this.ISSN || undefined,
            sections: this.sections
                ? this.sections.split(',').map(s => s.trim()).filter(Boolean)
                : undefined,
        };
    }

    buildUpdatePayload() {
        return {
            publisher: this.publisher || undefined,
            language: this.language || undefined,
            editorInChief: this.editorInChief || undefined,
            publicationFrequency: this.publicationFrequency || undefined,
            sections: this.sections
                ? this.sections.split(',').map(s => s.trim()).filter(Boolean)
                : undefined,
        };
    }
}

export function useNewspaperFormSection(): NewspaperFormSection {
    const section = useState(() => new NewspaperFormSection())[0];

    const [publisher, setPublisher] = useState('');
    const [publicationDate, setPublicationDate] = useState('');
    const [editorInChief, setEditorInChief] = useState('');
    const [language, setLanguage] = useState('');
    const [issueNumber, setIssueNumber] = useState('');
    const [publicationFrequency, setPublicationFrequency] = useState('');
    const [ISSN, setISSN] = useState('');
    const [sections, setSections] = useState('');

    section.publisher = publisher; section.setPublisher = setPublisher;
    section.publicationDate = publicationDate; section.setPublicationDate = setPublicationDate;
    section.editorInChief = editorInChief; section.setEditorInChief = setEditorInChief;
    section.language = language; section.setLanguage = setLanguage;
    section.issueNumber = issueNumber; section.setIssueNumber = setIssueNumber;
    section.publicationFrequency = publicationFrequency; section.setPublicationFrequency = setPublicationFrequency;
    section.ISSN = ISSN; section.setISSN = setISSN;
    section.sections = sections; section.setSections = setSections;

    return section;
}
