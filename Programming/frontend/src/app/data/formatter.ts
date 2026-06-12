export type DeliveryMethod = 'standard' | 'rush';

// ─── Utility Functions ────────────────────────────────────────────────────────

/** Format a number with comma thousands separator and fixed decimal places.
 *  e.g. formatNumber(1450800, 2) → "1,450,800.00"
 */
export const formatNumber = (value: number, decimals: number = 2): string =>
  value.toLocaleString('en-US', { minimumFractionDigits: decimals, maximumFractionDigits: decimals });

/** Vietnamese currency: whole number with comma separator followed by ₫.
 *  e.g. formatVND(1000000) → "1,000,000₫"
 */
export const formatVND = (amount: number): string =>
  Math.round(amount).toLocaleString('en-US') + '₫';

/** Format a date-only string (YYYY-MM-DD) or ISO datetime as DD/MM/YYYY. */
export const formatDate = (dateStr: string): string => {
  if (!dateStr) return '';
  const datePart = dateStr.split('T')[0];
  const parts = datePart!.split('-');
  if (parts.length === 3) return `${parts[2]}/${parts[1]}/${parts[0]}`;
  return dateStr;
};

/** Format an ISO datetime string with relative time for recent dates (≤ 7 days).
 *  Otherwise shows "HH:mm:SS DD/MM/YYYY".
 */
export const formatDateTime = (dateStr: string): string => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  if (isNaN(d.getTime())) return dateStr;

  const now = new Date();
  const diffMs = now.getTime() - d.getTime();
  const diffSeconds = Math.floor(diffMs / 1000);
  const diffMinutes = Math.floor(diffSeconds / 60);
  const diffHours = Math.floor(diffMinutes / 60);
  const diffDays = Math.floor(diffHours / 24);

  // Relative time for timestamps within the past 7 days
  if (diffMs >= 0 && diffDays < 7) {
    if (diffSeconds < 60) return 'Just now';
    if (diffMinutes < 60) return `${diffMinutes} minutes ago`;
    if (diffHours < 24) return `${diffHours} hours ago`;
    if (diffDays === 1) return '1 day ago';
    return `${diffDays} days ago`;
  }

  const pad = (n: number) => String(n).padStart(2, '0');
  const time = `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
  const date = `${pad(d.getDate())}/${pad(d.getMonth() + 1)}/${d.getFullYear()}`;
  return `${time} ${date}`;
};

/** Format DVD/CD runtime in minutes as HHhMMm (e.g. 143 → "2h23m"). */
export const formatDurationMinutes = (minutes: number): string => {
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  if (h === 0) return `${m}m`;
  if (m === 0) return `${h}h`;
  return `${h}h${String(m).padStart(2, '0')}m`;
};

/** Format duration in seconds as MMmSSs (e.g. 231 → "3m51s"). */
export const formatDurationSeconds = (seconds: number): string => {
  const m = Math.floor(seconds / 60);
  const s = seconds % 60;
  if (m === 0) return `${s}s`;
  if (s === 0) return `${m}m`;
  return `${m}m${String(s).padStart(2, '0')}s`;
};
