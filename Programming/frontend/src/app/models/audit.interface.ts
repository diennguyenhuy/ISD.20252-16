interface AuditLogBase {
    readonly id: string;
    readonly timestamp: string;
}

interface ProductLogBase extends AuditLogBase {
    readonly managerId: string;
    readonly managerUsername: string;
    readonly productId: string;
    readonly productTitle: string;
    readonly action: ProductAction;
}

export type ProductLog = ProductLogBase | ProductUpdateLog | StockAdjustLog;

export interface ProductUpdateLog extends ProductLogBase {
    readonly details: ProductEditDetail[];
}

export interface ProductEditDetail {
    readonly fieldName: string;
    readonly oldValue: string;
    readonly newValue: string;
}

export type ProductAction = "CREATE" | "UPDATE" | "DELETE" | "DEACTIVATE" | "ACTIVATE" | "STOCK_ADJUST";

export interface StockAdjustLog extends ProductLogBase {
    readonly oldStock: number;
    readonly newStock: number;
    readonly reason: string;
}

export interface AdminLog extends AuditLogBase {
    readonly action: UserAction;
    readonly adminId: string;
    readonly adminUsername: string;
    readonly affectedUserId: string;
    readonly affectedUsername: string;
}

export type UserAction = "CREATE" | "ACTIVATE" | "DEACTIVATE" | "BLOCK" | "UNBLOCK" | "MODIFY_ROLE" | "RESET_PASSWORD" | "UPDATE_EMAIL";