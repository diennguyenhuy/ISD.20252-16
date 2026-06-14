import { useState } from 'react';
import { useNavigate } from 'react-router';
import { ArrowLeft, UserPlus, AlertCircle, ChevronDown, Loader2 } from 'lucide-react';
import { AdminService } from '../../api/AdminService';
import type {UserRole} from "../../models/user.interface";

interface FormData {
  username: string;
  email: string;
  roles: UserRole[];
}
type Errors = Partial<Record<keyof FormData, string | undefined>>;

function Field({ label, required, error, children }: { label: string; required?: boolean; error?: string | undefined; children: React.ReactNode }) {
  return (
    <div className="flex flex-col">
      <label className="text-sm font-bold text-foreground mb-2 flex items-center gap-1">
        {label} {required && <span className="text-destructive">*</span>}
      </label>
      {children}
      {error && (
        <p className="mt-1.5 text-xs font-semibold text-destructive flex items-center gap-1.5 animate-in slide-in-from-top-1">
          <AlertCircle size={14} className="shrink-0" /> {error}
        </p>
      )}
    </div>
  );
}

const inputClass = (err?: string) => {
  const base = "w-full border rounded-xl px-4 py-3.5 text-sm font-medium outline-none transition-all duration-200 text-foreground";
  const normal = "border-border bg-input-background focus:border-destructive focus:ring-1 focus:ring-destructive/50 placeholder:text-muted-foreground/50";
  const error = "border-destructive/50 bg-destructive/5 focus:border-destructive focus:ring-1 focus:ring-destructive/50";
  return `${base} ${err ? error : normal}`;
};

export default function AdminUserCreation() {
  const navigate = useNavigate();

  const [form, setForm] = useState<FormData>({
    username: '', email: '', roles: ['PRODUCT_MANAGER']
  });
  const [errors, setErrors] = useState<Errors>({});
  const [loading, setLoading] = useState(false);
  const [apiError, setApiError] = useState('');

  const update = (field: keyof FormData) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm(prev => ({ ...prev, [field]: e.target.value }));
    setErrors(prev => ({ ...prev, [field]: undefined }));
  };

  const validate = (): boolean => {
    const errs: Errors = {};
    if (!form.username.trim() || form.username.trim().length < 2) errs.username = 'Username must be at least 2 characters';
    if (!form.email.trim() || !/\S+@\S+\.\S+/.test(form.email)) errs.email = 'Please enter a valid email format';
    if (form.roles.length === 0) errs.roles = 'Please select at least one role' as any;
    
    setErrors(errs);
    if (Object.keys(errs).length > 0) {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;
    setLoading(true);
    setApiError('');

    try {
      await AdminService.createUser({
        username: form.username,
        email: form.email,
        roles: form.roles,
      });
      navigate('/admin');
    } catch (err: any) {
      setApiError(err.response?.data?.message || 'Failed to create user.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500 pb-10">
      
      {/* Header */}
      <div className="flex items-center gap-4 mb-8">
        <button 
          onClick={() => navigate('/admin')} 
          className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0"
        >
          <ArrowLeft size={20} />
        </button>
        <div>
          <h2 className="text-2xl font-extrabold tracking-tight text-foreground">Create User Account</h2>
          <p className="text-muted-foreground text-sm font-medium mt-1">Grant system access to new managers or administrators. A temporary password will be emailed.</p>
        </div>
      </div>

      <div className="bg-card rounded-3xl border border-border shadow-lg p-6 sm:p-10">
        
        {apiError && (
          <div className="mb-6 p-4 bg-destructive/10 border border-destructive/20 text-destructive rounded-xl flex items-center gap-3 font-bold">
            <AlertCircle size={20} className="shrink-0" /> {apiError}
          </div>
        )}

        {/* Avatar Placeholder Preview */}
        <div className="flex items-center gap-5 mb-8 pb-8 border-b border-border/50">
          <div className="w-20 h-20 rounded-full bg-destructive/10 text-destructive border-2 border-destructive/20 flex items-center justify-center text-3xl font-extrabold shadow-inner shrink-0">
            {form.username.charAt(0).toUpperCase() || '?'}
          </div>
          <div>
            <p className="font-bold text-lg text-foreground tracking-tight">{form.username || 'Username'}</p>
            <p className="text-sm font-medium text-muted-foreground mt-0.5">{form.email || 'email@aims.vn'}</p>
          </div>
        </div>

        {/* Form Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-6">
          <div className="sm:col-span-2">
            <Field label="Username" required error={errors.username}>
              <input value={form.username} onChange={update('username')} className={inputClass(errors.username)} placeholder="e.g., johndoe" />
            </Field>
          </div>

          <div className="sm:col-span-2">
            <Field label="Login Email" required error={errors.email}>
              <input type="email" value={form.email} onChange={update('email')} className={inputClass(errors.email)} placeholder="e.g., admin@aims.vn" />
            </Field>
          </div>

          <div className="sm:col-span-2 border-t border-border/50 pt-6 mt-2">
            <h3 className="text-lg font-bold text-foreground mb-4">Access Permissions</h3>
          </div>

          <div className="sm:col-span-2">
            <Field label="Roles" required error={errors.roles as any}>
              <div className="flex flex-col sm:flex-row gap-4">
                {[
                  { value: 'PRODUCT_MANAGER' as UserRole, label: 'Product Manager', desc: 'Manage products and orders' },
                  { value: 'ADMINISTRATOR' as UserRole, label: 'Administrator', desc: 'Full system control' }
                ].map(roleOption => {
                  const isSelected = form.roles.includes(roleOption.value);
                  return (
                    <label 
                      key={roleOption.value} 
                      className={`flex-1 flex items-start gap-3 p-4 rounded-xl border cursor-pointer transition-all ${
                        isSelected 
                          ? 'border-primary bg-primary/5 shadow-sm' 
                          : 'border-border bg-card hover:bg-muted'
                      }`}
                    >
                      <div className="mt-0.5">
                        <input
                          type="checkbox"
                          className="w-4 h-4 rounded border-border text-primary focus:ring-primary/50"
                          checked={isSelected}
                          onChange={(e) => {
                            const checked = e.target.checked;
                            setForm(prev => {
                              const newRoles = checked 
                                ? [...prev.roles, roleOption.value]
                                : prev.roles.filter(r => r !== roleOption.value);
                              return { ...prev, roles: newRoles };
                            });
                            setErrors(prev => ({ ...prev, roles: undefined }));
                          }}
                        />
                      </div>
                      <div>
                        <p className={`font-bold text-sm ${isSelected ? 'text-primary' : 'text-foreground'}`}>
                          {roleOption.label}
                        </p>
                        <p className="text-xs text-muted-foreground mt-0.5">
                          {roleOption.desc}
                        </p>
                      </div>
                    </label>
                  );
                })}
              </div>
            </Field>
          </div>
        </div>

        {/* Actions */}
        <div className="flex flex-col-reverse sm:flex-row gap-4 mt-10 pt-6 border-t border-border/50">
          <button 
            onClick={() => navigate('/admin')} 
            className="w-full sm:w-auto py-3.5 px-8 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="w-full py-3.5 px-8 rounded-xl bg-destructive text-destructive-foreground font-bold hover:opacity-90 flex items-center justify-center gap-2 transition-all shadow-md hover:shadow-lg hover:-translate-y-0.5 disabled:opacity-70"
          >
            {loading ? <Loader2 size={18} className="animate-spin" /> : <UserPlus size={18} />}
            {loading ? 'Creating...' : 'Create Account'}
          </button>
        </div>
      </div>
    </div>
  );
}
