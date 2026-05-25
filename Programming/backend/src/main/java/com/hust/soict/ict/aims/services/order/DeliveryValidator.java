package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.constraints.LocationProvider;
import org.springframework.stereotype.Component;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class DeliveryValidator {

    private LocationProvider locationProvider;

    // Email regex matching standard format checks
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    /**
     * Aggregates all delivery validations. 
     * Throws IllegalArgumentException if any business constraint is violated.
     */
    public void validate(DeliveryRequest request) throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("Delivery request cannot be null.");
        }

        validateName(request.getCustomerName());
        validateEmail(request.getCustomerEmail());
        validatePhoneNumber(request.getPhoneNumber());
        validateDeliveryMethod(request.getDeliveryMethod());
        validateAddress(request.getAddress());
        validateLocation(request.getProvince(), request.getCommune());
    }

    public void validateName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be blank.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Customer name cannot exceed 255 characters.");
        }
        // Allows any Unicode letter (Vietnamese letters), spaces, and ' . -
        if (!name.matches("^[\\p{L}\\s'.-]+$")) {
            throw new IllegalArgumentException("Customer name contains invalid characters.");
        }
    }

    public void validateEmail(String email) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be blank.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Must be of valid email format.");
        }
    }

    public void validatePhoneNumber(String phone) throws IllegalArgumentException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be blank.");
        }
        // Matches exactly your requested pattern: starts with 0 or +84, followed by 9 digits
        if (!phone.matches("^(0|\\+84)[0-9]{9}$")) {
            throw new IllegalArgumentException("Phone number must have 9 digits starting with 0 or +84.");
        }
    }

    public void validateAddress(String address) throws IllegalArgumentException {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be blank.");
        }
        if (address.length() > 255) {
            throw new IllegalArgumentException("Address cannot exceed 255 characters.");
        }
        // Allows alphanumeric, spaces, and standard address markers / . , -
        if (!address.matches("^[\\p{L}\\p{N}\\s/.,-]+$")) {
            throw new IllegalArgumentException("Address contains invalid characters.");
        }
    }

    public void validateDeliveryMethod(String deliveryMethod) throws IllegalArgumentException {
        if (deliveryMethod == null || deliveryMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery method cannot be blank.");
        }
    }

    public void validateLocation(String province, String commune) throws IllegalArgumentException {
        if (province == null || province.trim().isEmpty()) {
            throw new IllegalArgumentException("Province cannot be blank.");
        }
        if (commune == null || commune.trim().isEmpty()) {
            throw new IllegalArgumentException("Commune cannot be blank.");
        }

        if (!locationProvider.isValid(province, commune)) {
            throw new IllegalArgumentException("The selected commune '" + commune + "' does not belong to province '" + province + "'.");
        }
    }
}