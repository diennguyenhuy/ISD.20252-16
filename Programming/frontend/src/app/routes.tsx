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
import LoginSignup from "./components/shared/LoginSignup";
import ManagerLayout from "./components/layout/ManagerLayout";
import ManagerHomePage from "./components/manager/ManagerHomePage";
import ProductAddition from "./components/manager/ProductAddition";
import ManagerProductDetail from "./components/manager/ManagerProductDetail";
import ManagerOrderDetail from "./components/manager/ManagerOrderDetail";
import ManagerOrderList from "./components/manager/ManagerOrderList";

export const router = createBrowserRouter([
    { path: '/login', Component: LoginSignup },
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
        path: '/manager',
        Component: ManagerLayout,
        children: [
            { index: true, Component: ManagerHomePage },
            { path: 'products/add', Component: ProductAddition },
            { path: 'products/edit/:id', Component: ProductAddition },
            { path: 'products/:id', Component: ManagerProductDetail },
            { path: 'orders', Component: ManagerOrderList },
            { path: 'orders/:id', Component: ManagerOrderDetail },
            // { path: 'profile', Component: UserProfile },
            // { path: 'change-password', Component: ChangePassword },
        ],

    },
    {
        //TODO: ADMIN ROUTES
    }
]);
