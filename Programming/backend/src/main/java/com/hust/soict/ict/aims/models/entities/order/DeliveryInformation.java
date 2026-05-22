package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryInformation {
    @Id
    @Column(name = "order_id", updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
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
    ) {
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
