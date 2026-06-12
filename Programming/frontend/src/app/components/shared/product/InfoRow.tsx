export function InfoRow({label, value,}: {
    label: string;
    value: string | number | boolean | undefined;
}) {
    if (value === undefined || value === '') return null;

    const displayValue = typeof value === 'boolean' ? (value ? '✓ Yes' : '✗ No') : value;

    return (
        <div
            className="flex py-3 border-b border-border last:border-0 hover:bg-muted/20 transition-colors px-2 rounded-lg">
            <span className="text-muted-foreground text-sm w-44 shrink-0 font-medium">
                {label}
            </span>
            <span className="text-foreground text-sm font-semibold">{displayValue}</span>
        </div>
    );
}
