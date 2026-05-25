package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.mapper.OrderMapper;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.models.dto.response.order.DeliveryResponse;
import com.hust.soict.ict.aims.models.entities.order.DeliveryInformation;
import com.hust.soict.ict.aims.models.entities.order.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Cohesion: Communicational Cohesion<br>
 * Reason:
 * Methods operate on the same delivery-related data,
 * including DeliveryInformation and draft Order state.<br>
 * Coupling:<br>
 * - Stamp coupling with OrderDraftContext, DeliveryRequest,
 *   and OrderMapper because composite objects are shared.
 * - Data coupling with DeliveryFeeCalculator through
 *   primitive/simple parameters.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    private final OrderDraftContext orderDraftContext;
    private final OrderMapper orderMapper;
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public DeliveryResponse submitDeliveryInformation(DeliveryRequest deliveryRequest) {
        log.debug("Submitting delivery request...");
        Order draftOrder = orderDraftContext.getDraftOrder();
        var deliveryInformation = draftOrder.getDeliveryInformation();
        if (deliveryInformation != null) {
            log.debug("Updating delivery information...");
            updateDeliveryInformation(deliveryInformation, deliveryRequest);
            log.debug("Delivery information successfully updated!");
            return orderMapper.toDeliveryResponse(deliveryInformation);
        }

        var di = DeliveryInformation.of(
                deliveryRequest.getCustomerName(),
                deliveryRequest.getCustomerEmail(),
                deliveryRequest.getPhoneNumber(),
                deliveryRequest.getProvince(),
                deliveryRequest.getCommune(),
                deliveryRequest.getAddress(),
                deliveryRequest.getDeliveryMethod(),
                draftOrder
        );

        draftOrder.setDeliveryFee(deliveryFeeCalculator.calculateDeliveryFee(
                draftOrder.getTotalWeight(),
                draftOrder.getDeliveryInformation().getProvince(),
                draftOrder.getTotalPriceWithoutVAT()
        ));

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Delivery information successfully saved!");
        return orderMapper.toDeliveryResponse(di);
    }

    private void updateDeliveryInformation(DeliveryInformation deliveryInformation, DeliveryRequest deliveryRequest) {
        String oldProvince = deliveryInformation.getProvince();
        String newProvince = deliveryRequest.getProvince();

        deliveryInformation.setCustomerName(deliveryRequest.getCustomerName());
        deliveryInformation.setCustomerEmail(deliveryRequest.getCustomerEmail());
        deliveryInformation.setPhoneNumber(deliveryRequest.getPhoneNumber());
        deliveryInformation.setProvince(deliveryRequest.getProvince());
        deliveryInformation.setCommune(deliveryRequest.getCommune());
        deliveryInformation.setAddress(deliveryRequest.getAddress());
        deliveryInformation.setDeliveryMethod(deliveryRequest.getDeliveryMethod());

        if (!oldProvince.equalsIgnoreCase(newProvince)) {
            Order draftOrder = orderDraftContext.getDraftOrder();
            draftOrder.setDeliveryFee(deliveryFeeCalculator.calculateDeliveryFee(
                    draftOrder.getTotalWeight(),
                    draftOrder.getDeliveryInformation().getProvince(),
                    draftOrder.getTotalPriceWithoutVAT()
            ));
        }
    }
}
