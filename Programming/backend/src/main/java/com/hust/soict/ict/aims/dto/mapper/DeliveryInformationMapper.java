package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.DeliveryResponse;
import com.hust.soict.ict.aims.models.entities.order.DeliveryInformation;
import org.springframework.stereotype.Component;

@Component
class DeliveryInformationMapper extends AbstractMapper<DeliveryInformation, DeliveryResponse> {

    DeliveryInformationMapper() {
        super(DeliveryResponse::new);
    }

    @Override
    public void map(DeliveryInformation source, DeliveryResponse target) {
        target.setCustomerName(source.getCustomerName());
        target.setCustomerEmail(source.getCustomerEmail());
        target.setPhoneNumber(source.getPhoneNumber());
        target.setProvince(source.getProvince());
        target.setCommune(source.getCommune());
        target.setAddress(source.getAddress());
        target.setDeliveryMethod(source.getDeliveryMethod());
    }

    @Override
    public Class<DeliveryInformation> getSourceClass() {
        return DeliveryInformation.class;
    }

    @Override
    public Class<DeliveryResponse> getTargetClass() {
        return DeliveryResponse.class;
    }
}
