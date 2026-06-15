import { useNavigate } from 'react-router';
import { LogOut, KeyRound } from 'lucide-react';
import { useAuth } from '../../context/AuthContext'; // Adjust path as needed

export default function SidebarUserProfile() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    // Safety check
    if (!user) return null;

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="p-4 border-t border-border bg-muted/10 flex flex-col gap-2">
            {/* User Info Badge */}
            <div className="mb-1 flex items-center gap-3 px-3 py-3 rounded-xl bg-card border border-border shadow-sm">
                <div className="w-10 h-10 rounded-full bg-primary text-primary-foreground flex items-center justify-center text-sm font-bold shrink-0 shadow-inner">
                    {user.username.charAt(0).toUpperCase()}
                </div>
                <div className="overflow-hidden text-left flex-1">
                    <p className="text-sm font-bold text-foreground truncate">{user.username}</p>
                    <p className="text-xs text-muted-foreground truncate">{user.email}</p>
                </div>
            </div>

            {/* Change Password Button */}
            <button
                onClick={() => navigate('/change-password')}
                className="w-full flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-foreground bg-card border border-border hover:bg-muted transition-colors shadow-sm"
            >
                <KeyRound size={16} className="text-muted-foreground" /> Change Password
            </button>

            {/* Logout Button */}
            <button
                onClick={handleLogout}
                className="w-full flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-destructive hover:bg-destructive/10 border border-transparent hover:border-destructive/20 transition-colors"
            >
                <LogOut size={16} /> Logout
            </button>
        </div>
    );
}