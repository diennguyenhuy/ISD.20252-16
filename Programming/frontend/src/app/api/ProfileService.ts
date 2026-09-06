import { apiClient } from "./client";

export interface ChangePasswordPayload {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
}

const ProfileService = {
    changePassword: async (data: ChangePasswordPayload) => {
        await apiClient.post<void>('/profile/password', data);
    },
};

export default ProfileService;