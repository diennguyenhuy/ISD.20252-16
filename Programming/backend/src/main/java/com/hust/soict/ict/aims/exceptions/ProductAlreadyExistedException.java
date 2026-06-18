package com.hust.soict.ict.aims.exceptions;

public class ProductAlreadyExistedException extends RuntimeException {
    public ProductAlreadyExistedException(String message) {
        super(message);
    }
}
