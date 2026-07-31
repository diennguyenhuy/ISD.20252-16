package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.InvoiceResponse;
import com.hust.soict.ict.aims.models.entities.order.Invoice;
import org.springframework.stereotype.Component;

@Component
class InvoiceMapper extends AbstractMapper<Invoice, InvoiceResponse> {

    InvoiceMapper() {
        super(Invoice.class, InvoiceResponse.class, InvoiceResponse::new);
    }

    @Override
    protected void map(Invoice source, InvoiceResponse target) {
        target.setIssuedAt(source.getIssuedAt());
        target.setTotalPriceWithoutVAT(source.getTotalPriceWithoutVAT());
        target.setTotalPriceWithVAT(source.getTotalPriceWithVAT());
        target.setDeliveryFee(source.getDeliveryFee());
        target.setTotalAmount(source.getTotalAmount());
    }
}
