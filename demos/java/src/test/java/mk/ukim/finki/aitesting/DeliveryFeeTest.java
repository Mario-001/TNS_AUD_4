package mk.ukim.finki.aitesting;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("reviewed")
class DeliveryFeeTest {
    @ParameterizedTest
    @CsvSource({"1,false,500", "4999,false,500", "5000,false,0", "5001,false,0",
            "4999,true,800", "5000,true,300", "1000000,true,300"})
    void appliesDocumentedThresholdAndExpressFee(int orderCents, boolean express, int expected) {
        assertEquals(expected, new DeliveryFee().calculate(orderCents, express));
    }
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1000001})
    void rejectsInvalidOrderValue(int value) {
        assertThrows(IllegalArgumentException.class, () -> new DeliveryFee().calculate(value, false));
    }
}
