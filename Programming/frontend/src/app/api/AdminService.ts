import { apiClient } from './client';
import type { UserRole, User} from "../models/user.interface";

export interface PagedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
}

export interface CreateUserPayload {
    username: string;
    email: string;
    roles: string[];
}

export interface AssignRolesPayload {
    roles: UserRole[];
}

export interface ChangePasswordPayload {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
}

export const AdminService = {
    getUsers: (page = 0, size = 100) =>
        apiClient.get<PagedResponse<User>>(`/admin/users?page=${page}&size=${size}`),

    getUser: (id: string) =>
        apiClient.get<User>(`/admin/users/${id}`),

    createUser: (data: CreateUserPayload) =>
        apiClient.post<User>('/admin/users', data),

    deactivateUser: (id: string) =>
        apiClient.patch(`/admin/users/${id}/deactivate`),

    activateUser: (id: string) =>
        apiClient.patch(`/admin/users/${id}/activate`),

    blockUser: (id: string) =>
        apiClient.patch(`/admin/users/${id}/block`),

    unblockUser: (id: string) =>
        apiClient.patch(`/admin/users/${id}/unblock`),

    assignRoles: (id: string, data: AssignRolesPayload) =>
        apiClient.put<User>(`/admin/users/${id}/roles`, data),

    resetPassword: (id: string) =>
        apiClient.post(`/admin/users/${id}/reset-password`),

    updateEmail: (id: string, newEmail: string) =>
        apiClient.put(`/admin/users/${id}/email-update`),
};

export const ProfileService = {
    changePassword: (data: ChangePasswordPayload) =>
        apiClient.post('/profile/password', data),
};
