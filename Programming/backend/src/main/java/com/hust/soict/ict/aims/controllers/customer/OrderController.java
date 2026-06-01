package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.*;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.models.dto.response.order.DeliveryResponse;
import com.hust.soict.ict.aims.models.dto.response.order.InvoiceResponse;
import com.hust.soict.ict.aims.models.dto.response.order.OrderDraftResponse;
import com.hust.soict.ict.aims.models.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.services.order.DeliveryService;
import com.hust.soict.ict.aims.services.order.PlaceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Provide API endpoints for customer delivery screen and invoice screen<br>
 * Cohesion: Procedural Cohesion<br>
 * - Reason:
 * Coordinates sequential order placement workflow through REST endpoints.
 * Methods participate in the same order processing procedure:
 * placing order, submitting delivery info, retrieving invoice,
 * retrieving order details, and canceling order. (not concerning payment)<br>
 * Coupling: Data coupling with PlaceOrderService and DeliveryService
 * through method parameters and return values.<br>
 * - Reason: Method sends data structures that the 2 service classes use wholly or
 * sends only necessary data to the service classes.
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final PlaceOrderService placeOrderService;
    private final DeliveryService deliveryService;

    /**
     * POST /order - endpoint for request to place order. Return 201 if success
     * @return a response of draft order created.
     * @throws NotEnoughStockException if cart contains some items with insufficient stock
     * @throws EmptyCartException if cart is empty or null
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDraftResponse placeOrder() throws NotEnoughStockException, EmptyCartException, ProductNotFoundException {
        return placeOrderService.placeOrder();
    }

    /**
     * POST /order/delivery endpoint for submitting or updating delivery information. Return status 202
     * @param deliveryRequest the request body from the user interface. If not valid, return status 400
     * @return a response of the validated delivery information (same as request)
     */
    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public DeliveryResponse submitDeliveryInformation(
            @Valid @RequestBody DeliveryRequest deliveryRequest
    ) {
        return deliveryService.submitDeliveryInformation(deliveryRequest);
    }

    /**
     * GET /order/invoice endpoint for getting invoice info. Return status 202
     * @return response of invoice
     */
    @GetMapping("/invoice")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceResponse getInvoice() {
        return placeOrderService.getInvoice();
    }

    /**
     * DELETE /order endpoint for cancelling order mid-placing. Return status 204
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder() {
        placeOrderService.cancelOrder();
    }

    /**
     * GET /order/{orderId} endpoint for getting order information
     * @param orderId the order id to be requested
     * @return a response of the order
     */
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrder(@PathVariable UUID orderId) {
        return placeOrderService.getOrder(orderId);
    }

    /**
     * DELETE /order/{orderId} endpoint for cancelling order. Return status 204 if success
     * @param orderId the requested order id of order to be canceled
     * @throws OrderNotFoundException if the order is somehow not found in database
     * @throws IllegalStateException if the order status cannot be set to canceled
     */
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable UUID orderId) throws OrderNotFoundException, IllegalStateException {
        placeOrderService.cancelOrder(orderId);
    }
}
