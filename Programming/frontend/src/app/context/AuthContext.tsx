import React, { createContext, useState, useContext, useEffect, type ReactNode } from 'react';
import type { User } from "../models/user.interface";

interface AuthContextType {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    login: (token: string, user: User) => void;
    logout: () => void;
    hasRole: (role: string) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    // SỬA ĐOẠN NÀY: Đọc trực tiếp từ localStorage ngay khi khởi tạo
    const [token, setToken] = useState<string | null>(() => localStorage.getItem('token'));
    const [user, setUser] = useState<User | null>(() => {
        const storedUser = localStorage.getItem('user');
        return storedUser ? JSON.parse(storedUser) : null;
    });

    // ĐÃ XÓA useEffect (không cần thiết nữa vì đã khởi tạo đồng bộ ở trên)

    const login = (newToken: string, newUser: User) => {
        setToken(newToken);
        setUser(newUser);
        localStorage.setItem('token', newToken);
        localStorage.setItem('user', JSON.stringify(newUser));
    };

    const logout = () => {
        setToken(null);
        setUser(null);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    };

    const hasRole = (role: string): boolean => {
        if (!user) return false;
        return (user.roles as string[]).includes(`ROLE_${role}`) || (user.roles as string[]).includes(role);
    };

    return (
        <AuthContext.Provider value={{ user, token, isAuthenticated: !!token, login, logout, hasRole }}>
            {children}
        </AuthContext.Provider>
    );
};

// Custom hook để sử dụng AuthContext dễ dàng hơn
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};