export interface User {
    readonly id: string;
    username: string;
    email: string;
    roles: UserRole[];
    active: boolean;
    blocked: boolean;
    mustChangePassword: boolean;
    createdAt: string;
    updatedAt: string;
}

export type UserRole = "PRODUCT_MANAGER" | "ADMINISTRATOR";
