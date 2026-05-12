Để chạy project:
1. Mở terminal
2. Gõ và enter
```bash
npm install
```
Lưu ý: Phải có Nodejs trên máy
3. Gõ và enter
```bash
npm run dev
```
4. Vào link `http://localhost:5173/`
5. Khám phá:
- `/login`: `LoginSignup.tsx`
* Customer: `/`
- Mặc định: `HomePage.tsx`
- `product/:id`: `ProductDetail.tsx`
- `cart`: `CartScreen.tsx`
- `checkout/delivery`: `DeliveryForm.tsx`
- `checkout/invoice`: `Invoice.tsx`
- `checkout/payment/qr`: `QRPayment.tsx`
- `checkout/payment/paypal`: `PaypalPayment.tsx`
- `checkout/success`: `SuccessOrder.tsx`
- `order/:id`: `CustomerOrderDetail.tsx`
* Manager: `/manager`
- Mặc định: `ManagerHomePage.tsx`
- `products/add`: `ProductAddition.tsx`
- `products/edit/:id`: `ProductAddition.tsx` - Edit có cùng screen với Add
- `products/:id`: `ManagerProductDetail.tsx`
- `orders`: `ManagerOrderList.tsx`
- `orders/:id`: `ManagerOrderDetail.tsx`
- `profile`: `UserProfile.tsx`
- `change-password`: `ChangePassword.tsx`
* Admin: `/admin`
- Mặc định: `AdminUserList.tsx`
- `users/create`: `AdminUserCreation.tsx`
- `users/:id`: `AdminUserDetail.tsx`
- `profile`: `UserProfile.tsx`
- `change-password`: `ChangePassword.tsx`
