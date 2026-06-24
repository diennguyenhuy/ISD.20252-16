package com.hust.soict.ict.aims.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
class NullOrNotEmptyCharSequenceValidator implements ConstraintValidator<NullOrNotEmpty, CharSequence> {
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || !value.isEmpty();
    }
}
