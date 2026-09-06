import type { AdminLog, UserAction } from "../../models";
import { useState, useEffect } from 'react';
import { Activity, Search, Loader2, UserPlus, UserCheck, UserX, Shield, ShieldOff, Key, Mail, RefreshCcw } from 'lucide-react';
import { formatDateTime } from '../../data/formatter'; // Adjust path
import { AdminAuditLogService } from '../../api/AuditLogService'; // Adjust path

// Semantic configuration for different administrative actions
const ACTION_CONFIG: Record<UserAction, { color: string, icon: any, verb: string }> = {
    CREATE: { color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: UserPlus, verb: 'created a new account for' },
    ACTIVATE: { color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: UserCheck, verb: 'activated the account of' },
    DEACTIVATE: { color: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20', icon: UserX, verb: 'deactivated the account of' },
    BLOCK: { color: 'bg-destructive/10 text-destructive border-destructive/20', icon: ShieldOff, verb: 'blocked user' },
    UNBLOCK: { color: 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border-emerald-500/20', icon: Shield, verb: 'unblocked user' },
    MODIFY_ROLE: { color: 'bg-blue-500/10 text-blue-600 dark:text-blue-400 border-blue-500/20', icon: Key, verb: 'modified system roles for' },
    RESET_PASSWORD: { color: 'bg-amber-500/10 text-amber-600 dark:text-amber-400 border-amber-500/20', icon: RefreshCcw, verb: 'triggered a password reset for' },
    UPDATE_EMAIL: { color: 'bg-blue-500/10 text-blue-600 dark:text-blue-400 border-blue-500/20', icon: Mail, verb: 'updated the email address of' }
};

export default function AdminUserLog() {
    const [logs, setLogs] = useState<AdminLog[]>([]);
    const [loading, setLoading] = useState(true);
    const [search, setSearch] = useState('');

    useEffect(() => {
        fetchLogs();
    }, []);

    const fetchLogs = async () => {
        try {
            // Replace with your actual AdminService call
            const data = await AdminAuditLogService.getAdminLogs();
            setLogs(data);
        } catch (err) {
            console.error("Failed to load admin logs:", err);
        } finally {
            setLoading(false);
        }
    };

    const filteredLogs = logs.filter(log => {
        const q = search.toLowerCase();
        return !q ||
            log.adminUsername.toLowerCase().includes(q) ||
            log.affectedUsername.toLowerCase().includes(q) ||
            log.affectedUserEmail.toLowerCase().includes(q) ||
            log.action.toLowerCase().includes(q);
    });

    if (loading) {
        return (
            <div className="py-32 flex flex-col items-center justify-center animate-in fade-in">
                <Loader2 className="animate-spin text-destructive mb-4" size={40} />
                <p className="text-muted-foreground font-medium">Loading security audit history...</p>
            </div>
        );
    }

    return (
        <div className="animate-in fade-in duration-500 pb-10 max-w-5xl mx-auto">

            {/* Header - Themed Red (Destructive) for Admin Context */}
            <div className="mb-8">
                <h2 className="text-2xl font-extrabold tracking-tight text-foreground flex items-center gap-2">
                    <Activity className="text-destructive" /> Security Audit Logs
                </h2>
                <p className="text-muted-foreground text-sm font-medium mt-1">
                    Immutable ledger of all sensitive administrative actions performed across the system.
                </p>
            </div>

            {/* Search Filter */}
            <div className="mb-8">
                <div className="relative max-w-md">
                    <Search size={18} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-muted-foreground" />
                    <input
                        type="text"
                        placeholder="Search by admin, user, or action..."
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                        className="w-full border border-border rounded-xl pl-10 pr-4 py-3 text-sm font-medium outline-none focus:border-destructive focus:ring-1 focus:ring-destructive/50 bg-input-background text-foreground transition-all shadow-sm"
                    />
                </div>
            </div>

            {/* Timeline Layout */}
            <div className="space-y-6 relative before:absolute before:inset-0 before:ml-5 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-border before:to-transparent">

                {filteredLogs.length === 0 ? (
                    <div className="text-center py-20 relative z-10 bg-background rounded-3xl border border-dashed border-border/50">
                        <p className="text-muted-foreground font-medium">No administrative logs match your search.</p>
                    </div>
                ) : filteredLogs.map((log) => {
                    const cfg = ACTION_CONFIG[log.action] || ACTION_CONFIG.UPDATE_EMAIL;
                    const ActionIcon = cfg.icon;

                    return (
                        <div key={log.id} className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active">

                            {/* Timeline Dot */}
                            <div className={`flex items-center justify-center w-10 h-10 rounded-full border-4 border-background shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 shadow-sm z-10 ${cfg.color.split(' ')[0]} ${cfg.color.split(' ')[1]}`}>
                                <ActionIcon size={16} />
                            </div>

                            {/* Log Card */}
                            <div className="w-[calc(100%-4rem)] md:w-[calc(50%-2.5rem)] bg-card border border-border rounded-2xl p-5 shadow-sm hover:shadow-md transition-shadow">

                                {/* Card Header */}
                                <div className="flex items-center justify-between mb-3 border-b border-border/50 pb-3">
                                    <span className={`text-[10px] uppercase tracking-wider font-bold px-2.5 py-1 rounded-md border ${cfg.color}`}>
                                        {log.action.replace('_', ' ')}
                                    </span>
                                    <span className="text-xs text-muted-foreground font-medium font-mono">
                                        {formatDateTime(log.timestamp)}
                                    </span>
                                </div>

                                {/* Body - Clear, Human Readable Sentence */}
                                <p className="text-sm text-muted-foreground leading-relaxed">
                                    Administrator <span className="font-bold text-foreground">@{log.adminUsername}</span>
                                    <span className="text-xs opacity-70 ml-1">({log.adminEmail})</span>
                                    <br/>
                                    {cfg.verb} <span className="font-bold text-foreground">@{log.affectedUsername}</span>
                                    <span className="text-xs opacity-70 ml-1">({log.affectedUserEmail})</span>.
                                </p>

                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}