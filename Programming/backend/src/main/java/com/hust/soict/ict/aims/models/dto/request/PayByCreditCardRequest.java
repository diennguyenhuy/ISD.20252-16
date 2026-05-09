package com.hust.soict.ict.aims.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PayByCreditCardRequest {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String cardHolderName;
    @NotBlank
    private String expirationDate;
    @NotBlank
    private String cvv;
}
