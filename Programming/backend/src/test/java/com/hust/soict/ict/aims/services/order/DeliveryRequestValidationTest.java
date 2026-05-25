package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.constraints.LocationProvider;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryRequestValidationTest {

    @Mock
    private LocationProvider locationProvider;

    @InjectMocks
    private DeliveryValidator validator;

    private DeliveryRequest deliveryRequest;

    @BeforeEach
    void setUp() {
        deliveryRequest = new DeliveryRequest();
        deliveryRequest.setCustomerName("Lê Hoàng Kiên");
        deliveryRequest.setCustomerEmail("kienle@aims.com");
        deliveryRequest.setPhoneNumber("0123456789");
        deliveryRequest.setProvince("Hà Nội");
        deliveryRequest.setCommune("Phường Hoàng Mai");
        deliveryRequest.setAddress("Số 1 Đại Cồ Việt");
        deliveryRequest.setDeliveryMethod("Standard");
    }

    @Test
    @DisplayName("Test valid complete request")
    void shouldPassValidationForValidRequest() {
        when(locationProvider.isValid("Hà Nội", "Phường Hoàng Mai")).thenReturn(true);

        assertDoesNotThrow(() -> validator.validate(deliveryRequest));
    }

    @Nested
    @DisplayName("Customer name test")
    class CustomerNameTest {
        @ParameterizedTest
        @ValueSource(strings = {"Lê Hoàng Kiên", "Pham Duc Phuoc", "Dima-Nguyen Jr."})
        void shouldPassValidFormats(String customerName) {
            assertDoesNotThrow(() -> validator.validateName(customerName),
                    customerName + " should pass validation");
        }

        @ParameterizedTest
        @ValueSource(strings = {"L3^ H0@`ng K!3^n", "   "})
        void shouldFailInvalidFormats(String customerName) {
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateName(customerName),
                    customerName + " should fail validation");
        }
    }

    @Nested
    @DisplayName("Customer phone number test")
    class CustomerPhoneNumberTest {
        @ParameterizedTest
        @ValueSource(strings = {
                "0123456789",
                "+84123456789"
        })
        void validatePhoneNumber_ValidFormats_ShouldPass(String phone) {
            assertDoesNotThrow(() -> validator.validatePhoneNumber(phone),
                    "Phone number should be valid");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "1123456789",    // Wrong prefix
                "012345678",     // Too short (9 chars total)
                "01234567890",   // Too long (11 chars total)
                "+8412345678",   // Too short for +84
                "012345678a"     // Contains letter
        })
        void validatePhoneNumber_InvalidFormats_ShouldFail(String phone) {
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validatePhoneNumber(phone),
                    "Phone number should fail validation");
        }
    }

    @Nested
    @DisplayName("Customer address test")
    class CustomerAddressTest {
        @ParameterizedTest
        @ValueSource(strings = {
                "Số 1, Đại Cồ Việt/Hai Bà Trưng - HN",
                "Hẻm 12"
        })
        void validateAddress_ValidFormats_ShouldPass(String address) {
            assertDoesNotThrow(() -> validator.validateAddress(address),
                    "Address should be valid");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "Số 1 @ Đại Cồ Việt",
                "Nhà số 1 # 2"
        })
        void validateAddress_InvalidFormats_ShouldFail(String address) {
            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateAddress(address),
                    "Address should fail validation");
        }
    }

    @Nested
    @DisplayName("Province & Commune Location test")
    class LocationTest {
        @Test
        void validateLocation_ValidCommune_ShouldPass() {
            when(locationProvider.isValid("Hà Nội", "Phường Bách Khoa")).thenReturn(true);

            assertDoesNotThrow(() -> validator.validateLocation("Hà Nội", "Phường Bách Khoa"));
        }

        @Test
        void validateLocation_InvalidCommune_ShouldFail() {
            when(locationProvider.isValid("Hà Nội", "Phường Bến Nghé")).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> validator.validateLocation("Hà Nội", "Phường Bến Nghé"));
        }
    }
}