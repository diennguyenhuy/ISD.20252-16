export interface User {
    readonly id: string;
    username: string;
    email: string;
    hashedPassword: string;
    createdAt: string;
    status: UserStatus;
    roles: UserRole[];
}

export type UserStatus = 'ACTIVE' | 'DEACTIVATED' | 'BLOCKED';

export type UserRole = "PRODUCT_MANAGER" | "ADMINISTRATOR";
