package com.hust.soict.ict.aims.models.entities.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
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
}
