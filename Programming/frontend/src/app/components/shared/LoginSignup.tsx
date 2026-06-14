import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import { ShieldCheck, AlertCircle, ArrowLeft, Loader2, Shield, Package } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { apiClient } from '../../api/client';

export default function LoginSignup() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // After login, if user has multiple roles, show role selector
  const [showRoleSelector, setShowRoleSelector] = useState(false);
  const [loginRoles, setLoginRoles] = useState<string[]>([]);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await apiClient.post('/api/auth/login', { email, password });
      const { token, id, username, email: userEmail, roles, mustChangePassword } = response.data;

      // Save auth state
      login(token, { id, username, email: userEmail, roles, mustChangePassword });

      // If must change password, redirect to change-password page
      if (mustChangePassword) {
        navigate('/change-password');
        return;
      }

      // Check roles for navigation
      const isAdmin = roles.includes('ROLE_ADMINISTRATOR') || roles.includes('ADMINISTRATOR');
      const isManager = roles.includes('ROLE_PRODUCT_MANAGER') || roles.includes('PRODUCT_MANAGER');

      if (isAdmin && isManager) {
        // Both roles — show the role selector
        setLoginRoles(roles);
        setShowRoleSelector(true);
      } else if (isAdmin) {
        navigate('/admin');
      } else if (isManager) {
        navigate('/manager');
      } else {
        navigate('/');
      }
    } catch (err: any) {
      if (err.response?.status === 401) {
        setError('Invalid credentials.');
      } else if (err.response?.status === 403) {
        setError(err.response?.data?.message || 'Access denied.');
      } else {
        setError(err.response?.data?.message || 'Server connection error.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleRoleSelect = (path: string) => {
    setShowRoleSelector(false);
    navigate(path);
  };

  if (showRoleSelector) {
    return (
      <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4">
        <div className="w-full max-w-md bg-card rounded-3xl shadow-2xl border p-8 space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
          <div className="text-center">
            <ShieldCheck size={40} className="mx-auto text-primary mb-2" />
            <h2 className="text-2xl font-bold">Choose Dashboard</h2>
            <p className="text-muted-foreground text-sm mt-1">
              You have multiple roles. Where would you like to go?
            </p>
          </div>

          <div className="space-y-3">
            {(loginRoles.includes('ROLE_ADMINISTRATOR') || loginRoles.includes('ADMINISTRATOR')) && (
              <button
                onClick={() => handleRoleSelect('/admin')}
                className="w-full flex items-center gap-4 p-4 rounded-2xl border border-border bg-card hover:border-primary/40 hover:bg-primary/5 transition-all duration-200 group"
              >
                <div className="p-3 rounded-xl bg-primary/10 text-primary group-hover:bg-primary group-hover:text-primary-foreground transition-colors">
                  <Shield size={24} />
                </div>
                <div className="text-left flex-1">
                  <p className="font-bold text-foreground">Admin Dashboard</p>
                  <p className="text-xs text-muted-foreground">Manage users, roles, and system settings</p>
                </div>
                <ArrowLeft size={18} className="text-muted-foreground rotate-180 group-hover:text-primary transition-colors" />
              </button>
            )}

            {(loginRoles.includes('ROLE_PRODUCT_MANAGER') || loginRoles.includes('PRODUCT_MANAGER')) && (
              <button
                onClick={() => handleRoleSelect('/manager')}
                className="w-full flex items-center gap-4 p-4 rounded-2xl border border-border bg-card hover:border-primary/40 hover:bg-primary/5 transition-all duration-200 group"
              >
                <div className="p-3 rounded-xl bg-amber-500/10 text-amber-600 group-hover:bg-amber-500 group-hover:text-white transition-colors">
                  <Package size={24} />
                </div>
                <div className="text-left flex-1">
                  <p className="font-bold text-foreground">Manager Dashboard</p>
                  <p className="text-xs text-muted-foreground">Manage products, orders, and inventory</p>
                </div>
                <ArrowLeft size={18} className="text-muted-foreground rotate-180 group-hover:text-primary transition-colors" />
              </button>
            )}
          </div>

          <div className="text-center pt-2 border-t border-border">
            <button
              type="button"
              onClick={() => navigate('/')}
              className="text-sm text-muted-foreground hover:text-primary font-bold transition-colors flex items-center justify-center gap-2 mx-auto"
            >
              <ArrowLeft size={16} /> Back to Store
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4 relative">
      <div className="w-full max-w-md bg-card rounded-3xl shadow-2xl border p-8 space-y-6 relative z-10 animate-in fade-in slide-in-from-bottom-4 duration-500">
        <div className="text-center">
          <ShieldCheck size={40} className="mx-auto text-primary mb-2" />
          <h2 className="text-2xl font-bold">System Login</h2>
          <p className="text-muted-foreground text-sm mt-1">Staff access only. No self-registration.</p>
        </div>

        <form onSubmit={handleLogin} className="space-y-4">
          <input
            type="email"
            required
            value={email}
            onChange={e => setEmail(e.target.value)}
            className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
            placeholder="Email"
          />
          <input
            type="password"
            required
            value={password}
            onChange={e => setPassword(e.target.value)}
            className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
            placeholder="Password"
          />

          {error && (
            <p className="text-destructive text-sm font-bold flex items-center gap-1.5">
              <AlertCircle size={16}/> {error}
            </p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-primary text-primary-foreground py-3 rounded-xl font-bold flex items-center justify-center gap-2 disabled:opacity-70 transition-all hover:bg-primary/90"
          >
            {loading && <Loader2 size={18} className="animate-spin" />}
            {loading ? 'Authenticating...' : 'Login'}
          </button>
        </form>

        <div className="text-center pt-4 mt-2 border-t border-border">
          <button
            type="button"
            onClick={() => navigate('/')}
            className="text-sm text-muted-foreground hover:text-primary font-bold transition-colors flex items-center justify-center gap-2 mx-auto"
          >
            <ArrowLeft size={16} /> Back to Store
          </button>
        </div>
      </div>
    </div>
  );
}