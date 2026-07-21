import { useParams, useNavigate } from 'react-router';
import {
    ArrowLeft, UserCheck, UserX, Shield, ShieldOff, Key, Mail, Calendar, CheckCircle2, Loader2, AlertTriangle, Info
} from 'lucide-react';
import { AdminService } from '../../api/AdminService';
import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import type { UserRole, User } from "../../models/user.interface";
import { formatDateTime } from '../../data/formatter';

const ROLE_LABELS: Record<UserRole | 'customer', string> = {
    customer: 'Customer',
    PRODUCT_MANAGER: 'Product Manager',
    ADMINISTRATOR: 'Administrator',
};

const ROLE_COLORS: Record<UserRole | 'customer', string> = {
    customer: 'bg-secondary text-secondary-foreground border-border',
    PRODUCT_MANAGER: 'bg-primary/10 text-primary border-primary/20', // Keep PM blue for contrast
    ADMINISTRATOR: 'bg-destructive/10 text-destructive border-destructive/20',
};

const STATUS_LABELS: Record<string, string> = {
    active: 'Active',
    inactive: 'Deactivated',
    blocked: 'Blocked',
};

const STATUS_COLORS: Record<string, string> = {
    active: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20',
    inactive: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20',
    blocked: 'bg-destructive/10 text-destructive border-destructive/20',
};

export default function AdminUserDetail() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { user: currentUser } = useAuth();

    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [actionLoading, setActionLoading] = useState(false);

    // Modals & Action States
    const [confirmAction, setConfirmAction] = useState<string | null>(null);
    const [newRoles, setNewRoles] = useState<UserRole[]>([]);
    const [showRoleAssign, setShowRoleAssign] = useState(false);
    const [passwordResetDone, setPasswordResetDone] = useState(false);

    // Email Update States
    const [showEmailUpdate, setShowEmailUpdate] = useState(false);
    const [newEmail, setNewEmail] = useState('');
    const [emailUpdateDone, setEmailUpdateDone] = useState(false);

    useEffect(() => {
        if (id) {
            fetchUser(id);
        }
    }, [id]);

    const fetchUser = async (userId: string) => {
        try {
            setLoading(true);
            const res = await AdminService.getUser(userId);
            setUser(res.data);
        } catch (err: any) {
            setError('User not found or access denied.');
        } finally {
            setLoading(false);
        }
    };

    const getStatus = (u: User) => {
        if (u.blocked) return 'blocked';
        if (!u.active) return 'inactive';
        return 'active';
    };

    const handleAction = async (action: () => Promise<any>) => {
        setActionLoading(true);
        setError(null);
        try {
            await action();
            if (id) await fetchUser(id); // Refetch to show updated data
            setConfirmAction(null);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Action failed.');
        } finally {
            setActionLoading(false);
        }
    }

    const toggleActivation = () => {
        if (!user) return;
        handleAction(async () => {
            if (user.active) {
                await AdminService.deactivateUser(user.id);
            } else {
                await AdminService.activateUser(user.id);
            }
        });
    };

    const toggleBlock = () => {
        if (!user) return;
        handleAction(async () => {
            if (user.blocked) {
                await AdminService.unblockUser(user.id);
            } else {
                await AdminService.blockUser(user.id);
            }
        });
    };

    const assignRole = () => {
        if (!user) return;
        handleAction(async () => {
            await AdminService.assignRoles(user.id, newRoles);
            setShowRoleAssign(false);
        });
    };

    const triggerPasswordReset = () => {
        if (!user) return;
        if (currentUser?.id === user.id) {
            navigate('/change-password');
            return;
        }
        handleAction(async () => {
            await AdminService.resetPassword(user.id);
            setPasswordResetDone(true);
            setTimeout(() => setPasswordResetDone(false), 3000);
        });
    };

    const triggerEmailUpdate = () => {
        if (!user || !newEmail || newEmail === user.email) return;
        handleAction(async () => {
            await AdminService.updateEmail(user.id, newEmail); // API Call
            setShowEmailUpdate(false);
            setNewEmail(''); // Reset input
            setEmailUpdateDone(true);
            setTimeout(() => setEmailUpdateDone(false), 3000);
        });
    };

    if (loading) {
        // Changed spinner to destructive red
        return <div className="flex justify-center items-center h-64"><Loader2 className="animate-spin text-destructive" size={48} /></div>;
    }

    if (!user) {
        return (
            <div className="max-w-2xl mx-auto px-4 py-20 text-center animate-in fade-in duration-500">
                <UserX size={64} className="mx-auto mb-6 text-muted-foreground opacity-30" />
                <p className="text-xl text-foreground font-bold mb-2">User not found</p>
                <p className="text-muted-foreground mb-6">This user does not exist or has been deleted from the system.</p>
                <button
                    onClick={() => navigate('/admin')}
                    className="text-destructive font-semibold hover:opacity-80 transition-colors underline underline-offset-4"
                >
                    Back to list
                </button>
            </div>
        );
    }

    const status = getStatus(user);
    const isSelf = currentUser?.id === user.id;

    // Dynamic semantic button styling for the confirmation modal
    const getModalConfirmStyle = () => {
        if (confirmAction === 'toggle_activation') {
            return status === 'active'
                ? 'bg-amber-600 hover:bg-amber-700 shadow-amber-500/20 text-white' // Locking
                : 'bg-emerald-600 hover:bg-emerald-700 shadow-emerald-500/20 text-white'; // Activating
        }
        if (confirmAction === 'toggle_block') {
            return status === 'blocked'
                ? 'bg-emerald-600 hover:bg-emerald-700 shadow-emerald-500/20 text-white' // Unblocking
                : 'bg-destructive hover:bg-destructive/90 shadow-destructive/20 text-white'; // Blocking
        }
        // Default for Password Reset
        return 'bg-destructive hover:bg-destructive/90 shadow-destructive/20 text-white';
    };

    return (
        <div className="max-w-3xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500">
            {/* Header */}
            <div className="flex items-center gap-4 mb-8">
                <button
                    onClick={() => navigate('/admin')}
                    className="p-2.5 bg-card border border-border rounded-xl hover:bg-muted text-muted-foreground hover:text-foreground transition-colors shadow-sm shrink-0"
                >
                    <ArrowLeft size={20} />
                </button>
                <h2 className="text-2xl text-foreground font-bold tracking-tight">User Details</h2>
            </div>

            {/* Global Error Banner */}
            {error && (
                <div className="bg-destructive/10 border border-destructive/20 text-destructive rounded-2xl p-4 mb-6 text-sm font-bold flex items-center gap-2 animate-in zoom-in-95 duration-300 shadow-sm">
                    <AlertTriangle size={18} />
                    {error}
                </div>
            )}

            {/* Success Banners */}
            {passwordResetDone && (
                <div className="bg-emerald-500/10 border border-emerald-500/20 text-emerald-600 dark:text-emerald-400 rounded-2xl p-4 mb-6 text-sm font-bold flex items-center gap-2 animate-in zoom-in-95 duration-300 shadow-sm">
                    <CheckCircle2 size={18} />
                    Password reset email has been sent to <strong>{user.email}</strong>
                </div>
            )}

            {emailUpdateDone && (
                <div className="bg-emerald-500/10 border border-emerald-500/20 text-emerald-600 dark:text-emerald-400 rounded-2xl p-4 mb-6 text-sm font-bold flex items-center gap-2 animate-in zoom-in-95 duration-300 shadow-sm">
                    <CheckCircle2 size={18} />
                    Email successfully updated. A confirmation has been sent to <strong>{user.email}</strong>
                </div>
            )}

            {/* Profile Card */}
            <div className="bg-card rounded-3xl border border-border shadow-lg overflow-hidden mb-8 relative">
                <div className="relative px-6 py-10 text-center border-b border-border bg-muted/10 overflow-hidden">
                    {/* Changed secondary background glow to destructive/5 to match Admin theme */}
                    <div className="absolute top-0 right-0 w-64 h-64 bg-destructive/10 rounded-full blur-3xl -mr-32 -mt-32 pointer-events-none"></div>
                    <div className="absolute bottom-0 left-0 w-48 h-48 bg-destructive/5 rounded-full blur-3xl -ml-24 -mb-24 pointer-events-none"></div>

                    <div className="w-24 h-24 rounded-full bg-destructive/10 border-4 border-card flex items-center justify-center text-4xl font-extrabold text-destructive mx-auto mb-4 relative z-10 shadow-md">
                        {user.username.charAt(0).toUpperCase()}
                    </div>
                    <h2 className="text-2xl font-extrabold text-foreground relative z-10 tracking-tight">
                        {user.username} {isSelf && <span className="text-sm font-medium text-muted-foreground ml-2">(You)</span>}
                    </h2>
                    <p className="text-muted-foreground text-sm mt-1 mb-4 font-medium relative z-10">{user.email}</p>

                    <div className="flex justify-center flex-wrap gap-2 relative z-10">
                        {user.roles.map(r => {
                            const cleanRole = r.replace('ROLE_', '');
                            if (cleanRole !== 'ADMINISTRATOR' && cleanRole !== 'PRODUCT_MANAGER') return null;
                            return (
                                <span key={cleanRole} className={`text-xs font-bold px-3 py-1 rounded-full border shadow-sm ${ROLE_COLORS[cleanRole]}`}>
                      {ROLE_LABELS[cleanRole] || cleanRole}
                    </span>
                            );
                        })}
                        <span className={`text-xs font-bold px-3 py-1 rounded-full border shadow-sm ${STATUS_COLORS[status]}`}>
              {STATUS_LABELS[status]}
            </span>
                    </div>
                </div>

                <div className="p-6 sm:p-8">
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div className="flex items-center gap-3.5 bg-muted/30 p-4 rounded-2xl border border-border/50 shadow-inner sm:col-span-2">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><Mail size={16} className="text-muted-foreground" /></div>
                            <div className="min-w-0">
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Email</p>
                                <p className="text-sm text-foreground font-bold truncate">{user.email}</p>
                            </div>
                        </div>
                        <div className="flex items-center gap-3.5 bg-muted/30 p-4 rounded-2xl border border-border/50 shadow-inner sm:col-span-2">
                            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0"><Calendar size={16} className="text-muted-foreground" /></div>
                            <div className="min-w-0">
                                <p className="text-xs text-muted-foreground font-medium mb-0.5">Account Creation Time</p>
                                <p className="text-sm text-foreground font-bold truncate">{formatDateTime(user.createdAt)}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Admin Actions */}
            <div className="bg-card rounded-3xl border border-border shadow-md p-6 sm:p-8 space-y-2">
                <div className="mb-6 border-b border-border/50 pb-4 flex items-center justify-between">
                    <h3 className="text-foreground font-bold text-lg flex items-center gap-2">
                        <Shield size={20} className="text-destructive" />
                        Admin Actions
                    </h3>
                    {isSelf && (
                        <span className="flex items-center gap-1.5 text-xs font-bold text-amber-600 bg-amber-500/10 px-3 py-1 rounded-full">
              <Info size={14} /> Self-modification restricted
            </span>
                    )}
                </div>

                {/* Update Email */}
                <div className={`flex flex-col sm:flex-row sm:items-center justify-between py-4 border-b border-border/50 gap-4 ${isSelf ? 'opacity-50 pointer-events-none' : ''}`}>
                    <div>
                        <p className="text-sm font-bold text-foreground">Update Email Address</p>
                        <p className="text-xs font-medium text-muted-foreground mt-1">Change the primary contact email for this user</p>
                    </div>
                    {/* Replaced bg-primary/5 with bg-destructive/5 */}
                    <button
                        onClick={() => {
                            setNewEmail(user.email);
                            setShowEmailUpdate(true);
                        }}
                        disabled={isSelf}
                        className="flex items-center justify-center sm:justify-start gap-2 px-5 py-2.5 rounded-xl border border-destructive/30 text-destructive bg-destructive/5 hover:bg-destructive/10 text-sm font-bold transition-all shadow-sm"
                    >
                        <Mail size={18} />
                        Update Email
                    </button>
                </div>

                {/* Activate/Deactivate */}
                <div className={`flex flex-col sm:flex-row sm:items-center justify-between py-4 border-b border-border/50 gap-4 ${isSelf ? 'opacity-50 pointer-events-none' : ''}`}>
                    <div>
                        <p className="text-sm font-bold text-foreground">
                            {status === 'active' ? 'Deactivate Account' : 'Activate Account'}
                        </p>
                        <p className="text-xs font-medium text-muted-foreground mt-1">
                            {status === 'active'
                                ? 'Temporarily prevent the user from logging into the system'
                                : 'Allow the user to access the system again'}
                        </p>
                    </div>
                    <button
                        onClick={() => setConfirmAction('toggle_activation')}
                        disabled={isSelf}
                        className={`flex items-center justify-center sm:justify-start gap-2 px-5 py-2.5 rounded-xl text-sm font-bold transition-all shadow-sm ${
                            status === 'active'
                                ? 'border border-amber-500/30 text-amber-600 dark:text-amber-400 bg-amber-500/5 hover:bg-amber-500/10'
                                : 'border border-emerald-500/30 text-emerald-600 dark:text-emerald-400 bg-emerald-500/5 hover:bg-emerald-500/10'
                        }`}
                    >
                        {status === 'active' ? <UserX size={18} /> : <UserCheck size={18} />}
                        {status === 'active' ? 'Deactivate' : 'Activate'}
                    </button>
                </div>

                {/* Block/Unblock */}
                <div className={`flex flex-col sm:flex-row sm:items-center justify-between py-4 border-b border-border/50 gap-4 ${isSelf ? 'opacity-50 pointer-events-none' : ''}`}>
                    <div>
                        <p className="text-sm font-bold text-foreground">
                            {status === 'blocked' ? 'Unblock Account' : 'Block Account'}
                        </p>
                        <p className="text-xs font-medium text-muted-foreground mt-1">
                            {status === 'blocked'
                                ? 'Restore normal account status'
                                : 'Freeze account immediately due to policy violation'}
                        </p>
                    </div>
                    <button
                        onClick={() => setConfirmAction('toggle_block')}
                        disabled={isSelf}
                        className={`flex items-center justify-center sm:justify-start gap-2 px-5 py-2.5 rounded-xl text-sm font-bold transition-all shadow-sm ${
                            status === 'blocked'
                                ? 'border border-emerald-500/30 text-emerald-600 dark:text-emerald-400 bg-emerald-500/5 hover:bg-emerald-500/10'
                                : 'border border-destructive/30 text-destructive bg-destructive/5 hover:bg-destructive/10'
                        }`}
                    >
                        {status === 'blocked' ? <Shield size={18} /> : <ShieldOff size={18} />}
                        {status === 'blocked' ? 'Unblock' : 'Block'}
                    </button>
                </div>

                {/* Assign Role */}
                <div className={`flex flex-col sm:flex-row sm:items-center justify-between py-4 border-b border-border/50 gap-4 ${isSelf ? 'opacity-50 pointer-events-none' : ''}`}>
                    <div>
                        <p className="text-sm font-bold text-foreground">System Role Assignment</p>
                        <p className="text-xs font-medium text-muted-foreground mt-1">Current Roles: <span className="font-bold text-foreground">
              {user.roles.map(r => r.replace('ROLE_', '')).filter(r => r === 'ADMINISTRATOR' || r === 'PRODUCT_MANAGER').map(r => ROLE_LABELS[r] || r).join(', ')}
            </span></p>
                    </div>
                    {/* Replaced bg-primary/5 with bg-destructive/5 */}
                    <button
                        onClick={() => {
                            setShowRoleAssign(true);
                            setNewRoles(user.roles.map(r => r.replace('ROLE_', '')).filter(r => r === 'ADMINISTRATOR' || r === 'PRODUCT_MANAGER'));
                        }}
                        disabled={isSelf}
                        className="flex items-center justify-center sm:justify-start gap-2 px-5 py-2.5 rounded-xl border border-destructive/30 text-destructive bg-destructive/5 hover:bg-destructive/10 text-sm font-bold transition-all shadow-sm"
                    >
                        <Shield size={18} />
                        Change Role
                    </button>
                </div>

                {/* Password Reset / Change Password */}
                <div className="flex flex-col sm:flex-row sm:items-center justify-between py-4 gap-4">
                    <div>
                        <p className="text-sm font-bold text-foreground">{isSelf ? 'Change Your Password' : 'Reset Password'}</p>
                        <p className="text-xs font-medium text-muted-foreground mt-1">
                            {isSelf ? 'Navigate to the secure password change screen' : 'Send a temporary password via email'}
                        </p>
                    </div>
                    <button
                        onClick={() => isSelf ? triggerPasswordReset() : setConfirmAction('reset_password')}
                        className="flex items-center justify-center sm:justify-start gap-2 px-5 py-2.5 rounded-xl border border-border text-foreground bg-card hover:bg-muted text-sm font-bold transition-all shadow-sm"
                    >
                        <Key size={18} className="text-muted-foreground" />
                        {isSelf ? 'Change Password' : 'Send Request'}
                    </button>
                </div>
            </div>

            {/* Update Email Modal */}
            {showEmailUpdate && !isSelf && (
                <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <h3 className="font-bold text-lg text-foreground tracking-tight mb-3">Update Email Address</h3>
                        <p className="text-sm text-muted-foreground mb-5 leading-relaxed">
                            Enter the new email address for <span className="font-bold text-foreground">{user.username}</span>. Security notifications will be dispatched to both the old and new addresses.
                        </p>
                        <div className="mb-6">
                            {/* Added focus:border-destructive and ring-destructive */}
                            <input
                                type="email"
                                value={newEmail}
                                onChange={(e) => setNewEmail(e.target.value)}
                                placeholder="new.email@example.com"
                                className="w-full border border-border rounded-xl px-4 py-3 text-sm font-medium outline-none focus:border-destructive focus:ring-1 focus:ring-destructive/50 bg-input-background text-foreground transition-all shadow-sm"
                                autoFocus
                            />
                        </div>
                        <div className="flex gap-3">
                            <button
                                onClick={() => setShowEmailUpdate(false)}
                                className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm"
                            >
                                Cancel
                            </button>
                            {/* Changed submit button to destructive Red */}
                            <button
                                onClick={triggerEmailUpdate}
                                disabled={actionLoading || !newEmail || newEmail === user.email}
                                className="flex-1 py-3.5 rounded-xl bg-destructive text-destructive-foreground text-sm font-bold hover:bg-destructive/90 transition-all shadow-md flex justify-center items-center disabled:opacity-50 disabled:cursor-not-allowed"
                            >
                                {actionLoading ? <Loader2 size={16} className="animate-spin" /> : 'Save Changes'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Role Assign Modal */}
            {showRoleAssign && !isSelf && (
                <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <h3 className="font-bold text-lg text-foreground tracking-tight mb-5">Assign Role to <span className="text-destructive">{user.username}</span></h3>
                        <div className="flex flex-col gap-3 mb-6">
                            {[
                                { value: 'PRODUCT_MANAGER' as UserRole, label: 'Product Manager' },
                                { value: 'ADMINISTRATOR' as UserRole, label: 'Administrator' }
                            ].map(roleOption => {
                                const isSelected = newRoles.includes(roleOption.value);
                                return (
                                    <label
                                        key={roleOption.value}
                                        // Changed active border/bg to destructive
                                        className={`flex items-center gap-3 p-3.5 rounded-xl border cursor-pointer transition-all ${
                                            isSelected
                                                ? 'border-destructive bg-destructive/5 shadow-sm'
                                                : 'border-border bg-card hover:bg-muted'
                                        }`}
                                    >
                                        {/* Changed checkbox to text-destructive focus:ring-destructive */}
                                        <input
                                            type="checkbox"
                                            className="w-4 h-4 rounded border-border text-destructive focus:ring-destructive/50"
                                            checked={isSelected}
                                            onChange={(e) => {
                                                const checked = e.target.checked;
                                                setNewRoles(prev =>
                                                    checked
                                                        ? [...prev, roleOption.value]
                                                        : prev.filter(r => r !== roleOption.value)
                                                );
                                            }}
                                        />
                                        {/* Changed selected text to destructive */}
                                        <span className={`font-bold text-sm ${isSelected ? 'text-destructive' : 'text-foreground'}`}>
                      {roleOption.label}
                    </span>
                                    </label>
                                );
                            })}
                        </div>
                        <div className="flex gap-3">
                            <button
                                onClick={() => setShowRoleAssign(false)}
                                className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={assignRole}
                                disabled={actionLoading}
                                className="flex-1 py-3.5 rounded-xl bg-destructive text-destructive-foreground text-sm font-bold hover:opacity-90 transition-all shadow-md flex justify-center items-center"
                            >
                                {actionLoading ? <Loader2 size={16} className="animate-spin" /> : 'Save Changes'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Confirm Action Modal */}
            {confirmAction && !isSelf && (
                <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <h3 className="font-bold text-lg text-foreground tracking-tight mb-3">
                            {confirmAction === 'toggle_activation' && (status === 'active' ? 'Temporarily Lock Account' : 'Activate Account')}
                            {confirmAction === 'toggle_block' && (status === 'blocked' ? 'Unblock Account' : 'Block Account')}
                            {confirmAction === 'reset_password' && 'Confirm Password Reset'}
                        </h3>
                        <p className="text-muted-foreground text-sm font-medium mb-8 leading-relaxed">
                            {confirmAction === 'toggle_activation' && `Are you sure you want to ${status === 'active' ? 'temporarily lock' : 'reactivate'} the account of ${user.username}?`}
                            {confirmAction === 'toggle_block' && `Are you sure you want to ${status === 'blocked' ? 'unblock' : 'block'} the account of ${user.username}?`}
                            {confirmAction === 'reset_password' && `The system will send a password reset link to ${user.email}.`}
                        </p>
                        <div className="flex gap-3">
                            <button
                                onClick={() => setConfirmAction(null)}
                                className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={() => {
                                    if (confirmAction === 'toggle_activation') toggleActivation();
                                    else if (confirmAction === 'toggle_block') toggleBlock();
                                    else triggerPasswordReset();
                                }}
                                disabled={actionLoading}
                                // Applies the dynamic semantic colors
                                className={`flex-1 py-3.5 rounded-xl font-bold shadow-md hover:shadow-lg transition-all hover:-translate-y-0.5 flex justify-center items-center ${getModalConfirmStyle()}`}
                            >
                                {actionLoading ? <Loader2 size={16} className="animate-spin" /> : 'Confirm'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}