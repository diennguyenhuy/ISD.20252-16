import { ArrowLeft, Truck, ChevronDown, AlertCircle, Loader2 } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router';

import { useCart } from '../../context/CartContext';
import OrderService from '../../api/orderService';
import { formatVND } from '../../data/mockData';
import { provinces, provinceToWards } from '../../data/provinceData';
import type { DeliveryInformation } from '../../models/order.interface';

type FieldError = {
    [K in keyof DeliveryInformation]?: DeliveryInformation[K] | undefined;
}

export default function DeliveryForm() {
    const navigate = useNavigate();

    // Bring in the global cart to satisfy the requirement: "Customers will still see products"
    const { cart } = useCart();

    const [form, setForm] = useState<DeliveryInformation>({
        customerName: '',
        customerEmail: '',
        phoneNumber: '',
        province: '',
        commune: '',
        address: '',
        deliveryMethod: 'standard',
    });

    const [errors, setErrors] = useState<FieldError>({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isCanceling, setIsCanceling] = useState(false);

    const validate = (): boolean => {
        const errs: FieldError = {};
        if (!form.customerName.trim()) errs.customerName = 'Enter your full name';
        else if (form.customerName.trim().length < 2) errs.customerName = 'Full name must be at least 2 characters';

        if (!form.customerEmail.trim()) errs.customerEmail = 'Please enter your email';
        else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.customerEmail.trim())) errs.customerEmail = 'Invalid email address';

        if (!form.phoneNumber.trim()) errs.phoneNumber = 'Please enter your phone number';
        else if (!/^(0|\+84)[0-9]{9}$/.test(form.phoneNumber.trim())) errs.phoneNumber = 'Invalid phone number (e.g., 0901234567)';

        if (!form.province) errs.province = 'Please select a province/city';
        if (!form.commune) errs.commune = 'Please select a ward/commune';

        if (!form.address.trim()) errs.address = 'Please enter a detailed address';
        else if (form.address.trim().length < 5) errs.address = 'Address is too short';

        setErrors(errs);
        return Object.keys(errs).length === 0;
    };

    const handleSubmit = async () => {
        if (!validate()) return;
        setIsSubmitting(true);

        try {
            // Send the strict data payload to the backend
            await OrderService.submitDeliveryForm(form);
            navigate('/checkout/invoice', {
                state: {
                    deliveryInformation: form,
                }
            });
        } catch (error) {
            console.error("Failed to submit delivery info", error);
            alert("Something went wrong saving your delivery details. Please try again.");
            setIsSubmitting(false);
        }
    };

    const handleCancel = async () => {
        setIsCanceling(true);
        try {
            // Cancel the current active order session in the backend
            await OrderService.cancelOrderPlacement();
            navigate('/cart');
        } catch (error) {
            console.error("Failed to cancel order placement", error);
            // Even if API fails, we should probably still let the user go back to cart
            navigate('/cart');
        }
    };

    // Safe updater for standard text fields
    const updateText = (field: keyof DeliveryInformation, value: string) => {
        setForm(prev => ({ ...prev, [field]: value }));
        if (errors[field as keyof FieldError]) {
            setErrors(prev => ({ ...prev, [field]: undefined }));
        }
    };

    // Dependent Dropdown Logic
    const handleProvinceChange = (newProvince: string) => {
        setForm(prev => ({
            ...prev,
            province: newProvince,
            commune: ''
        })
        );
        if (errors.province) setErrors(prev => ({ ...prev, province: undefined }));
    };

    const inputBaseClass = "w-full border rounded-xl px-4 py-3.5 text-sm outline-none transition-all duration-200 text-foreground";
    const inputNormalClass = "border-border bg-input-background focus:border-primary focus:ring-1 focus:ring-primary/50 placeholder:text-muted-foreground/70";
    const inputErrorClass = "border-destructive/50 bg-destructive/5 focus:border-destructive focus:ring-1 focus:ring-destructive/50";

    // Prevent rendering if cart vanished (e.g. user refreshed the page directly on /checkout/delivery)
    if (!cart) {
        return (
            <div className="max-w-2xl mx-auto px-4 py-20 text-center">
                <Loader2 className="w-10 h-10 animate-spin text-primary mx-auto mb-4" />
                <p>Loading order data...</p>
            </div>
        );
    }

    return (
        <div className="max-w-6xl mx-auto px-4 py-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
            {/* Progress */}
            <div className="flex items-center gap-2 mb-8 text-sm font-medium">
                <span className="text-muted-foreground">Shopping Cart</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-primary px-2 py-1 bg-primary/10 rounded-md">Delivery Information</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-muted-foreground">Invoice</span>
                <span className="text-muted-foreground/50">›</span>
                <span className="text-muted-foreground">Pay Order</span>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Left Column: Delivery Form (Takes up 2/3 space) */}
                <div className="lg:col-span-2 bg-card rounded-3xl border border-border shadow-lg p-6 sm:p-8">
                    <div className="flex items-center gap-4 mb-8">
                        <div className="bg-primary/10 p-3 rounded-2xl shadow-inner">
                            <Truck size={24} className="text-primary" />
                        </div>
                        <div>
                            <h1 className="text-2xl text-foreground font-bold">Delivery Information</h1>
                            <p className="text-sm text-muted-foreground mt-1">Please fill in all the required fields for
                                delivery</p>
                        </div>
                    </div>

                    <div className="space-y-5">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                            {/* Customer Name */}
                            <div>
                                <label className="block text-sm text-foreground font-medium mb-1.5">
                                    Full Name <span className="text-destructive">*</span>
                                </label>
                                <input
                                    type="text"
                                    placeholder="Nguyễn Văn A"
                                    value={form.customerName}
                                    onChange={e => updateText('customerName', e.target.value)}
                                    className={`${inputBaseClass} ${errors.customerName ? inputErrorClass : inputNormalClass}`}
                                />
                                {errors.customerName && (
                                    <p className="mt-1.5 text-xs text-destructive font-medium flex items-center gap-1.5">
                                        <AlertCircle size={14} /> {errors.customerName}
                                    </p>
                                )}
                            </div>

                            {/* Phone */}
                            <div>
                                <label className="block text-sm text-foreground font-medium mb-1.5">
                                    Phone Number <span className="text-destructive">*</span>
                                </label>
                                <input
                                    type="tel"
                                    placeholder="0901234567"
                                    value={form.phoneNumber}
                                    onChange={e => updateText('phoneNumber', e.target.value)}
                                    className={`${inputBaseClass} ${errors.phoneNumber ? inputErrorClass : inputNormalClass}`}
                                />
                                {errors.phoneNumber && (
                                    <p className="mt-1.5 text-xs text-destructive font-medium flex items-center gap-1.5">
                                        <AlertCircle size={14} /> {errors.phoneNumber}
                                    </p>
                                )}
                            </div>
                        </div>

                        {/* Email */}
                        <div>
                            <label className="block text-sm text-foreground font-medium mb-1.5">
                                Email Address <span className="text-destructive">*</span>
                            </label>
                            <input
                                type="email"
                                placeholder="example@gmail.com"
                                value={form.customerEmail}
                                onChange={e => updateText('customerEmail', e.target.value)}
                                className={`${inputBaseClass} ${errors.customerEmail ? inputErrorClass : inputNormalClass}`}
                            />
                            {errors.customerEmail && (
                                <p className="mt-1.5 text-xs text-destructive font-medium flex items-center gap-1.5">
                                    <AlertCircle size={14} /> {errors.customerEmail}
                                </p>
                            )}
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                            {/* Dependent Dropdown 1: Province */}
                            <div>
                                <label className="block text-sm text-foreground font-medium mb-1.5">
                                    Province/City <span className="text-destructive">*</span>
                                </label>
                                <div className="relative">
                                    <select
                                        value={form.province}
                                        onChange={e => handleProvinceChange(e.target.value)}
                                        className={`${inputBaseClass} appearance-none cursor-pointer ${errors.province ? inputErrorClass : inputNormalClass}`}
                                    >
                                        <option value="" disabled className="text-muted-foreground">-- Select Province
                                            --
                                        </option>
                                        {provinces.map(p => <option key={p} value={p}
                                            className="bg-card text-foreground">{p}</option>)}
                                    </select>
                                    <ChevronDown size={16}
                                        className="absolute right-4 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none" />
                                </div>
                                {errors.province && (
                                    <p className="mt-1.5 text-xs text-destructive font-medium flex items-center gap-1.5">
                                        <AlertCircle size={14} /> {errors.province}
                                    </p>
                                )}
                            </div>

                            {/* Dependent Dropdown 2: Commune/Ward */}
                            <div>
                                <label className="block text-sm text-foreground font-medium mb-1.5">
                                    Commune/Ward <span className="text-destructive">*</span>
                                </label>
                                <div className="relative">
                                    <select
                                        value={form.commune}
                                        onChange={e => updateText('commune', e.target.value)}
                                        disabled={!form.province}
                                        className={`${inputBaseClass} appearance-none ${!form.province ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'} ${errors.commune ? inputErrorClass : inputNormalClass}`}
                                    >
                                        <option value="" disabled>-- Select Ward --</option>
                                        {form.province && provinceToWards[form.province]?.map((ward: string) => (
                                            <option key={ward} value={ward}
                                                className="bg-card text-foreground">{ward}</option>
                                        ))}
                                    </select>
                                    <ChevronDown size={16}
                                        className="absolute right-4 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none" />
                                </div>
                                {errors.commune && (
                                    <p className="mt-1.5 text-xs text-destructive font-medium flex items-center gap-1.5">
                                        <AlertCircle size={14} /> {errors.commune}
                                    </p>
                                )}
                            </div>
                        </div>

                        {/* Address */}
                        <div>
                            <label className="block text-sm text-foreground font-medium mb-1.5">
                                Detailed Address <span className="text-destructive">*</span>
                            </label>
                            <input
                                type="text"
                                placeholder="House number, street name, etc."
                                value={form.address}
                                onChange={e => updateText('address', e.target.value)}
                                className={`${inputBaseClass} ${errors.address ? inputErrorClass : inputNormalClass}`}
                            />
                            {errors.address && (
                                <p className="mt-1.5 text-xs text-destructive font-medium flex items-center gap-1.5">
                                    <AlertCircle size={14} /> {errors.address}
                                </p>
                            )}
                        </div>

                        {/* Delivery Method */}
                        <div className="pt-2">
                            <label className="block text-sm text-foreground font-medium mb-3">
                                Delivery Method <span className="text-destructive">*</span>
                            </label>
                            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                                <button
                                    type="button"
                                    onClick={() => updateText('deliveryMethod', 'standard')}
                                    className={`flex items-center gap-4 p-4 rounded-2xl border-2 transition-all text-left ${form.deliveryMethod === 'standard'
                                            ? 'border-primary bg-primary/5 shadow-sm shadow-primary/10'
                                            : 'border-border bg-card hover:border-primary/40 hover:bg-muted/30'
                                        }`}
                                >
                                    <div
                                        className={`p-2 rounded-full ${form.deliveryMethod === 'standard' ? 'bg-primary/10 text-primary' : 'bg-muted text-muted-foreground'}`}>
                                        <Truck size={20} />
                                    </div>
                                    <div>
                                        <p className={`text-sm font-bold ${form.deliveryMethod === 'standard' ? 'text-primary' : 'text-foreground'}`}>Standard
                                            Delivery</p>
                                        <p className="text-xs text-muted-foreground mt-0.5">3-5 days</p>
                                    </div>
                                </button>

                                <button
                                    type="button"
                                    onClick={() => updateText('deliveryMethod', 'rush')}
                                    className={`flex items-center gap-4 p-4 rounded-2xl border-2 transition-all text-left ${form.deliveryMethod === 'rush'
                                            ? 'border-primary bg-primary/5 shadow-sm shadow-primary/10'
                                            : 'border-border bg-card hover:border-primary/40 hover:bg-muted/30'
                                        }`}
                                >
                                    <div
                                        className={`p-2 rounded-full ${form.deliveryMethod === 'rush' ? 'bg-primary/10 text-primary' : 'bg-muted text-muted-foreground'}`}>
                                        <Truck size={20} />
                                    </div>
                                    <div>
                                        <p className={`text-sm font-bold ${form.deliveryMethod === 'rush' ? 'text-primary' : 'text-foreground'}`}>Rush
                                            Delivery</p>
                                        <p className="text-xs text-muted-foreground mt-0.5">1-2 days</p>
                                    </div>
                                </button>
                            </div>
                        </div>
                    </div>

                    {/* Actions */}
                    <div className="flex gap-4 mt-8 pt-6 border-t border-border">
                        <button
                            onClick={handleCancel}
                            disabled={isCanceling || isSubmitting}
                            className="flex-1 py-3.5 rounded-xl border border-border bg-card text-foreground font-semibold hover:bg-muted transition-colors flex items-center justify-center gap-2 disabled:opacity-50"
                        >
                            {isCanceling ? <Loader2 size={18} className="animate-spin" /> : <ArrowLeft size={18} />}
                            Cancel Order
                        </button>
                        <button
                            onClick={handleSubmit}
                            disabled={isSubmitting || isCanceling}
                            className="flex-[2] py-3.5 rounded-xl bg-primary text-primary-foreground font-bold hover:bg-accent hover:text-accent-foreground transition-all shadow-lg hover:shadow-primary/30 hover:-translate-y-0.5 flex items-center justify-center gap-2 disabled:opacity-70 disabled:cursor-not-allowed"
                        >
                            {isSubmitting && <Loader2 size={18} className="animate-spin" />}
                            {isSubmitting ? 'Processing...' : 'Continue to Invoice →'}
                        </button>
                    </div>
                </div>

                {/* Right Column: Order/Cart Summary */}
                <div className="lg:col-span-1">
                    <div className="bg-card rounded-3xl border border-border shadow-sm p-6 sticky top-24">
                        <h2 className="text-foreground font-bold text-lg mb-5 pb-4 border-b border-border">Order
                            Summary</h2>

                        {/* Scrollable list so huge carts don't break the layout */}
                        <div className="space-y-4 mb-6 max-h-[400px] overflow-y-auto pr-2 custom-scrollbar">
                            {cart.items.map(item => (
                                <div key={item.product.id} className="flex gap-3">
                                    <div
                                        className="w-16 h-16 bg-muted rounded-lg shrink-0 overflow-hidden border border-border">
                                        <img
                                            src={item.product.imageURL}
                                            alt={item.product.title}
                                            className="w-full h-full object-cover"
                                        />
                                    </div>
                                    <div className="flex-1 min-w-0 flex flex-col justify-center">
                                        <h4 className="text-sm font-semibold text-foreground line-clamp-1">{item.product.title}</h4>
                                        <div className="flex justify-between items-center mt-1">
                                            <span className="text-xs text-muted-foreground">Qty: {item.quantity}</span>
                                            <span
                                                className="text-sm font-bold text-primary">{formatVND(item.itemTotalPrice)}</span>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>

                        <div className="border-t border-border pt-4 space-y-2">
                            <div className="flex justify-between items-center">
                                <span className="text-sm text-muted-foreground">Subtotal</span>
                                <span className="font-semibold text-foreground">{formatVND(cart.totalPrice)}</span>
                            </div>
                            <div className="flex justify-between items-center">
                                <span className="text-sm text-muted-foreground">Shipping Fee</span>
                                <span
                                    className="text-xs font-medium text-amber-500 italic bg-amber-500/10 px-2 py-0.5 rounded">Calculated at Invoice</span>
                            </div>
                            <div
                                className="flex justify-between items-center pt-2 mt-2 border-t border-border border-dashed">
                                <span className="font-bold text-foreground">Provisional Total</span>
                                <span className="text-primary font-bold text-xl">{formatVND(cart.totalPrice)}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}