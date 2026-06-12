import { apiClient } from "./client";
import type { ProductAuditLog } from "../models/audit.interface"

const AuditLogService = {
    getAuditLogs: async (): Promise<ProductAuditLog[]> => {
        const result =  await apiClient.get<ProductAuditLog[]>("manager/logs");
        return result.data;
    }
}

export default AuditLogService;