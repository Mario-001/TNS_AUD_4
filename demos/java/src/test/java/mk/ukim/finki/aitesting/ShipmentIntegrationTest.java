package mk.ukim.finki.aitesting;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import mk.ukim.finki.aitesting.Ports.*;

@Tag("reviewed")
class ShipmentIntegrationTest {
    @Test void persistsShipmentUsingRealServiceAndJdbcRepository() {
        String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        JdbcShipmentRepository repository = new JdbcShipmentRepository(url);
        repository.initialize();
        LabelGateway stub = customer -> "LBL-7";
        ShipmentService service = new ShipmentService(repository, stub, new DeliveryFee());
        try {
            service.book("REF-7", "Ana", 5000, true);
            assertEquals(new Shipment("REF-7", "Ana", 300, "LBL-7"), repository.find("REF-7").orElseThrow());
            assertTrue(repository.find("MISSING").isEmpty());
            assertThrows(IllegalStateException.class,
                    () -> service.book("REF-7", "Ana", 5000, true));
            assertEquals("Ana", repository.find("REF-7").orElseThrow().customer());
        } finally {
            try (var connection = java.sql.DriverManager.getConnection(url);
                 var statement = connection.createStatement()) {
                statement.execute("SHUTDOWN");
            } catch (java.sql.SQLException e) { throw new IllegalStateException(e); }
        }
    }
}
