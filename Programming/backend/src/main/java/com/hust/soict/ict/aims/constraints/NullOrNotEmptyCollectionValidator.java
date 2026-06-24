package com.hust.soict.ict.aims.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class NullOrNotEmptyCollectionValidator implements ConstraintValidator<NullOrNotEmpty, Collection<?>> {
    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        return value == null || !value.isEmpty();
    }
}
