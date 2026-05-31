import { useState } from 'react';
import { useNavigate } from 'react-router';
import { ShieldCheck, AlertCircle, ArrowLeft } from 'lucide-react';

export default function LoginSignup() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = () => {
    if(email === 'manager@aims.vn') navigate('/manager');
    else if(email === 'admin@aims.vn') navigate('/admin');
    else setError("Invalid credentials or unauthorized.");
  };

  return (
      <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4 relative">
        <div className="w-full max-w-md bg-card rounded-3xl shadow-2xl border p-8 space-y-6 relative z-10">
          <div className="text-center">
            <ShieldCheck size={40} className="mx-auto text-primary mb-2" />
            <h2 className="text-2xl font-bold">System Login</h2>
          </div>
          <div className="bg-primary/5 p-3 rounded-lg flex gap-2">
            <button onClick={()=>setEmail('manager@aims.vn')} className="flex-1 bg-card border rounded p-2 text-xs font-bold">Fill Manager</button>
            <button onClick={()=>setEmail('admin@aims.vn')} className="flex-1 bg-card border rounded p-2 text-xs font-bold">Fill Admin</button>
          </div>
          <input type="email" value={email} onChange={e=>setEmail(e.target.value)} className="w-full border p-3 rounded-xl bg-input-background" placeholder="Email" />
          <input type="password" value={password} onChange={e=>setPassword(e.target.value)} className="w-full border p-3 rounded-xl bg-input-background" placeholder="Password" />
          {error && <p className="text-destructive text-sm font-bold"><AlertCircle className="inline" size={16}/> {error}</p>}
          <button onClick={handleLogin} className="w-full bg-primary text-primary-foreground py-3 rounded-xl font-bold">Login</button>

          <div className="text-center pt-4 mt-2 border-t border-border">
            <button
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