package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ProductActions {
    public static final DefaultActionLogging CREATE = () -> ProductAction.CREATE;
    public static final Supplier<UpdateLogBuilder> UPDATE = UpdateLogBuilder::new;
    public static final DefaultActionLogging DELETE = () -> ProductAction.DELETE;
    public static final DefaultActionLogging DEACTIVATE = () -> ProductAction.DEACTIVATE;
    public static final DefaultActionLogging ACTIVATE = () -> ProductAction.ACTIVATE;
    public static final StockAdjustLogging STOCK_ADJUST = new StockAdjustLogging();

    @FunctionalInterface
    private interface Action {
        ProductAction action();
    }

    public interface DefaultActionLogging extends Action {
        default ProductLog log(User manager, Product product) {
            return new ProductLog(manager, product, action());
        }
    }

    public static final class UpdateLogBuilder implements Action {
        private final Set<String> requestedFieldNames = new HashSet<>();
        private final Map<String, Object> oldValueMap = new HashMap<>();
        private final Map<String, ProductEditDetail> editDetails = new HashMap<>();
        private final Map<String, ProductEditArrayDetail> editArrayDetails = new HashMap<>();

        private UpdateLogBuilder() {}

        @Override
        public ProductAction action() {
            return ProductAction.UPDATE;
        }

        public void requestedFieldName(String fieldName) {
            requestedFieldNames.add(fieldName);
        }

        public void extractOldValues(Product product) {
            for (Class<?> clazz = product.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                        continue;
                    }

                    try {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        if (!requestedFieldNames.contains(fieldName)) {
                            continue;
                        }
                        Object value = field.get(product);
                        oldValueMap.put(fieldName, value);
                    } catch (IllegalAccessException e) {
                        System.out.println("[UNREACHABLE] Error accessing " + field.getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        public void extractNewValues(Product product) {
            for (Class<?> clazz = product.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                        continue;
                    }

                    try {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        if (!requestedFieldNames.contains(fieldName)) {
                            continue;
                        }
                        Object newValue = field.get(product);
                        Object oldValue = oldValueMap.get(fieldName);

                        if (oldValue instanceof Collection<?> oldCollection && newValue instanceof Collection<?> newCollection) {
                            Set<Object> removedValue = new HashSet<>(oldCollection);
                            Set<Object> addedValue = new HashSet<>(newCollection);
                            removedValue.removeAll(newCollection);
                            addedValue.removeAll(oldCollection);
                            editArrayDetails.put(fieldName, new ProductEditArrayDetail(
                                    removedValue.stream().map(Objects::toString).toList(),
                                    addedValue.stream().map(Objects::toString).toList()
                            ));
                        } else if (!Objects.equals(oldValue, newValue)) {
                            editDetails.put(fieldName, new ProductEditDetail(
                                    Objects.toString(oldValue),
                                    Objects.toString(newValue)
                            ));
                        }
                    } catch (IllegalAccessException e) {
                        System.out.println("[UNREACHABLE] Error accessing " + field.getName() + ": " + e.getMessage());
                    }
                }
            }
        }
        public ProductLog build(User manager, Product product) {
            return new ProductLog(manager, product, editDetails, editArrayDetails);
        }
    }

    public static final class StockAdjustLogging implements Action {
        private StockAdjustLogging() {}

        @Override
        public ProductAction action() {
            return ProductAction.STOCK_ADJUST;
        }

        public StockAdjustLog log(User manager, Product product, int oldStock, int newStock, String reason) {
            return new StockAdjustLog(manager, product, oldStock, newStock, reason);
        }
    }

    private ProductActions() {}
}
