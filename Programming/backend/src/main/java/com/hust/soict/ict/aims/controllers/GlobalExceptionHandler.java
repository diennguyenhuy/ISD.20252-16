package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public @ResponseBody String productNotFound(ProductNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(OrderNotPlacedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String orderNotPlaced(OrderNotPlacedException e) { return e.getMessage(); }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<?, ?> methodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
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
    public @ResponseBody Map<String, List<String>> methodArgumentNotValid(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody String notFound(NoSuchElementException e) {
        return e.getMessage();
    }

    @ExceptionHandler(EmptyCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String emptyCart(EmptyCartException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotEnoughStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody Map<String, Object> notEnoughStock(NotEnoughStockException e) {
        return Map.of(
                "message", e.getMessage(),
                "details", e.getInsufficientQuantity()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody String exception(Exception e) {
        return e.getMessage();
    }
}
