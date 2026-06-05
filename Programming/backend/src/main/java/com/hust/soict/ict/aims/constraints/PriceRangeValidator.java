package com.hust.soict.ict.aims.constraints;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, CreateProductRequest> {
    @Override
    public boolean isValid(CreateProductRequest value, ConstraintValidatorContext context) {
        if (value.getCurrentPrice() == null || value.getOriginalValue() == null) {
            return true;
        }

        long minPrice = value.getCurrentPrice() * Product.MIN_PRICE_RELATIVE_PERCENTAGE / 100;
        long maxPrice = value.getCurrentPrice() * Product.MAX_PRICE_RELATIVE_PERCENTAGE / 100;

        return value.getCurrentPrice() >= minPrice && value.getCurrentPrice() <= maxPrice;
    }
}
