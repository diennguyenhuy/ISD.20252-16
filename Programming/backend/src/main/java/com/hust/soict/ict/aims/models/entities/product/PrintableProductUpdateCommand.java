package com.hust.soict.ict.aims.models.entities.product;

public interface PrintableProductUpdateCommand<T> extends ProductUpdateCommand<T> {
    record Publisher(String newValue) implements ProductUpdateCommand<String> {}
    record Language(String newValue) implements ProductUpdateCommand<String> {}
}
