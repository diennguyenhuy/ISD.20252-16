package com.hust.soict.ict.aims.exceptions;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Cart is empty");
    }
}
