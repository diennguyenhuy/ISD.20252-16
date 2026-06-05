package com.hust.soict.ict.aims.constraints;

import com.hust.soict.ict.aims.dto.request.DeliveryRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProvinceCommuneValidator implements ConstraintValidator<ValidProvinceCommune, DeliveryRequest> {
    private final LocationProvider locationProvider;

    @Override
    public boolean isValid(DeliveryRequest value, ConstraintValidatorContext context) {
        if (value.getProvince() == null || value.getCommune() == null) {
            return true;
        }

        if (!locationProvider.isValidProvince(value.getProvince())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Province does not exist").addPropertyNode("province").addConstraintViolation();
            return false;
        }

        boolean valid = locationProvider.isValid(value.getProvince(), value.getCommune());
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Commune does not exist in " + value.getProvince()).addPropertyNode("commune").addConstraintViolation();
        }

        return valid;
    }
}
