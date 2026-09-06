interface AuditLogBase {
    readonly id: string;
    readonly timestamp: string;
    readonly action: string;
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
    readonly action: "UPDATE";
    readonly details: ProductEditDetail[];
}

export type ProductEditDetail = {
    readonly fieldName: string;
    readonly oldValue: string;
    readonly newValue: string;
} | {
    readonly fieldName: string;
    readonly removedValues: string[];
    readonly addedValues: string[];
};

export type ProductAction = "CREATE" | "UPDATE" | "DELETE" | "DEACTIVATE" | "ACTIVATE" | "STOCK_ADJUST";

export interface StockAdjustLog extends ProductLogBase {
    readonly action: "STOCK_ADJUST";
    readonly oldStock: number;
    readonly newStock: number;
    readonly reason: string;
}

export interface AdminLog extends AuditLogBase {
    readonly action: UserAction;
    readonly adminId: string;
    readonly adminUsername: string;
    readonly adminEmail: string;
    readonly affectedUserId: string;
    readonly affectedUsername: string;
    readonly affectedUserEmail: string;
}

export type UserAction = "CREATE" | "ACTIVATE" | "DEACTIVATE" | "BLOCK" | "UNBLOCK" | "MODIFY_ROLE" | "RESET_PASSWORD" | "UPDATE_EMAIL";

export interface OrderLog extends AuditLogBase {
    readonly action: OrderAction;
    readonly orderId: string;
    readonly managerId?: string;
    readonly managerUsername?: string;
}

export type OrderAction = "PLACE" | "APPROVE" | "REJECT" | "CANCEL" | "REFUND" | "CANCEL_REFUND" | "REJECT_REFUND";
