import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router';
import {
    Search, Plus, UserCheck, UserX, Shield, ShieldOff, Eye, ChevronDown, Users, Loader2, AlertTriangle
} from 'lucide-react';
import {AdminService} from '../../api/AdminService';
import {useAuth} from '../../context/AuthContext';
import type {UserRole, User} from "../../models/user.interface";
import {formatDate} from '../../data/formatter';

const ROLE_LABELS: Record<UserRole | 'customer', string> = {
    customer: 'Customer',
    PRODUCT_MANAGER: 'Product Manager',
    ADMINISTRATOR: 'Administrator',
};

// We keep Product Manager blue (primary) to easily distinguish it from the Red (destructive) Admins
const ROLE_COLORS: Record<UserRole | 'customer', string> = {
    customer: 'bg-secondary text-secondary-foreground border-border',
    PRODUCT_MANAGER: 'bg-primary/10 text-primary border-primary/20',
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

export default function AdminUserList() {
    const navigate = useNavigate();
    const {user: currentUser} = useAuth();

    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [actionLoading, setActionLoading] = useState(false);
    const [confirmAction, setConfirmAction] = useState<{ user: User, type: 'activation' | 'block' } | null>(null);

    const [search, setSearch] = useState('');
    const [filterRole, setFilterRole] = useState<UserRole | 'customer' | 'all'>('all');
    const [filterStatus, setFilterStatus] = useState<string>('all');

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const res = await AdminService.getUsers(0, 100);
            setUsers(res.data.content);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to load users');
        } finally {
            setLoading(false);
        }
    };

    const getStatus = (u: User) => {
        if (u.blocked) return 'blocked';
        if (!u.active) return 'inactive';
        return 'active';
    };

    const getMainRole = (u: User) => {
        if (u.roles.includes('ADMINISTRATOR')) return 'ADMINISTRATOR';
        if (u.roles.includes('PRODUCT_MANAGER')) return 'PRODUCT_MANAGER';
        return 'customer';
    };

    const filtered = users.filter(u => {
        const q = search.toLowerCase();
        const matchSearch = !q || u.username.toLowerCase().includes(q) || u.email.toLowerCase().includes(q);
        const mainRole = getMainRole(u);
        const matchRole = filterRole === 'all' || mainRole === filterRole;
        const status = getStatus(u);
        const matchStatus = filterStatus === 'all' || status === filterStatus;

        return mainRole !== 'customer' && matchSearch && matchRole && matchStatus;
    });

    const executeAction = async () => {
        if (!confirmAction) return;
        const {user, type} = confirmAction;

        setActionLoading(true);
        setError(null);
        try {
            if (type === 'activation') {
                if (user.active) await AdminService.deactivateUser(user.id);
                else await AdminService.activateUser(user.id);
            } else if (type === 'block') {
                if (user.blocked) await AdminService.unblockUser(user.id);
                else await AdminService.blockUser(user.id);
            }
            await fetchUsers();
        } catch (err: any) {
            setError(err.response?.data?.message || 'Action failed to execute.');
        } finally {
            setActionLoading(false);
            setConfirmAction(null);
        }
    };

    // Uses the red destructive focus rings
    const inputClass = "border border-border rounded-xl px-4 py-2.5 text-sm outline-none focus:border-destructive focus:ring-1 focus:ring-destructive/50 bg-input-background text-foreground transition-all";

    const staffUsers = users.filter(u => getMainRole(u) !== 'customer');

    if (loading && users.length === 0) {
        // Changed spinner to destructive red
        return <div className="flex justify-center items-center h-64"><Loader2 className="animate-spin text-destructive"
                                                                               size={48}/></div>;
    }

    // Dynamic button styles for the confirmation modal based on action safety
    const getModalConfirmStyle = () => {
        if (!confirmAction) return '';
        if (confirmAction.type === 'activation') {
            return confirmAction.user.active
                ? 'bg-amber-600 hover:bg-amber-700 shadow-amber-500/20 text-white' // Locking
                : 'bg-emerald-600 hover:bg-emerald-700 shadow-emerald-500/20 text-white'; // Activating
        }
        if (confirmAction.type === 'block') {
            return confirmAction.user.blocked
                ? 'bg-emerald-600 hover:bg-emerald-700 shadow-emerald-500/20 text-white' // Unblocking
                : 'bg-destructive hover:bg-destructive/90 shadow-destructive/20 text-destructive-foreground'; // Blocking
        }
        return 'bg-destructive hover:bg-destructive/90 shadow-destructive/20';
    };

    return (
        <div className="animate-in fade-in duration-500">
            {/* Header */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
                <div>
                    <h2 className="text-foreground text-2xl font-bold tracking-tight">User Management (Staff)</h2>
                    <p className="text-muted-foreground text-sm mt-1 font-medium">System has recorded a total
                        of {staffUsers.length} staff accounts</p>
                </div>
                <button
                    onClick={() => navigate('/admin/users/create')}
                    className="flex items-center justify-center gap-2 bg-destructive text-destructive-foreground px-5 py-2.5 rounded-xl hover:opacity-90 transition-all shadow-md hover:shadow-destructive/20 hover:-translate-y-0.5 text-sm font-bold"
                >
                    <Plus size={18}/>
                    Create New Account
                </button>
            </div>

            {error && (
                <div
                    className="p-4 bg-destructive/10 border border-destructive/20 text-destructive rounded-xl flex items-center gap-3 font-bold mb-6">
                    <AlertTriangle size={20} className="shrink-0"/>
                    {error}
                    <button onClick={() => setError(null)}
                            className="ml-auto underline text-sm hover:text-destructive/80">Dismiss
                    </button>
                </div>
            )}

            {/* Stats Cards */}
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-8">
                {([
                    // Changed Total card to use Destructive Red
                    {
                        label: 'Total',
                        value: staffUsers.length,
                        color: 'text-destructive',
                        bg: 'bg-destructive/5 border-destructive/20'
                    },
                    {
                        label: 'Active',
                        value: staffUsers.filter(u => getStatus(u) === 'active').length,
                        color: 'text-emerald-600 dark:text-emerald-400',
                        bg: 'bg-emerald-500/5 border-emerald-500/20'
                    },
                    {
                        label: 'Deactivated',
                        value: staffUsers.filter(u => getStatus(u) === 'inactive').length,
                        color: 'text-amber-600 dark:text-amber-400',
                        bg: 'bg-amber-500/5 border-amber-500/20'
                    },
                    {
                        label: 'Blocked',
                        value: staffUsers.filter(u => getStatus(u) === 'blocked').length,
                        color: 'text-destructive',
                        bg: 'bg-destructive/5 border-destructive/20'
                    },
                ]).map(stat => (
                    <div key={stat.label} className={`p-5 rounded-2xl border shadow-sm ${stat.bg}`}>
                        <p className={`text-3xl font-extrabold tracking-tight ${stat.color}`}>{stat.value}</p>
                        <p className="text-xs font-semibold mt-1.5 text-muted-foreground uppercase tracking-wider">{stat.label}</p>
                    </div>
                ))}
            </div>

            {/* Filters */}
            <div className="flex flex-wrap gap-3 mb-6">
                <div className="flex-1 min-w-[240px] relative">
                    <Search size={18} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground"/>
                    <input
                        type="text"
                        placeholder="Search by name, email..."
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                        className={`w-full pl-10 pr-4 ${inputClass}`}
                    />
                </div>
                <div className="relative min-w-[180px]">
                    <select
                        value={filterRole}
                        onChange={e => setFilterRole(e.target.value as UserRole | 'customer' | 'all')}
                        className={`w-full appearance-none pr-10 cursor-pointer ${inputClass}`}
                    >
                        <option value="all">All Roles</option>
                        <option value="PRODUCT_MANAGER">Product Manager</option>
                        <option value="ADMINISTRATOR">Administrator</option>
                    </select>
                    <ChevronDown size={16}
                                 className="absolute right-3.5 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none"/>
                </div>
                <div className="relative min-w-[180px]">
                    <select
                        value={filterStatus}
                        onChange={e => setFilterStatus(e.target.value)}
                        className={`w-full appearance-none pr-10 cursor-pointer ${inputClass}`}
                    >
                        <option value="all">All Statuses</option>
                        <option value="active">Active</option>
                        <option value="inactive">Deactivated</option>
                        <option value="blocked">Blocked</option>
                    </select>
                    <ChevronDown size={16}
                                 className="absolute right-3.5 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none"/>
                </div>
            </div>

            {/* Users Table */}
            <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full text-sm">
                        <thead>
                        <tr className="bg-muted/30 border-b border-border">
                            <th className="text-left px-5 py-4 font-semibold text-muted-foreground uppercase tracking-wider text-xs">Account</th>
                            <th className="text-left px-5 py-4 font-semibold text-muted-foreground uppercase tracking-wider text-xs">Role</th>
                            <th className="text-left px-5 py-4 font-semibold text-muted-foreground uppercase tracking-wider text-xs">Status</th>
                            <th className="text-left px-5 py-4 font-semibold text-muted-foreground uppercase tracking-wider text-xs">Created
                                Date
                            </th>
                            <th className="text-right px-5 py-4 font-semibold text-muted-foreground uppercase tracking-wider text-xs">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filtered.length === 0 ? (
                            <tr>
                                <td colSpan={5} className="px-5 py-16 text-center">
                                    <Users size={32} className="mx-auto mb-3 text-muted-foreground opacity-30"/>
                                    <p className="text-foreground font-medium text-base">No accounts found</p>
                                    <p className="text-muted-foreground mt-1 text-sm">Try adjusting the search keyword
                                        or filters</p>
                                </td>
                            </tr>
                        ) : filtered.map(user => {
                            const mainRole = getMainRole(user);
                            const status = getStatus(user);
                            const isSelf = currentUser?.id === user.id;

                            return (
                                <tr
                                    key={user.id}
                                    className="border-b border-border/50 hover:bg-muted/30 cursor-pointer transition-colors"
                                    onClick={() => navigate(`/admin/users/${user.id}`)}
                                >
                                    <td className="px-5 py-4">
                                        <div className="flex items-center gap-4">
                                            {/* Changed Avatar to Destructive Red */}
                                            <div
                                                className="w-10 h-10 rounded-full bg-destructive/10 text-destructive border border-destructive/20 flex items-center justify-center text-sm font-extrabold shrink-0 shadow-inner">
                                                {user.username.charAt(0).toUpperCase()}
                                            </div>
                                            <div>
                                                <p className="text-foreground font-bold">
                                                    {user.username} {isSelf && <span
                                                    className="text-muted-foreground font-medium text-xs ml-1">(You)</span>}
                                                </p>
                                                <p className="text-muted-foreground font-medium text-xs mt-0.5">{user.email}</p>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-5 py-4 flex flex-wrap gap-1.5">
                                        {user.roles.map(r => {
                                            const cleanRole = r.replace('ROLE_', '');
                                            if (cleanRole !== 'ADMINISTRATOR' && cleanRole !== 'PRODUCT_MANAGER') return null;
                                            return (
                                                <span key={cleanRole} className={`inline-flex items-center text-xs px-2.5 py-1 rounded-md font-bold border shadow-sm ${ROLE_COLORS[cleanRole]}`}>
                                                    {ROLE_LABELS[cleanRole] || cleanRole}
                                                </span>
                                            );
                                        })}
                                    </td>
                                    <td className="px-5 py-4">
                                        <span className={`inline-flex items-center text-xs px-2.5 py-1 rounded-md font-bold border shadow-sm ${STATUS_COLORS[status]}`}>
                                            {STATUS_LABELS[status]}
                                        </span>
                                    </td>
                                    <td className="px-5 py-4 text-muted-foreground font-medium">{formatDate(user.createdAt)}</td>
                                    <td className="px-5 py-4">
                                        <div className="flex items-center justify-end gap-1.5"
                                             onClick={e => e.stopPropagation()}>
                                            {/* Changed hover color to destructive */}
                                            <button
                                                onClick={() => navigate(`/admin/users/${user.id}`)}
                                                className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors"
                                                title="View Details"
                                            >
                                                <Eye size={18}/>
                                            </button>

                                            {/* Activate / Deactivate Button */}
                                            <button
                                                onClick={() => setConfirmAction({user, type: 'activation'})}
                                                disabled={isSelf}
                                                className={`p-2 rounded-lg transition-colors ${
                                                    isSelf ? 'opacity-30 cursor-not-allowed' :
                                                        status === 'active'
                                                            ? 'text-muted-foreground hover:text-amber-500 hover:bg-amber-500/10'
                                                            : 'text-muted-foreground hover:text-emerald-500 hover:bg-emerald-500/10'
                                                }`}
                                                title={isSelf ? 'Cannot modify own status' : status === 'active' ? 'Deactivate Account' : 'Activate Account'}
                                            >
                                                {status === 'active' ? <UserX size={18}/> : <UserCheck size={18}/>}
                                            </button>

                                            {/* Block / Unblock Button */}
                                            <button
                                                onClick={() => setConfirmAction({user, type: 'block'})}
                                                disabled={isSelf}
                                                className={`p-2 rounded-lg transition-colors ${
                                                    isSelf ? 'opacity-30 cursor-not-allowed' :
                                                        status === 'blocked'
                                                            ? 'text-muted-foreground hover:text-emerald-500 hover:bg-emerald-500/10'
                                                            : 'text-muted-foreground hover:text-destructive hover:bg-destructive/10'
                                                }`}
                                                title={isSelf ? 'Cannot block own account' : status === 'blocked' ? 'Unblock Account' : 'Block Account'}
                                            >
                                                {status === 'blocked' ? <Shield size={18}/> : <ShieldOff size={18}/>}
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* Confirmation Modal */}
            {confirmAction && (
                <div
                    className="fixed inset-0 bg-background/80 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-in fade-in duration-200">
                    <div
                        className="bg-card rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-border animate-in zoom-in-95 duration-200">
                        <h3 className="font-bold text-lg text-foreground tracking-tight mb-3">
                            {confirmAction.type === 'activation' && (confirmAction.user.active ? 'Temporarily Lock Account' : 'Activate Account')}
                            {confirmAction.type === 'block' && (confirmAction.user.blocked ? 'Unblock Account' : 'Block Account')}
                        </h3>
                        <p className="text-muted-foreground text-sm font-medium mb-8 leading-relaxed">
                            {confirmAction.type === 'activation' && `Are you sure you want to ${confirmAction.user.active ? 'temporarily lock' : 'reactivate'} the account of `}
                            {confirmAction.type === 'block' && `Are you sure you want to ${confirmAction.user.blocked ? 'unblock' : 'block'} the account of `}
                            <span className="font-bold text-foreground">{confirmAction.user.username}</span>?
                        </p>
                        <div className="flex gap-3">
                            <button
                                onClick={() => setConfirmAction(null)}
                                className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-bold hover:bg-muted transition-colors shadow-sm"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={executeAction}
                                disabled={actionLoading}
                                // Applies the dynamic semantic colors calculated above
                                className={`flex-1 py-3.5 rounded-xl font-bold shadow-md hover:shadow-lg transition-all hover:-translate-y-0.5 flex justify-center items-center ${getModalConfirmStyle()}`}
                            >
                                {actionLoading ? <Loader2 size={16} className="animate-spin"/> : 'Confirm'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
