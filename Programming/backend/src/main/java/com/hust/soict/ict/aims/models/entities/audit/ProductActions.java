package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;

import java.util.*;

public final class ProductActions {
    public static final Create CREATE = new Create();
    public static final Update UPDATE = new Update();
    public static final Delete DELETE = new Delete();
    public static final Deactivate DEACTIVATE = new Deactivate();
    public static final Activate ACTIVATE = new Activate();
    public static final StockAdjust STOCK_ADJUST = new StockAdjust();

    private static abstract class Action {
        protected final ProductAction action;
        public final ProductAction action() {
            return action;
        }
        protected Action(final ProductAction action) {
            this.action = action;
        }
    }

    private interface DefaultLogging {
        ProductAction action();
        default ProductLog log(User manager, Product product) {
            return new ProductLog(manager, product, action());
        }
    }

    public static final class Create extends Action implements DefaultLogging {
        private Create() {
            super(ProductAction.CREATE);
        }
    }

    public static final class Update extends Action {
        private Update() {
            super(ProductAction.UPDATE);
        }
        public ProductUpdateLog log(
                User manager,
                Product product,
                Set<String> requestedFieldNames,
                Map<String, ?> oldValueMap,
                Map<String, ?> newValueMap
        ) {
            List<ProductEditDetail> details = new ArrayList<>();

            requestedFieldNames.forEach(f -> {
                Object oldValue = oldValueMap.get(f);
                Object newValue = newValueMap.get(f);

                if (!Objects.equals(oldValue, newValue)) {
                    details.add(new ProductEditDetail(
                            f,
                            Objects.toString(oldValue),
                            Objects.toString(newValue)
                    ));
                }
            });
            return new ProductUpdateLog(manager, product, details);
        }
    }

    public static final class Delete extends Action implements DefaultLogging {
        private Delete() {
            super(ProductAction.DELETE);
        }
    }

    public static final class Deactivate extends Action implements DefaultLogging {
        private Deactivate() {
            super(ProductAction.DEACTIVATE);
        }
    }

    public static final class Activate extends Action implements DefaultLogging {
        private Activate() {
            super(ProductAction.ACTIVATE);
        }
    }

    public static final class StockAdjust extends Action {
        private StockAdjust() {
            super(ProductAction.STOCK_ADJUST);
        }
        public StockAdjustLog log(User manager, Product product, int oldStock, int newStock, String reason) {
            return new StockAdjustLog(manager, product, oldStock, newStock, reason);
        }
    }

    private ProductActions() {}
}
