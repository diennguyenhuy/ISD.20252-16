package com.hust.soict.ict.aims.models.dto.request;

/**
 * DTO for Credit Card Payment
 *
 * Coupling:
 * - Stamp Coupling: full object passed across layers
 *
 * Improvement:
 * - Can split into smaller objects:
 *   + CardInfo
 *   + PaymentInfo
 */
public class PayByCreditCardRequest {

    private String orderId;
    private double amount;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}