package com.hust.soict.ict.aims.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotEmptyValidator.class)
public @interface NullOrNotEmpty {
    String message() default "{NullOrNotNot.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
