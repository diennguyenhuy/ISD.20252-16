package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class DeliveryRequest {
    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;
    @Email(message = "Must be of valid email format")
    private String customerEmail;
    @Pattern(regexp = "0\\d{9}", message = "Must have 10 digits starting with 0")
    private String phoneNumber;
    @NotBlank(message = "Province cannot be blank")
    private String province;
    @NotBlank(message = "Commune cannot be blank")
    private String commune;
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotBlank(message = "Delivery method cannot be blank")
    private String deliveryMethod;
}
