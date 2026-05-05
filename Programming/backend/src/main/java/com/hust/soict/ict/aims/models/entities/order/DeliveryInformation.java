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
    ) {
        Objects.requireNonNull(order, "order cannot be null");
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
}
