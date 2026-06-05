package com.hust.soict.ict.aims.services.order;

//import com.hust.soict.ict.aims.services.order.deliveryfee.StandardDeliveryFeeStrategy;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.math.BigDecimal;
//import java.util.stream.Stream;
//
//class DeliveryFeeCalculatorTest {
//    private StandardDeliveryFeeStrategy standardDeliveryFeeStrategy;
//
//    @BeforeEach
//    void setUp() {
//        standardDeliveryFeeStrategy = new StandardDeliveryFeeStrategy();
//    }
//
//    @ParameterizedTest(name = "{0}")
//    @MethodSource("testCases")
//    void calculateDeliveryFee(
//            String testName,
//            String province,
//            BigDecimal weight,
//            long totalPrice,
//            long expectedFee
//    ) {
//        long actualFee = standardDeliveryFeeStrategy.calculateDeliveryFee(weight, province, totalPrice);
//        assertEquals(expectedFee, actualFee);
//    }
//
//    private static Stream<Arguments> testCases() {
//        return Stream.of(
//                Arguments.of(
//                        "[UT005] Test Hanoi/HCM baseline",
//                        "Thành phố Hà Nội",
//                        BigDecimal.valueOf(1.3),
//                        79_000L,
//                        22_000L
//                ),
//                Arguments.of(
//                        "[UT006] Test Hanoi/HCM increments + ceiling",
//                        "Thành phố Hồ Chí Minh",
//                        BigDecimal.valueOf(3.2),
//                        95_000L,
//                        24_500L
//                ),
//                Arguments.of(
//                        "[UT007] Test Hanoi/HCM increments + ceiling + subsidy",
//                        "Thành phố Hà Nội",
//                        BigDecimal.valueOf(5.7),
//                        467_000L,
//                        12_000L
//                ),
//                Arguments.of(
//                        "[UT008] Test other provinces baseline",
//                        "Tỉnh Thanh Hóa",
//                        BigDecimal.valueOf(0.2),
//                        49_000L,
//                        30_000L
//                ),
//                Arguments.of(
//                        "[UT009] Test other provinces increments + ceiling",
//                        "Thành phố Hải Phòng",
//                        BigDecimal.valueOf(1.1),
//                        69_000L,
//                        35_000L
//                ),
//                Arguments.of(
//                        "[UT010] Test other provinces increments + ceiling + subsidy",
//                        "Tỉnh Nghệ An",
//                        BigDecimal.valueOf(4.1),
//                        312_000L,
//                        25_000L
//                ),
//                Arguments.of(
//                        "[UT011] Test total price exactly 100.000",
//                        "Thành phố Đà Nẵng",
//                        BigDecimal.valueOf(3.2),
//                        100_000L,
//                        45_000L
//                ),
//                Arguments.of(
//                        "[UT012] Test full free shipping",
//                        "Thành phố Hà Nội",
//                        BigDecimal.valueOf(2.3),
//                        119_000L,
//                        0L
//                )
//        );
//    }
//
//}
