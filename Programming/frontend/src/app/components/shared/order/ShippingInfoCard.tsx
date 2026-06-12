import { MapPin, Phone, Truck, User } from 'lucide-react';
import type { DeliveryInformation } from '../../../models/order.interface';

interface ShippingInfoCardProps {
    delivery: DeliveryInformation;
}

export function ShippingInfoCard({ delivery }: ShippingInfoCardProps) {
    const isRush = delivery.deliveryMethod === 'rush';

    return (
        <div className="bg-card rounded-3xl border border-border shadow-md overflow-hidden flex flex-col">

            {/* Header */}
            <div className="px-6 py-4 border-b border-border bg-muted/30 flex items-center gap-2">
                <Truck size={18} className="text-primary" />
                <span className="text-base font-bold text-foreground">Shipping Information</span>
            </div>

            {/* Fields */}
            <div className="p-6 grid gap-3 flex-1">

                {/* Recipient */}
                <InfoTile icon={User} label="Recipient" value={delivery.customerName} />

                {/* Phone */}
                <InfoTile icon={Phone} label="Phone Number" value={delivery.phoneNumber} />

                {/* Address */}
                <InfoTile
                    icon={MapPin}
                    label="Address"
                    value={`${delivery.address}, ${delivery.commune}, ${delivery.province}`}
                />

                {/* Delivery method — accent styling to stand out */}
                <div className="flex items-start gap-3 bg-primary/5 p-3.5 rounded-2xl border border-primary/20">
                    <div className="p-2 bg-primary/20 rounded-lg shadow-sm shrink-0">
                        <Truck size={16} className="text-primary" />
                    </div>
                    <div>
                        <p className="text-xs text-muted-foreground font-medium mb-0.5">Delivery Method</p>
                        <p className="text-sm text-primary font-bold">
                            {isRush ? 'Rush Delivery (1–2 days)' : 'Standard Delivery (3–5 days)'}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}


function InfoTile({icon: Icon, label, value}: {
    icon: React.ElementType;
    label: string;
    value: string;
}) {
    return (
        <div className="flex items-start gap-3 bg-muted/30 p-3.5 rounded-2xl border border-border/50">
            <div className="p-2 bg-background rounded-lg shadow-sm shrink-0">
                <Icon size={16} className="text-primary" />
            </div>
            <div>
                <p className="text-xs text-muted-foreground font-medium mb-0.5">{label}</p>
                <p className="text-sm text-foreground font-semibold leading-relaxed">{value}</p>
            </div>
        </div>
    );
}
