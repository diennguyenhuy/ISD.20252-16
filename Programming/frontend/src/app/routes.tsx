import { createBrowserRouter } from 'react-router';

// Layouts
import CustomerLayout from './components/layout/CustomerLayout';

import HomePage from './components/customer/HomePage';
import CustomerProductDetail from './components/customer/CustomerProductDetail';
import CartScreen from './components/customer/CartScreen';
import DeliveryForm from './components/customer/DeliveryForm';
import Invoice from './components/customer/Invoice';
import QRPayment from './components/customer/QRPayment';
// import PaypalPayment from './components/customer/PaypalPayment';
import SuccessOrder from './components/customer/SuccessOrder';
import CustomerOrderDetail from './components/customer/CustomerOrderDetail';
import { CartProvider } from "./context/CartContext";

export const router = createBrowserRouter([
    {
        path: '/',
        element: (
            <CartProvider>
                <CustomerLayout />
            </CartProvider>
        ),
        children: [
            { index: true, Component: HomePage },
            { path: 'product/:id', Component: CustomerProductDetail },
            { path: 'cart', Component: CartScreen },
            { path: 'checkout/delivery', Component: DeliveryForm },
            { path: 'checkout/invoice', Component: Invoice },
            { path: 'checkout/payment/qr', Component: QRPayment },
            // { path: 'checkout/payment/paypal', Component: PaypalPayment },
            { path: 'checkout/success/:id', Component: SuccessOrder },
            { path: 'order/:id', Component: CustomerOrderDetail },
        ],
    },
    {
        //TODO: PRODUCT MANAGER ROUTES
    },
    {
        //TODO: ADMIN ROUTES
    }
]);
