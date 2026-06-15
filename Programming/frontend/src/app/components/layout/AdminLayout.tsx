import { Outlet, useNavigate, useLocation } from 'react-router';
import { Users, ChevronRight, Menu, X, Sun, Moon, Shield } from 'lucide-react';
import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import SidebarUserProfile from '../shared/SibebarUserProfile';

const NAV_ITEMS = [
  { path: '/admin', label: 'User Management', icon: Users, exact: false }
];

export default function AdminLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, hasRole } = useAuth();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    setIsDark(document.documentElement.classList.contains('dark'));
    if (!hasRole('ADMINISTRATOR')) {
      navigate('/login', { replace: true });
    }
  }, [hasRole, navigate]);

  const toggleTheme = () => {
    document.documentElement.classList.toggle('dark');
    setIsDark(!isDark);
  };

  const isActive = (path: string, exact = false) =>
      exact ? location.pathname === path : location.pathname.startsWith(path);

  if (!user) return null;

  return (
      <div className="min-h-screen bg-background flex transition-colors duration-300">
        {sidebarOpen && <div className="fixed inset-0 bg-background/80 backdrop-blur-sm z-30 lg:hidden" onClick={() => setSidebarOpen(false)} />}

        <aside className={`fixed lg:static inset-y-0 left-0 z-40 w-64 bg-card border-r border-border flex flex-col transform transition-transform duration-300 shadow-2xl lg:shadow-none ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}>
          <div className="p-5 border-b border-border bg-primary/5">
            <button onClick={() => navigate('/admin')} className="flex items-center gap-3 w-full group">
              <div className="w-10 h-10 rounded-xl bg-primary text-primary-foreground flex items-center justify-center shrink-0 shadow-lg group-hover:scale-105 transition-transform duration-300">
                <Shield size={24} />
              </div>
              <div className="text-left">
                <div className="text-foreground font-extrabold text-xl tracking-tight" style={{ fontFamily: 'Montserrat, sans-serif' }}>AIMS</div>
                <div className="text-[10px] uppercase tracking-widest text-primary font-bold">Admin Console</div>
              </div>
            </button>
          </div>

          <nav className="flex-1 p-4 space-y-2 overflow-y-auto hide-scrollbar">
            {NAV_ITEMS.map(item => (
                <button key={item.path} onClick={() => { navigate(item.path); setSidebarOpen(false); }} className={`w-full flex items-center gap-3 px-4 py-3.5 rounded-xl text-sm transition-all duration-200 ${isActive(item.path, item.exact) ? 'bg-primary/10 text-primary font-bold shadow-sm border border-primary/20' : 'text-muted-foreground hover:bg-muted hover:text-foreground font-medium border border-transparent'}`}>
                  <item.icon size={18} className={isActive(item.path, item.exact) ? "text-primary" : "opacity-70"} />
                  <span className="flex-1 text-left">{item.label}</span>
                  {isActive(item.path, item.exact) && <ChevronRight size={16} className="opacity-70" />}
                </button>
            ))}
          </nav>

          <SidebarUserProfile />

        </aside>

        <div className="flex-1 flex flex-col min-w-0 h-screen overflow-hidden">
          <header className="bg-card/80 backdrop-blur-md border-b border-border px-4 sm:px-6 py-4 flex items-center gap-4 sticky top-0 z-20 transition-colors duration-300">
            <button onClick={() => setSidebarOpen(o => !o)} className="lg:hidden p-2 -ml-2 rounded-lg text-muted-foreground hover:bg-muted hover:text-foreground transition-colors">
              {sidebarOpen ? <X size={22} /> : <Menu size={22} />}
            </button>
            <h1 className="text-foreground font-bold text-lg sm:text-xl tracking-tight">
              {NAV_ITEMS.find(i => isActive(i.path, i.exact))?.label ?? 'Admin Dashboard'}
            </h1>
            <div className="ml-auto flex items-center gap-2 sm:gap-3">
              <button onClick={toggleTheme} className="p-2.5 rounded-xl border border-border bg-background text-muted-foreground hover:bg-muted hover:text-foreground transition-colors shadow-sm">
                {isDark ? <Sun size={18} /> : <Moon size={18} />}
              </button>
            </div>
          </header>
          <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-y-auto bg-background">
            <div className="max-w-7xl mx-auto animate-in fade-in duration-500">
              <Outlet />
            </div>
          </main>
        </div>
      </div>
  );
}