package com.hust.soict.ict.aims.controllers.order;

import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.models.dto.response.order.DeliveryResponse;
import com.hust.soict.ict.aims.models.dto.response.order.InvoiceResponse;
import com.hust.soict.ict.aims.models.dto.response.order.OrderDraftResponse;
import com.hust.soict.ict.aims.services.order.PlaceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Provide API endpoints for customer delivery screen and invoice screen
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final PlaceOrderService placeOrderService;

    /**
     * POST /order - endpoint for request to place order. Return 201 if success
     * @return a response of draft order created.
     * @throws NotEnoughStockException if cart contains some items with insufficient stock
     * @throws EmptyCartException if cart is empty or null
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody OrderDraftResponse placeOrder() throws NotEnoughStockException, EmptyCartException {
        return placeOrderService.placeOrder();
    }

    /**
     * POST /order/delivery endpoint for submitting or updating delivery information. Status 200 if OK
     * @param deliveryRequest the request body from the user interface. If not valid, return status 400
     * @return a response of the validated delivery information (same as request)
     */
    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody DeliveryResponse submitDeliveryInformation(
            @Valid @RequestBody DeliveryRequest deliveryRequest
    ) {
        return placeOrderService.submitDeliveryInformation(deliveryRequest);
    }

    /**
     * GET /order/invoice endpoint for getting invoice info, which also consists of the entire
     * draft order so far. Return status 201
     * @return response of draft order
     */
    @GetMapping("/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody InvoiceResponse getInvoice() {
        return placeOrderService.getInvoice();
    }

}
