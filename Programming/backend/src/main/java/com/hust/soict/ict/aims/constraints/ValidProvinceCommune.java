package com.hust.soict.ict.aims.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProvinceCommuneValidator.class)
@Documented
public @interface ValidProvinceCommune {
    String message() default "Province-Commune pair is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
