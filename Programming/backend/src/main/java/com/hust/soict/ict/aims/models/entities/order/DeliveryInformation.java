package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.exceptions.DeliveryValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryInformation {
    @Id
    @Column(name = "order_id", updatable = false)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    private String customerName;
    private String customerEmail;
    @Column(length = 10)
    private String phoneNumber;
    @Column(length = 50)
    private String province;
    @Column(length = 50)
    private String commune;
    @Column(columnDefinition = "TEXT")
    private String address;
    @Column(length = 50)
    private String deliveryMethod;

    public static DeliveryInformation of(
            Order order,
            String customerName,
            String customerEmail,
            String phoneNumber,
            String province,
            String commune,
            String address,
            String deliveryMethod
    ) throws DeliveryValidationException {
        Objects.requireNonNull(order, "order cannot be null");
        DeliveryInformationValidator.validateCustomerName(customerName);
        DeliveryInformationValidator.validateCustomerEmail(customerEmail);
        DeliveryInformationValidator.validatePhoneNumber(phoneNumber);
        DeliveryInformationValidator.validateProvinceAndCommune(province, commune);
        DeliveryInformationValidator.validateAddress(address);
        DeliveryInformationValidator.validateDeliveryMethod(deliveryMethod);

        DeliveryInformation di = new DeliveryInformation();

        di.order = order;
        di.customerName = customerName;
        di.customerEmail = customerEmail;
        di.phoneNumber = phoneNumber;
        di.province = province;
        di.commune = commune;
        di.address = address;
        di.deliveryMethod = deliveryMethod;

        return di;
    }
}

final class DeliveryInformationValidator {
    private DeliveryInformationValidator() {}

    static void validateCustomerName(String customerName) throws DeliveryValidationException {
        if (customerName == null || customerName.isBlank()) {
            throw new DeliveryValidationException("Customer Name is required", "customerName");
        }
    }

    static void validateCustomerEmail(String customerEmail) throws DeliveryValidationException {
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new DeliveryValidationException("Customer Email is required", "customerEmail");
        }
    }

    static void validatePhoneNumber(String phoneNumber) throws DeliveryValidationException {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new DeliveryValidationException("Phone Number is required", "phoneNumber");
        }

        if (!phoneNumber.matches("0\\d{9}")) {
            throw new DeliveryValidationException("Phone Number is not valid", "phoneNumber");
        }
    }

    static void validateProvinceAndCommune(String province, String commune) throws DeliveryValidationException {
        if (province == null || province.isBlank()) {
            throw new DeliveryValidationException("Province is required", "province");
        }

        if (commune == null || commune.isBlank()) {
            throw new DeliveryValidationException("Commune is required", "commune");
        }
    }

    static void validateAddress(String address) throws DeliveryValidationException {
        if (address == null || address.isBlank()) {
            throw new DeliveryValidationException("Address is required", "address");
        }
    }

    static void validateDeliveryMethod(String deliveryMethod) throws DeliveryValidationException {
        if (deliveryMethod == null || deliveryMethod.isBlank()) {
            throw new DeliveryValidationException("Delivery Method is required", "deliveryMethod");
        }
    }
}
