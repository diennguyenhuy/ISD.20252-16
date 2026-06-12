interface AuditLogBase {
    readonly id: string;
    readonly timestamp: string;
}

interface ProductAuditLogBase extends AuditLogBase {
    readonly managerId?: string;
    readonly managerUsername: string;
    readonly productId?: string;
    readonly productTitle: string;
}

export type ProductAuditLog = ProductLog | StockAdjustLog;

export interface ProductLog extends ProductAuditLogBase {
    readonly action: ProductAction;
    readonly details?: ProductEditDetail[];
}

export interface ProductEditDetail {
    readonly fieldName: string;
    readonly oldValue: string;
    readonly newValue: string;
}

export type ProductAction = "CREATE" | "UPDATE" | "DELETE" | "DEACTIVATE" | "ACTIVATE";

export interface StockAdjustLog extends ProductAuditLogBase {
    readonly oldStock: number;
    readonly newStock: number;
    readonly reason: string;
}

export interface AdminLog extends AuditLogBase {
    readonly action: UserAction;
    readonly adminId?: string;
    readonly adminUsername: string;
    readonly affectedUserId?: string;
    readonly affectedUsername: string;
}

export type UserAction = "CREATE" | "ACTIVATE" | "DEACTIVATE" | "BLOCK" | "UNBLOCK" | "MODIFY_ROLE" | "RESET_PASSWORD" | "UPDATE_EMAIL";