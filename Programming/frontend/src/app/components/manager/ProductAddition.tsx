import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router';
import { ArrowLeft, Save, AlertCircle, Package, FileText, LayoutGrid, Loader2, Lock } from 'lucide-react';
import { useProductManagement } from '../../hooks/useProductManagement';
import type { Book, CD, DVD, Newspaper } from '../../models/product.interface';

// Helper component hiển thị Input/Field
function Field({ label, required, error, disabled, children }: { label: string; required?: boolean; error?: string; disabled?: boolean; children: React.ReactNode }) {
  return (
      <div className="flex flex-col">
        <label className="text-sm font-bold text-foreground mb-2 flex items-center gap-1.5">
          {label}
          {required && !disabled && <span className="text-destructive">*</span>}
          {disabled && (
              <span title="Immutable field">
              <Lock size={12} className="text-muted-foreground ml-1" />
            </span>
          )}
        </label>
        {children}
        {error && <p className="mt-1.5 text-xs text-destructive font-semibold flex items-center gap-1.5"><AlertCircle size={14} className="shrink-0" /> {error}</p>}
      </div>
  );
}
const inputClass = (err?: string, disabled?: boolean) =>
    `w-full border rounded-xl px-4 py-3 text-sm outline-none transition-all duration-200 text-foreground 
  ${disabled ? 'bg-muted/50 border-border/50 text-muted-foreground cursor-not-allowed opacity-70' :
        err ? 'border-destructive/50 bg-destructive/5' : 'border-border bg-input-background focus:border-primary focus:ring-1 focus:ring-primary/50'}`;

export default function ProductAddition() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const { getProduct, createProduct, updateProduct } = useProductManagement();

  const isEdit = !!id;
  const [loadingData, setLoadingData] = useState(isEdit);

  // --- BASE STATE ---
  const [type, setType] = useState<'BOOK' | 'CD' | 'DVD' | 'NEWSPAPER'>('BOOK');
  const [title, setTitle] = useState('');
  const [barcode, setBarcode] = useState('');
  const [originalValue, setOriginalValue] = useState('');
  const [category, setCategory] = useState('');
  const [weight, setWeight] = useState('');
  const [height, setHeight] = useState('');
  const [width, setWidth] = useState('');
  const [length, setLength] = useState('');
  const [imageURL, setImageURL] = useState('');
  const [description, setDescription] = useState('');

  // --- SPECIFIC STATE ---
  const [author, setAuthor] = useState('');
  const [publisher, setPublisher] = useState('');
  const [publicationDate, setPublicationDate] = useState('');
  const [pages, setPages] = useState('');
  const [language, setLanguage] = useState('');
  const [genre, setGenre] = useState('');
  const [coverType, setCoverType] = useState<'HARDCOVER' | 'PAPERBACK'>('PAPERBACK');

  const [artist, setArtist] = useState('');
  const [recordLabel, setRecordLabel] = useState('');
  const [tracklist, setTracklist] = useState('');
  const [releaseDate, setReleaseDate] = useState('');

  const [studio, setStudio] = useState('');
  const [director, setDirector] = useState('');
  const [runtime, setRuntime] = useState('');
  const [subtitles, setSubtitles] = useState('');
  const [discType, setDiscType] = useState<'BLU_RAY' | 'HD_DVD'>('BLU_RAY');

  const [topic, setTopic] = useState(''); // Sections
  const [editorInChief, setEditorInChief] = useState('');
  const [issueNumber, setIssueNumber] = useState('');
  const [publicationFrequency, setPublicationFrequency] = useState('Daily');
  const [paperIssn, setPaperIssn] = useState('');

  const [submitError, setSubmitError] = useState<string | null>(null);

  useEffect(() => {
    if (isEdit && id) {
      getProduct(id).then(data => {
        if (data) {
          setType(data.productType.toUpperCase() as any);
          setTitle(data.title);
          setBarcode(data.barcode);
          setOriginalValue(String((data as any).originalValue || data.currentPrice));
          setWeight(String(data.weight));
          setHeight(String(data.height));
          setWidth(String(data.width));
          setLength(String(data.length));
          setCategory(data.category || '');
          setDescription(data.description || '');
          setImageURL(data.imageURL || '');

          if (data.productType.toUpperCase() === 'BOOK') {
            const b = data as Book;
            setAuthor(b.authors?.join(', ') || '');
            setPublisher(b.publisher || '');
            setPublicationDate(b.publicationDate || '');
            setPages(String(b.numberOfPages || ''));
            setLanguage(b.language || '');
            setGenre(b.genre || '');
            setCoverType(b.coverType || 'PAPERBACK');
          } else if (data.productType.toUpperCase() === 'CD') {
            const c = data as CD;
            setArtist(c.artists?.join(', ') || '');
            setRecordLabel(c.recordLabel || '');
            setReleaseDate(c.releaseDate || '');
            setGenre(c.genre || '');
            // Dù có tracklist cũng không gán để sửa ở đây, vì CD có UI quản lý riêng.
          } else if (data.productType.toUpperCase() === 'DVD') {
            const d = data as DVD;
            setStudio(d.studio || '');
            setDirector(d.director || '');
            setRuntime(String(d.runtime || ''));
            setLanguage(d.language || '');
            setSubtitles(d.subtitles?.join(', ') || '');
            setGenre(d.genre || '');
            setReleaseDate(d.releaseDate || '');
            setDiscType(d.discType || 'BLU_RAY');
          } else if (data.productType.toUpperCase() === 'NEWSPAPER') {
            const n = data as Newspaper;
            setPublisher(n.publisher || '');
            setPublicationDate(n.publicationDate || '');
            setLanguage(n.language || '');
            setTopic(n.sections?.join(', ') || '');
            setEditorInChief(n.editorInChief || '');
            setIssueNumber(String(n.issueNumber || ''));
            setPaperIssn(n.ISSN || '');
            setPublicationFrequency(n.publicationFrequency || 'Daily');
          }
        }
        setLoadingData(false);
      });
    }
  }, [id, isEdit]);

  const validateForm = () => {
    const errs: string[] = [];
    if (!title.trim()) errs.push('Title');
    if (!isEdit && !barcode.trim()) errs.push('Barcode');
    if (!isEdit && (!originalValue || Number(originalValue) <= 0)) errs.push('Original Value');

    if (errs.length > 0) {
      setSubmitError(`Please provide valid values for required fields: ${errs.join(', ')}`);
      window.scrollTo({ top: 0, behavior: 'smooth' });
      return false;
    }
    return true;
  };

  const handleSubmit = async () => {
    setSubmitError(null);
    if (!validateForm()) return;

    // DTO CƠ BẢN: Chứa các trường Update được
    const payload: any = {
      title,
      category, description, imageURL,
      weight: Number(weight || 0), height: Number(height || 0),
      width: Number(width || 0), length: Number(length || 0),
    };

    // NẾU LÀ CREATE: Bơm thêm các trường IMMUTABLE
    if (!isEdit) {
      payload.productType = type;
      payload.barcode = barcode;
      payload.originalValue = Number(originalValue);
    }

    // MAP TỪNG LOẠI SẢN PHẨM
    if (type === 'BOOK') {
      const authorsArray = author.split(',').map(s => s.trim()).filter(Boolean);
      Object.assign(payload, {
        authors: authorsArray.length ? authorsArray : [author],
        publisher, language, genre, numberOfPages: Number(pages)
      });
      if (!isEdit) Object.assign(payload, { publicationDate, coverType }); // Khóa ngày xuất bản, loại bìa
    }
    else if (type === 'CD') {
      const artistsArray = artist.split(',').map(s => s.trim()).filter(Boolean);
      Object.assign(payload, { artists: artistsArray.length ? artistsArray : [artist], recordLabel, genre });
      if (!isEdit) { // Khóa ngày phát hành, tracks ban đầu
        payload.releaseDate = releaseDate;
        payload.tracks = tracklist.split('\n').filter(Boolean).map(t => {
          const parts = t.split('|');
          return { title: parts[0], length: Number(parts[1] || 0) };
        });
      }
    }
    else if (type === 'DVD') {
      const subtitlesArray = subtitles.split(',').map(s => s.trim()).filter(Boolean);
      Object.assign(payload, { studio, director, runtime: Number(runtime), language, subtitles: subtitlesArray, genre });
      if (!isEdit) Object.assign(payload, { releaseDate, discType }); // Khóa ngày phát hành, loại đĩa
    }
    else if (type === 'NEWSPAPER') {
      const topicsArray = topic.split(',').map(s => s.trim()).filter(Boolean);
      Object.assign(payload, { publisher, language, sections: topicsArray.length ? topicsArray : [topic], editorInChief, publicationFrequency });
      if (!isEdit) Object.assign(payload, { publicationDate, issueNumber: Number(issueNumber), ISSN: paperIssn }); // Khóa ISSN, số phát hành
    }

    const result = isEdit ? await updateProduct(id!, payload) : await createProduct(payload);

    if (result.success) {
      navigate('/manager');
    } else {
      setSubmitError(result.error || 'Failed to save product. Please check your data.');
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  if(loadingData) return <div className="py-20 text-center"><Loader2 className="animate-spin inline text-primary" size={40} /></div>;

  return (
      <div className="max-w-4xl mx-auto animate-in fade-in pb-10">
        <div className="flex items-center gap-4 mb-8">
          <button onClick={() => navigate(-1)} className="p-2.5 bg-card border rounded-xl hover:bg-muted shadow-sm"><ArrowLeft size={20} /></button>
          <div>
            <h2 className="text-2xl font-extrabold">{isEdit ? 'Edit Product' : 'Add New Product'}</h2>
            <p className="text-muted-foreground text-sm mt-1">{isEdit ? `Editing Code: ${barcode} (Some fields are locked)` : 'Fill in all required fields'}</p>
          </div>
        </div>

        {submitError && (
            <div className="mb-6 p-4 bg-destructive/10 border border-destructive/20 text-destructive rounded-xl flex items-center gap-2 font-bold">
              <AlertCircle size={20} className="shrink-0" />
              {submitError}
            </div>
        )}

        <div className="bg-card rounded-3xl border shadow-lg p-6 sm:p-10">
          <div className="mb-10">
            <label className="flex items-center gap-2 text-base font-bold mb-4"><LayoutGrid size={18} className="text-primary"/> Product Type</label>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
              {(['BOOK', 'CD', 'DVD', 'NEWSPAPER'] as const).map(t => (
                  <button key={t} type="button" onClick={() => !isEdit && setType(t)} disabled={isEdit}
                          className={`py-3.5 px-4 rounded-xl border-2 text-sm font-bold ${type === t ? 'border-primary bg-primary/10 text-primary' : 'border-border bg-input-background'} ${isEdit && type !== t ? 'opacity-40 cursor-not-allowed' : ''}`}>
                    {t}
                  </button>
              ))}
            </div>
          </div>

          <div className="border-t border-border/50 pt-8 mb-8">
            <h3 className="text-lg font-bold mb-6 flex items-center gap-2"><Package size={20} className="text-primary"/> Basic Info</h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-5">
              <div className="sm:col-span-2"><Field label="Title" required><input value={title} onChange={e=>setTitle(e.target.value)} className={inputClass()} /></Field></div>

              {/* IMMUTABLE FIELDS */}
              <Field label="Barcode" required disabled={isEdit}><input value={barcode} onChange={e=>setBarcode(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>
              <Field label="Original Value (VND)" required disabled={isEdit}><input type="number" value={originalValue} onChange={e=>setOriginalValue(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>

              <Field label="Category"><input value={category} onChange={e=>setCategory(e.target.value)} className={inputClass()} /></Field>
              <Field label="Weight (kg)"><input type="number" value={weight} onChange={e=>setWeight(e.target.value)} className={inputClass()} /></Field>
              <Field label="Height (cm)"><input type="number" value={height} onChange={e=>setHeight(e.target.value)} className={inputClass()} /></Field>
              <Field label="Width (cm)"><input type="number" value={width} onChange={e=>setWidth(e.target.value)} className={inputClass()} /></Field>
              <Field label="Length (cm)"><input type="number" value={length} onChange={e=>setLength(e.target.value)} className={inputClass()} /></Field>
              <div className="sm:col-span-2"><Field label="Image URL"><input value={imageURL} onChange={e=>setImageURL(e.target.value)} className={inputClass()} /></Field></div>
              <div className="sm:col-span-2"><Field label="Description"><textarea value={description} onChange={e=>setDescription(e.target.value)} rows={4} className={inputClass()} /></Field></div>
            </div>
          </div>

          <div className="border-t border-border/50 pt-8 mb-10">
            <h3 className="text-lg font-bold mb-6 flex items-center gap-2"><FileText size={20} className="text-primary"/> Specific Info</h3>

            {type === 'BOOK' && (
                <div className="grid grid-cols-2 gap-5">
                  <Field label="Authors (comma separated)"><input value={author} onChange={e=>setAuthor(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Publisher"><input value={publisher} onChange={e=>setPublisher(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Pages"><input type="number" value={pages} onChange={e=>setPages(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Language"><input value={language} onChange={e=>setLanguage(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Genre"><input value={genre} onChange={e=>setGenre(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Publish Date" disabled={isEdit}><input type="date" value={publicationDate} onChange={e=>setPublicationDate(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>
                  <Field label="Cover" disabled={isEdit}><select value={coverType} onChange={e=>setCoverType(e.target.value as any)} disabled={isEdit} className={inputClass('', isEdit)}><option value="PAPERBACK">Paperback</option><option value="HARDCOVER">Hardcover</option></select></Field>
                </div>
            )}

            {type === 'CD' && (
                <div className="grid grid-cols-2 gap-5">
                  <Field label="Artists (comma separated)"><input value={artist} onChange={e=>setArtist(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Record Label"><input value={recordLabel} onChange={e=>setRecordLabel(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Genre"><input value={genre} onChange={e=>setGenre(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Release Date" disabled={isEdit}><input type="date" value={releaseDate} onChange={e=>setReleaseDate(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>

                  {!isEdit ? (
                      <div className="sm:col-span-2">
                        <Field label="Tracks (Title|Seconds)"><textarea value={tracklist} onChange={e=>setTracklist(e.target.value)} rows={4} className={inputClass()} placeholder="Song1|240&#10;Song2|180" /></Field>
                      </div>
                  ) : (
                      <div className="sm:col-span-2 p-4 rounded-xl bg-primary/5 border border-primary/20 text-sm text-primary font-medium flex items-center gap-2">
                        <Lock size={16} /> Tracklist can only be modified from the Product Details page.
                      </div>
                  )}
                </div>
            )}

            {type === 'DVD' && (
                <div className="grid grid-cols-2 gap-5">
                  <Field label="Director"><input value={director} onChange={e=>setDirector(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Studio"><input value={studio} onChange={e=>setStudio(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Runtime (Min)"><input type="number" value={runtime} onChange={e=>setRuntime(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Language"><input value={language} onChange={e=>setLanguage(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Genre"><input value={genre} onChange={e=>setGenre(e.target.value)} className={inputClass()} /></Field>
                  <div className="sm:col-span-2"><Field label="Subtitles (comma separated)"><input value={subtitles} onChange={e=>setSubtitles(e.target.value)} className={inputClass()} placeholder="English, Vietnamese..." /></Field></div>
                  <Field label="Release Date" disabled={isEdit}><input type="date" value={releaseDate} onChange={e=>setReleaseDate(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>
                  <Field label="Disc Type" disabled={isEdit}><select value={discType} onChange={e=>setDiscType(e.target.value as any)} disabled={isEdit} className={inputClass('', isEdit)}><option value="BLU_RAY">Blu Ray</option><option value="HD_DVD">HD-DVD</option></select></Field>
                </div>
            )}

            {type === 'NEWSPAPER' && (
                <div className="grid grid-cols-2 gap-5">
                  <Field label="Editor in Chief"><input value={editorInChief} onChange={e=>setEditorInChief(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Publisher"><input value={publisher} onChange={e=>setPublisher(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Language"><input value={language} onChange={e=>setLanguage(e.target.value)} className={inputClass()} /></Field>
                  <Field label="Frequency"><input value={publicationFrequency} onChange={e=>setPublicationFrequency(e.target.value)} className={inputClass()} /></Field>
                  <div className="sm:col-span-2"><Field label="Sections (comma separated)"><input value={topic} onChange={e=>setTopic(e.target.value)} className={inputClass()} /></Field></div>
                  <Field label="ISSN" disabled={isEdit}><input value={paperIssn} onChange={e=>setPaperIssn(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>
                  <Field label="Issue Number" disabled={isEdit}><input type="number" value={issueNumber} onChange={e=>setIssueNumber(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>
                  <Field label="Publish Date" disabled={isEdit}><input type="date" value={publicationDate} onChange={e=>setPublicationDate(e.target.value)} disabled={isEdit} className={inputClass('', isEdit)} /></Field>
                </div>
            )}
          </div>

          <div className="flex gap-4 pt-6 border-t">
            <button onClick={() => navigate(-1)} className="w-full py-3.5 rounded-xl border font-bold hover:bg-muted transition-colors">Cancel</button>
            <button onClick={handleSubmit} className="w-full py-3.5 rounded-xl bg-primary text-primary-foreground font-bold flex justify-center gap-2 hover:bg-accent transition-all shadow-md">
              <Save size={18} /> Save Changes
            </button>
          </div>
        </div>
      </div>
  );
}