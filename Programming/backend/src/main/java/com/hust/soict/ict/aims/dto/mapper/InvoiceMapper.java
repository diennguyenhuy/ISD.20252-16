package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.InvoiceResponse;
import com.hust.soict.ict.aims.models.entities.order.Invoice;
import org.springframework.stereotype.Component;

@Component
class InvoiceMapper extends AbstractMapper<Invoice, InvoiceResponse> {

    InvoiceMapper() {
        super(InvoiceResponse::new);
    }

    @Override
    public void map(Invoice source, InvoiceResponse target) {
        target.setIssuedAt(source.getIssuedAt());
        target.setTotalPriceWithoutVAT(source.getTotalPriceWithoutVAT());
        target.setTotalPriceWithVAT(source.getTotalPriceWithVAT());
        target.setDeliveryFee(source.getDeliveryFee());
        target.setTotalAmount(source.getTotalAmount());
    }

    @Override
    public Class<Invoice> getSourceClass() {
        return Invoice.class;
    }

    @Override
    public Class<InvoiceResponse> getTargetClass() {
        return InvoiceResponse.class;
    }
}
