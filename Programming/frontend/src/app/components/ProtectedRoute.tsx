import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute() {
    const { isAuthenticated } = useAuth();
    const location = useLocation();

    console.log("Security Check:", isAuthenticated ? "AUTHORIZED" : "DENIED - REDIRECTING");

    if (!isAuthenticated) {
        // Pass the attempted URL in the `state` prop.
        // Your Login screen can read this and redirect them back here after a successful login!
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return <Outlet />;
}