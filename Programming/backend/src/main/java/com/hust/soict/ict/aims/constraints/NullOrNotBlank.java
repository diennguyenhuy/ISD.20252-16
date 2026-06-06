package com.hust.soict.ict.aims.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "{NullOrNotBlank.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
