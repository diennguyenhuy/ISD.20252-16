package com.hust.soict.ict.aims.exceptions;

public class OrderNotCompleteException extends RuntimeException {
    public OrderNotCompleteException() {
    }
    public OrderNotCompleteException(String message) {
        super(message);
    }
}
