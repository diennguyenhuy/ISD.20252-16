package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(ProductNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(OrderNotFoundException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(OrderNotPlacedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(OrderNotPlacedException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(MethodArgumentTypeMismatchException e) {
        if (e.getRequiredType() == UUID.class) {
            return Map.of("message", "Invalid UUID format for parameter " + e.getName(),
                    "value", e.getValue() != null ? e.getValue() : "",
                    "requiredType", UUID.class,
                    "reason", e.getCause() != null ? e.getCause().getMessage() : ""
                    );
        }
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handle(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(NoSuchElementException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(EmptyCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(EmptyCartException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(NotEnoughStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handle(NotEnoughStockException e) {
        return Map.of(
                "message", e.getMessage(),
                "details", e.getInsufficientQuantity()
        );
    }

    @ExceptionHandler(DeliveryConstructionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(DeliveryConstructionException e) {
        return Map.of(
                "message", e.getMessage(),
                "details", e.getInvalidFields()
        );
    }

    @ExceptionHandler(ProductConstructionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(ProductConstructionException e) {
        return Map.of(
                "message", e.getMessage(),
                "details", e.getInvalidFields()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handle(IllegalStateException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(IllegalArgumentException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handle(Exception e) {
        return Map.of("message", e.getMessage());
    }
}
