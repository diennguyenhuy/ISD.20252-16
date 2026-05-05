package com.hust.soict.ict.aims.models.cart;

import com.hust.soict.ict.aims.models.entities.order.DeliveryInformation;
import com.hust.soict.ict.aims.models.entities.order.Invoice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
public class OrderDraft {
    private final Cart cart;
    @Setter
    private DeliveryInformation deliveryInformation;
    @Setter
    private long deliveryFee;

    public OrderDraft(Cart cart) {
        this.cart = cart;
    }
}
