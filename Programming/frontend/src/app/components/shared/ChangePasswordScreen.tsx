import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { ShieldAlert, AlertCircle, Loader2, KeyRound } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { ProfileService } from '../../api/AdminService';

export default function ChangePasswordScreen() {
    const navigate = useNavigate();
    const { user, logout, token } = useAuth();

    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!user || !token) {
            navigate('/login', { replace: true });
        }
    }, [user, token, navigate]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (newPassword.length < 8) {
            setError('New password must be at least 8 characters long.');
            return;
        }

        if (newPassword !== confirmPassword) {
            setError('New password and confirmation do not match.');
            return;
        }

        if (currentPassword === newPassword) {
            setError('New password must be different from the current password.');
            return;
        }

        setLoading(true);

        try {
            await ProfileService.changePassword({
                currentPassword,
                newPassword,
                confirmPassword
            });

            setSuccess('Password changed successfully! Please log in again.');

            setTimeout(() => {
                logout();
                navigate('/login', { replace: true });
            }, 2500);

        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to change password. Please verify your current password.');
        } finally {
            setLoading(false);
        }
    };

    if (!user) return null;

    const isForced = user.mustChangePassword;

    return (
        <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4 relative">
            <div className="w-full max-w-md bg-card rounded-3xl shadow-2xl border p-8 space-y-6 relative z-10 animate-in fade-in slide-in-from-bottom-4 duration-500">
                <div className="text-center">
                    {isForced ? (
                        <div className="w-16 h-16 bg-amber-500/10 text-amber-600 rounded-full flex items-center justify-center mx-auto mb-4">
                            <ShieldAlert size={32} />
                        </div>
                    ) : (
                        <div className="w-16 h-16 bg-primary/10 text-primary rounded-full flex items-center justify-center mx-auto mb-4">
                            <KeyRound size={32} />
                        </div>
                    )}
                    <h2 className="text-2xl font-bold">Change Password</h2>
                    {isForced ? (
                        <p className="text-amber-600 text-sm mt-2 font-medium bg-amber-500/10 p-3 rounded-xl border border-amber-500/20">
                            You must change your temporary password before continuing.
                        </p>
                    ) : (
                        <p className="text-muted-foreground text-sm mt-1">
                            Update your account security credentials.
                        </p>
                    )}
                </div>

                {success ? (
                    <div className="bg-emerald-500/10 border border-emerald-500/20 text-emerald-600 p-4 rounded-xl text-center font-bold">
                        {success}
                    </div>
                ) : (
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div>
                            <label className="block text-xs font-bold text-muted-foreground mb-1.5 uppercase">Current Password</label>
                            <input
                                type="password"
                                required
                                value={currentPassword}
                                onChange={e => setCurrentPassword(e.target.value)}
                                className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
                                placeholder="Enter current password"
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-muted-foreground mb-1.5 uppercase">New Password</label>
                            <input
                                type="password"
                                required
                                minLength={8}
                                value={newPassword}
                                onChange={e => setNewPassword(e.target.value)}
                                className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
                                placeholder="At least 8 characters"
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-muted-foreground mb-1.5 uppercase">Confirm New Password</label>
                            <input
                                type="password"
                                required
                                minLength={8}
                                value={confirmPassword}
                                onChange={e => setConfirmPassword(e.target.value)}
                                className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
                                placeholder="Must match new password"
                            />
                        </div>

                        {error && (
                            <p className="text-destructive text-sm font-bold flex items-center gap-1.5 p-3 bg-destructive/10 rounded-xl border border-destructive/20">
                                <AlertCircle size={16} className="shrink-0" /> {error}
                            </p>
                        )}

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full bg-primary text-primary-foreground py-3.5 rounded-xl font-bold flex items-center justify-center gap-2 disabled:opacity-70 transition-all hover:bg-primary/90 mt-2"
                        >
                            {loading && <Loader2 size={18} className="animate-spin" />}
                            {loading ? 'Updating Password...' : 'Change Password'}
                        </button>
                    </form>
                )}

                {!isForced && !success && (
                    <div className="text-center pt-4 border-t border-border">
                        <button
                            type="button"
                            onClick={() => navigate(-1)}
                            className="text-sm text-muted-foreground hover:text-foreground font-bold transition-colors"
                        >
                            Cancel
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}
