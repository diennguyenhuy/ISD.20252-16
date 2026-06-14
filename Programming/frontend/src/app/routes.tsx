import { createBrowserRouter } from 'react-router';

// Layouts
import CustomerLayout from './components/layout/CustomerLayout';
import ManagerLayout from "./components/layout/ManagerLayout";
import AdminLayout from "./components/layout/AdminLayout";

// Shared
import LoginSignup from "./components/shared/LoginSignup";
import ChangePasswordScreen from "./components/shared/ChangePasswordScreen";
import { CartProvider } from "./context/CartContext";
import ProtectedRoute from './components/ProtectedRoute';

// Customer Components
import HomePage from './components/customer/HomePage';
import CustomerProductDetail from './components/customer/CustomerProductDetail';
import CartScreen from './components/customer/CartScreen';
import DeliveryForm from './components/customer/DeliveryForm';
import Invoice from './components/customer/Invoice';
import QRPayment from './components/customer/QRPayment';
import SuccessOrder from './components/customer/SuccessOrder';
import CustomerOrderDetail from './components/customer/CustomerOrderDetail';

import PayPalPayment from './components/customer/PayPalPayment';
import PayPalCallback from './components/customer/PayPalCallback';
import PayPalCancel from './components/customer/PayPalCancel';

// Manager Components
import ManagerHomePage from "./components/manager/ManagerHomePage";
import ProductCreation from "./components/manager/ProductCreation";
import ProductUpdate from "./components/manager/ProductUpdate";
import ManagerProductDetail from "./components/manager/ManagerProductDetail";
import ManagerOrderDetail from "./components/manager/ManagerOrderDetail";
import ManagerOrderList from "./components/manager/ManagerOrderList";

// Admin Components
import AdminUserList from "./components/admin/AdminUserList";
import AdminUserCreation from "./components/admin/AdminUserCreation";
import AdminUserDetail from "./components/admin/AdminUserDetail";

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

            { path: 'checkout/payment/paypal', Component: PayPalPayment },
            { path: 'checkout/payment/paypal/callback', Component: PayPalCallback },
            { path: 'checkout/payment/paypal/cancel', Component: PayPalCancel },

            { path: 'checkout/success/:id', Component: SuccessOrder },
            { path: 'order/:id', Component: CustomerOrderDetail },
        ],
    },
    {
        element: <ProtectedRoute />,
        children: [
            { path: '/change-password', Component: ChangePasswordScreen },
            {
                path: '/manager',
                Component: ManagerLayout,
                children: [
                    { index: true, Component: ManagerHomePage },
                    { path: 'products/add', Component: ProductCreation },
                    { path: 'products/edit/:id', Component: ProductUpdate },
                    { path: 'products/:id', Component: ManagerProductDetail },
                    { path: 'orders', Component: ManagerOrderList },
                    { path: 'orders/:id', Component: ManagerOrderDetail },
                ],
            },
            {
                path: '/admin',
                Component: AdminLayout,
                children: [
                    { index: true, Component: AdminUserList },
                    { path: 'users/create', Component: AdminUserCreation },
                    { path: 'users/:id', Component: AdminUserDetail },
                ],
            }
        ]
    }
]);