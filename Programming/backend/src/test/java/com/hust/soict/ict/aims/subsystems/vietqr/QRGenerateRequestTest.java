package com.hust.soict.ict.aims.subsystems.vietqr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QRGenerateRequestTest {

    private QRGenerateRequest request;
    private final ObjectMapper mapper = new ObjectMapper();

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

        assertEquals("ORDER12345678", result);
        assertEquals(13, result.length());
    }

    @Test
    void buildRequestString_shouldReturnValidJsonWithCorrectFields() throws Exception {
        String json = request.buildRequestString();

        JsonNode node = mapper.readTree(json);

        assertEquals("970436", node.get("bankCode").asText());
        assertEquals("123456789", node.get("bankAccount").asText());
        assertEquals("NGUYEN VAN A", node.get("userBankName").asText());
        assertEquals("PAYMENT", node.get("content").asText());
        assertEquals(0, node.get("qrType").asInt());
        assertEquals(100000, node.get("amount").asLong());
        assertEquals("ORD001", node.get("orderId").asText());
        assertEquals("C", node.get("transType").asText());
    }
}