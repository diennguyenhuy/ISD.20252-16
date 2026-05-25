import { Outlet, useNavigate, useLocation } from 'react-router';
import { ShoppingCart, Moon, Sun, User } from 'lucide-react';
import { useState, useEffect } from 'react';
import { useCart } from '../../context/CartContext';

export default function CustomerLayout() {
    const { totalQuantity } = useCart();

    const navigate = useNavigate();
    const location = useLocation();

    const [isDark, setIsDark] = useState(false);

    useEffect(() => {
        setIsDark(document.documentElement.classList.contains('dark'));
    }, []);

    const toggleTheme = () => {
        document.documentElement.classList.toggle('dark');
        setIsDark(!isDark);
    };

    return (
        <div className="min-h-screen bg-background text-foreground flex flex-col transition-colors duration-300">
            <header
                className="bg-background/80 backdrop-blur-lg border-b border-border sticky top-0 z-50 transition-colors duration-300">
                <div className="max-w-7xl mx-auto px-4 py-3 flex items-center justify-between">

                    {/* Logo */}
                    <button onClick={() => navigate('/')}
                        className="flex items-center gap-3 hover:opacity-80 transition-opacity group">
                        <img src="/favicon.png" alt="AIMS Logo"
                            className="w-10 h-10 object-contain drop-shadow-md group-hover:scale-105 transition-transform duration-300" />
                        <div className="flex flex-col items-start">
                            <span className="text-2xl tracking-tight text-foreground font-extrabold"
                                style={{ fontFamily: 'Montserrat, sans-serif' }}>AIMS</span>
                            <span
                                className="text-[10px] uppercase tracking-widest text-muted-foreground font-semibold hidden sm:block">An Internet Media Store</span>
                        </div>
                    </button>

                    {/* Right Actions */}
                    <div className="flex items-center gap-2 sm:gap-3">
                        <button onClick={toggleTheme}
                            className="p-2.5 rounded-xl text-muted-foreground hover:bg-muted hover:text-foreground transition-colors">
                            {isDark ? <Sun size={20} /> : <Moon size={20} />}
                        </button>

                        {/* Cart Button */}
                        <button
                            onClick={() => navigate('/cart')}
                            className={`relative flex items-center gap-2 px-4 py-2.5 rounded-xl font-semibold transition-all duration-200 ${location.pathname === '/cart'
                                ? 'bg-primary text-primary-foreground shadow-md shadow-primary/20'
                                : 'bg-card text-foreground border border-border hover:border-primary/50 hover:bg-muted/50'
                                }`}
                        >
                            <ShoppingCart size={20}
                                className={location.pathname === '/cart' ? 'text-primary-foreground' : 'text-primary'} />
                            <span className="hidden sm:inline text-sm">Cart</span>

                            {/* Cart Badge - Reactive to Global State! */}
                            {totalQuantity > 0 && (
                                <span
                                    className="absolute -top-2 -right-2 bg-destructive text-destructive-foreground text-[11px] rounded-full min-w-[22px] h-[22px] px-1.5 flex items-center justify-center font-bold shadow-sm ring-2 ring-background animate-in zoom-in">
                                    {totalQuantity > 99 ? '99+' : totalQuantity}
                                </span>
                            )}
                        </button>

                        <button onClick={() => navigate('/login')}
                            className="flex items-center gap-2 px-3 py-2.5 rounded-xl font-semibold transition-all duration-200 bg-card text-foreground border border-border hover:border-primary/50 hover:text-primary shadow-sm">
                            <User size={18} className="text-primary" />
                            <span className="hidden sm:inline text-sm">Login/Sign Up</span>
                        </button>
                    </div>
                </div>
            </header>

            <main className="flex-1 flex flex-col">
                <Outlet />
            </main>

            <footer className="bg-card border-t border-border py-8 mt-auto transition-colors duration-300">
                <div className="max-w-7xl mx-auto px-4 text-center">
                    <p className="text-muted-foreground text-sm font-medium">© 2026 AIMS - An Internet Media Store. All rights reserved.</p>
                    <div className="flex items-center justify-center gap-4 mt-3 text-sm">
                        <span className="text-primary font-bold">Hotline: 1800-AIMS</span>
                        <span className="text-border">|</span>
                        <span className="text-primary font-bold">support@aims.vn</span>
                    </div>
                </div>
            </footer>
        </div>
    );
}