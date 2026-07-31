package com.hust.soict.ict.aims.dto.request;

import com.hust.soict.ict.aims.constraints.ValidProvinceCommune;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@ValidProvinceCommune
public record DeliveryRequest(
        @NotBlank
        @Pattern(regexp = "^[\\p{L}\\s'.-]+$", message = "Must only contain characters")
        @Size(max = 255, message = "Maximum 255 characters")
        String customerName,
        @NotBlank
        @Email(message = "Must be of valid email format")
        String customerEmail,
        @NotBlank
        @Pattern(regexp = "^(0|\\+84)[0-9]{9}$", message = "Must have 9 digits starting with 0 or +84")
        String phoneNumber,
        @NotBlank(message = "Province cannot be blank")
        String province,
        @NotBlank(message = "Commune cannot be blank")
        String commune,
        @NotBlank(message = "Address cannot be blank")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s/.,-]+$", message = "Must have alphanumeric character")
        String address,
        @NotBlank(message = "Delivery method cannot be blank")
        String deliveryMethod
) {}
