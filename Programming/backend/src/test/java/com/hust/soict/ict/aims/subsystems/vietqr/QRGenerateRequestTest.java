package com.hust.soict.ict.aims.subsystems.vietqr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRGenerateRequestTest {

    private QRGenerateRequest request;

    @BeforeEach
    void setUp() {
        request = new QRGenerateRequest(
                "970436",
                "123456789",
                "NGUYEN VAN A",
                "PAYMENT",
                100000,
                "ORD001"
        );
    }

    @Test
    void sanitizeContent_shouldRemoveSpecialCharactersAndTruncate() {
        String result = request.sanitizeContent(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ@#$!!!"
        );

        assertEquals("ABCDEFGHIJKLMNOPQRSTUVW", result);
        assertEquals(23, result.length());
    }

    @Test
    void sanitizeOrderId_shouldRemoveSpecialCharactersAndTruncate() {
        String result = request.sanitizeOrderId(
                "ORDER@#123456789012345"
        );

        assertEquals("ORDER1234567", result);
        assertEquals(13, result.length());
    }
}