package mk.ukim.finki.aitesting;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import mk.ukim.finki.aitesting.Ports.*;
import static org.mockito.Mockito.*;

@Tag("reviewed")
@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {
    @Mock ShipmentRepository repository;
    @Mock LabelGateway gateway;
    ShipmentService service;
    @BeforeEach void setup() { service = new ShipmentService(repository, gateway, new DeliveryFee()); }

    @Test void savesTheCalculatedShipment() {
        when(gateway.createLabel("Ana")).thenReturn("LBL-7");
        Shipment result = service.book("REF-7", "Ana", 5000, true);
        assertEquals(new Shipment("REF-7", "Ana", 300, "LBL-7"), result);
        ArgumentCaptor<Shipment> captured = ArgumentCaptor.forClass(Shipment.class);
        verify(repository).save(captured.capture());
        assertEquals(result, captured.getValue());
        verify(gateway).createLabel("Ana");
    }
    @Test void invalidInputDoesNotContactDependencies() {
        assertThrows(IllegalArgumentException.class, () -> service.book(" ", "Ana", 5000, false));
        verifyNoInteractions(repository, gateway);
    }
    @Test void carrierFailureDoesNotSaveShipment() {
        IllegalStateException failure = new IllegalStateException("carrier unavailable");
        when(gateway.createLabel("Ana")).thenThrow(failure);
        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> service.book("REF-7", "Ana", 5000, false)));
        verifyNoInteractions(repository);
    }
    @Test void rejectsEmptyLabelWithoutSaving() {
        when(gateway.createLabel("Ana")).thenReturn("");
        assertThrows(IllegalStateException.class, () -> service.book("REF-7", "Ana", 5000, false));
        verifyNoInteractions(repository);
    }
    @Test void saveFailurePropagatesAfterLabelCreation() {
        when(gateway.createLabel("Ana")).thenReturn("LBL-7");
        IllegalStateException failure = new IllegalStateException("storage unavailable");
        doThrow(failure).when(repository).save(any());
        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> service.book("REF-7", "Ana", 5000, false)));
        InOrder order = inOrder(gateway, repository);
        order.verify(gateway).createLabel("Ana");
        order.verify(repository).save(any());
    }
}
