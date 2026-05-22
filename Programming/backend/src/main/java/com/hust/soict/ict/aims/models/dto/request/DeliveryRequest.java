package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeliveryRequest {
    @NotBlank
    @Pattern(regexp = "^[\\p{L}\\s'.-]+$", message = "Must only contain characters")
    @Size(max = 255, message = "Maximum 255 characters")
    private String customerName;
    @NotBlank
    @Email(message = "Must be of valid email format")
    private String customerEmail;
    @NotBlank
    @Pattern(regexp = "^(0|\\+84)[0-9]{9}$", message = "Must have 9 digits starting with 0 or +84")
    private String phoneNumber;
    @NotBlank(message = "Province cannot be blank")
    private String province;
    @NotBlank(message = "Commune cannot be blank")
    private String commune;
    @NotBlank(message = "Address cannot be blank")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s/.,-]+$", message = "Must have alphanumeric character")
    private String address;
    @NotBlank(message = "Delivery method cannot be blank")
    private String deliveryMethod;
}
