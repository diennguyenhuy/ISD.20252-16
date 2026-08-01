import { apiClient } from "./client";
import type { ProductLog } from "../models/audit.interface"

const AuditLogService = {
    getAuditLogs: async (): Promise<ProductLog[]> => {
        const result =  await apiClient.get<ProductLog[]>("manager/logs");
        return result.data;
    }
}

export default AuditLogService;