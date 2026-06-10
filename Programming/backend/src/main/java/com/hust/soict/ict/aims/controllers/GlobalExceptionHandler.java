package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.exceptions.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(MethodArgumentTypeMismatchException e) {
        return Map.ofEntries(
                Map.entry("message", e.getMessage()),
                Map.entry("name", e.getName()),
                Map.entry("value", e.getValue() == null ? "" : e.getValue()),
                Map.entry("requiredType", e.getRequiredType() == null ? "": e.getRequiredType()),
                Map.entry("reason", e.getCause() == null ? "" : e.getCause().getMessage())
        );
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

    @ExceptionHandler(NotEnoughStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handle(NotEnoughStockException e) {
        return Map.ofEntries(
                Map.entry("message", e.getMessage()),
                Map.entry("details", Map.of(
                        Map.entry("insufficient", e.getInsufficientQuantity()),
                        Map.entry("missing", e.getMissingProducts())
                ))
        );
    }

    @ExceptionHandler(OrderApprovalException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handle(OrderApprovalException e) {
        return Map.ofEntries(
                Map.entry("message", e.getMessage()),
                Map.entry("details", Map.of(
                        Map.entry("insufficient", e.getInsufficientQuantity()),
                        Map.entry("missing", e.getMissingProducts())
                ))
        );
    }

    @ExceptionHandler(InvalidDeliveryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handle(InvalidDeliveryException e) {
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

    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handle(OptimisticLockingFailureException e) {
        return Map.of("message", e.getMessage());
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
    public ResponseEntity<Map<String, String>> handle(Exception e) {
        HttpStatus status = getHttpStatus(e);
        return ResponseEntity.status(status).body(Map.of("message", e.getMessage()));
    }

    private HttpStatus getHttpStatus(Exception e) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);

        if (responseStatus != null) {
            return responseStatus.code();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
