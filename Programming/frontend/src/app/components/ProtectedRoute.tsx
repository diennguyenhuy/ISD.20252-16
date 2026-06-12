import React from 'react';
import { Navigate, Outlet } from 'react-router'; // Dùng Outlet cho Nested Routing
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute() {
    const { isAuthenticated } = useAuth();
    console.log("Trạng thái bảo vệ cửa:", isAuthenticated ? "CÓ THẺ - CHO QUA" : "CHƯA ĐĂNG NHẬP - ĐÁ VỀ LOGIN");
    // Nếu chưa đăng nhập, ép chuyển hướng về trang /login
    // Tham số `replace` giúp user không thể ấn nút "Back" trên trình duyệt để quay lại đây
    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    // Nếu đã đăng nhập, cho phép render các component con bên trong (Các trang Manager)
    return <Outlet />;
}