package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;

public enum ProductAction {
    CREATE,
    UPDATE,
    DELETE,
    DEACTIVATE,
    ACTIVATE,
    STOCK_ADJUST;

    public ProductLog log(User manager, Product product) {
        if (this == STOCK_ADJUST || this == UPDATE) {
            throw new UnsupportedOperationException("Cannot instantiate new " + ProductLog.class.getSimpleName() + " for action " + this +
                    ". Use the other entity constructor (" + STOCK_ADJUST + " -> " + StockAdjustLog.class.getSimpleName() +
                    ", " + UPDATE + " -> " + ProductUpdateLog.class.getSimpleName() + ")");
        }
        return new ProductLog(manager, product, this);
    }
}
