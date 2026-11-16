package mk.ukim.finki.aitesting;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
@Tag("weak")
class WeakFeeTest {
    @Test void calculatesDelivery() {
        DeliveryFee fee = new DeliveryFee();
        for (int value : new int[]{100, 6000}) {
            for (boolean express : new boolean[]{false, true}) {
                assertTrue(fee.calculate(value, express) >= 0);
            }
        }
    }
}
