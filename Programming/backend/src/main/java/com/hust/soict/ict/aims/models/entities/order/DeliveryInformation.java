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
    @Setter(AccessLevel.PACKAGE)
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

    public boolean isHanoiOrHoChiMinh() {
        return province.equalsIgnoreCase("Thành phố Hà Nội") || province.equalsIgnoreCase("Thành phố Hồ Chí Minh");
    }

    DeliveryInformation(
            @NonNull String customerName,
            @NonNull String customerEmail,
            @NonNull String phoneNumber,
            @NonNull String province,
            @NonNull String commune,
            @NonNull String address,
            @NonNull String deliveryMethod,
            Order order
    ) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.commune = commune;
        this.address = address;
        this.deliveryMethod = deliveryMethod;
        this.order = order;
    }
}
