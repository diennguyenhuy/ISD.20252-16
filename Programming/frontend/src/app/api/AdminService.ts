import { apiClient } from './client';

export interface UserResponse {
    id: string;
    username: string;
    email: string;
    roles: string[];
    active: boolean;
    blocked: boolean;
    mustChangePassword: boolean;
    createdAt: string;
}

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
    roles: string[];
}

export interface ChangePasswordPayload {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
}

export const AdminService = {
    getUsers: (page = 0, size = 100) =>
        apiClient.get<PagedResponse<UserResponse>>(`/api/admin/users?page=${page}&size=${size}`),

    getUser: (id: string) =>
        apiClient.get<UserResponse>(`/api/admin/users/${id}`),

    createUser: (data: CreateUserPayload) =>
        apiClient.post<UserResponse>('/api/admin/users', data),

    deactivateUser: (id: string) =>
        apiClient.patch(`/api/admin/users/${id}/deactivate`),

    activateUser: (id: string) =>
        apiClient.patch(`/api/admin/users/${id}/activate`),

    blockUser: (id: string) =>
        apiClient.patch(`/api/admin/users/${id}/block`),

    unblockUser: (id: string) =>
        apiClient.patch(`/api/admin/users/${id}/unblock`),

    assignRoles: (id: string, data: AssignRolesPayload) =>
        apiClient.put<UserResponse>(`/api/admin/users/${id}/roles`, data),

    resetPassword: (id: string) =>
        apiClient.post(`/api/admin/users/${id}/reset-password`),
};

export const ProfileService = {
    changePassword: (data: ChangePasswordPayload) =>
        apiClient.post('/api/profile/password', data),
};
