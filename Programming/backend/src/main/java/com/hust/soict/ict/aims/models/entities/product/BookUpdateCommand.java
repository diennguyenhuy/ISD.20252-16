package com.hust.soict.ict.aims.models.entities.product;

import java.util.List;

public interface BookUpdateCommand<T> extends PrintableProductUpdateCommand<T> {
    record Authors(List<String> newValue) implements BookUpdateCommand<List<String>> {}
    record NumberOfPages(Integer newValue) implements BookUpdateCommand<Integer> {}
    record Genre(String newValue) implements BookUpdateCommand<String> {}
}
