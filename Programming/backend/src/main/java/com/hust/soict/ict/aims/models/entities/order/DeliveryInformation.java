package com.hust.soict.ict.aims.models.entities.order;

import com.hust.soict.ict.aims.exceptions.DeliveryConstructionException;
import com.hust.soict.ict.aims.exceptions.DeliveryValidationException;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
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

    @Setter @NonNull
    private String customerName;
    @Setter @NonNull
    private String customerEmail;
    @Setter @NonNull
    @Column(length = 10)
    private String phoneNumber;
    @Setter @NonNull
    @Column(length = 50)
    private String province;
    @Setter @NonNull
    @Column(length = 50)
    private String commune;
    @Setter @NonNull
    @Column(columnDefinition = "TEXT")
    private String address;
    @Setter @NonNull
    @Column(length = 50)
    private String deliveryMethod;

    public static DeliveryInformation of(
            String customerName,
            String customerEmail,
            String phoneNumber,
            String province,
            String commune,
            String address,
            String deliveryMethod,
            @NonNull Order order
    ) throws DeliveryConstructionException {
        Map<String, String> invalidFields = new HashMap<>();

        try { requireNonBlank(customerName, "customerName"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }
        try { requireNonBlank(customerEmail, "customerEmail"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }
        try { requireNonBlank(phoneNumber, "phoneNumber"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }
        try { requireNonBlank(province, "province"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }
        try { requireNonBlank(commune, "commune"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }
        try { requireNonBlank(address, "address"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }
        try { requireNonBlank(deliveryMethod, "deliveryMethod"); }
        catch (DeliveryValidationException e) { invalidFields.put(e.getInvalidFieldName(), e.getMessage()); }

        if (!invalidFields.isEmpty()) throw new DeliveryConstructionException(invalidFields);

        DeliveryInformation di = new DeliveryInformation();

        order.setDeliveryInformation(di);
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

    private static void requireNonBlank(String value, String field) throws DeliveryValidationException {
        if (value == null || value.isBlank()) {
            throw new DeliveryValidationException(field + " cannot be blank", field);
        }
    }
}
