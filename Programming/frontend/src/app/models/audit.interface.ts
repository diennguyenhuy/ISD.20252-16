export interface ProductLog {
    readonly id: string;
    readonly action: ManagerAction;
    readonly managerId: string;
    readonly productId: string;
    readonly timestamp: string;
    readonly logMessage: string;
}

export type ManagerAction = "ADD" | "EDIT" | "DELETE" | "DEACTIVATE";

export interface StockAdjustLog {
    readonly id: string;
    readonly oldStock: number;
    readonly newStock: number;
    readonly reason: string;
    readonly managerId: string;
    readonly productId: string;
    readonly timestamp: string;
    readonly logMessage: string;
}

export interface AdminLog {
    readonly id: string;
    readonly action: ManagerAction;
    readonly adminId: string;
    readonly affectedUserId: string;
    readonly timestamp: string;
    readonly logMessage: string;
}

export type AdminAction = "CREATE" | "ACTIVATE" | "DEACTIVATE" | "BLOCK" | "UNBLOCK" | "ASSIGN_ROLE" | "RESET_PASSWORD" | "UPDATE_EMAIL";