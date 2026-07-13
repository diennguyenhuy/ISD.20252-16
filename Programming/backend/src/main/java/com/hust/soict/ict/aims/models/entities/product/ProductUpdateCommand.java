package com.hust.soict.ict.aims.models.entities.product;

import java.math.BigDecimal;

public interface ProductUpdateCommand<T> {
    T newValue();

    record Title(String newValue) implements ProductUpdateCommand<String> {}
    record Category(String newValue) implements ProductUpdateCommand<String> {}
    record Description(String newValue) implements ProductUpdateCommand<String> {}
    record Height(BigDecimal newValue) implements ProductUpdateCommand<BigDecimal> {}
    record Width(BigDecimal newValue) implements ProductUpdateCommand<BigDecimal> {}
    record Length(BigDecimal newValue) implements ProductUpdateCommand<BigDecimal> {}
    record Weight(BigDecimal newValue) implements ProductUpdateCommand<BigDecimal> {}
    record CurrentPrice(Long newValue) implements ProductUpdateCommand<Long> {}
    record ImageUrl(String newValue) implements ProductUpdateCommand<String> {}
}
