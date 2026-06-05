package com.hust.soict.ict.aims.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PriceRangeValidator.class)
public @interface ValidPriceRange {
    String message() default "{ValidPriceRange.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
