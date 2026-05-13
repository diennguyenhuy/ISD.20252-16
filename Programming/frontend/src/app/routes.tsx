import { createBrowserRouter } from 'react-router';

// Layouts
import CustomerLayout from './components/layout/CustomerLayout';

import HomePage from './components/customer/HomePage';
import ProductDetail from './components/customer/ProductDetail';
import CartScreen from './components/customer/CartScreen';
import DeliveryForm from './components/customer/DeliveryForm';
import Invoice from './components/customer/Invoice';
import QRPayment from './components/customer/QRPayment';
import PaypalPayment from './components/customer/PaypalPayment';
import SuccessOrder from './components/customer/SuccessOrder';
import CustomerOrderDetail from './components/customer/CustomerOrderDetail';

export const router = createBrowserRouter([
  {
    path: '/',
    Component: CustomerLayout,
    children: [
      { index: true, Component: HomePage },
      { path: 'product/:id', Component: ProductDetail },
      { path: 'cart', Component: CartScreen },
      { path: 'checkout/delivery', Component: DeliveryForm },
      { path: 'checkout/invoice', Component: Invoice },
      { path: 'checkout/payment/qr', Component: QRPayment },
      { path: 'checkout/payment/paypal', Component: PaypalPayment },
      { path: 'checkout/success/:id', Component: SuccessOrder },
      { path: 'order/:id', Component: CustomerOrderDetail },
    ],
  },

]);
