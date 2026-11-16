package mk.ukim.finki.aitesting;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import mk.ukim.finki.aitesting.Ports.*;
import static org.mockito.Mockito.*;

@Tag("reviewed")
class CarrierAdapterTest {
    @Test void configuresTheSdkChainExplicitly() {
        CarrierSdk sdk = mock(CarrierSdk.class);
        CarrierAccount account = mock(CarrierAccount.class);
        LabelApi labels = mock(LabelApi.class);
        when(sdk.account("teaching")).thenReturn(account);
        when(account.labels()).thenReturn(labels);
        when(labels.create("Ana")).thenReturn("LBL-7");

        assertEquals("LBL-7", new CarrierAdapter(sdk).createLabel("Ana"));
        verify(sdk).account("teaching");
        verify(labels).create("Ana");
    }
    @Test void usesDeepStubsForComparison() {
        CarrierSdk sdk = mock(CarrierSdk.class, RETURNS_DEEP_STUBS);
        when(sdk.account("teaching").labels().create("Ana")).thenReturn("LBL-7");

        assertEquals("LBL-7", new CarrierAdapter(sdk).createLabel("Ana"));
        verify(sdk.account("teaching").labels()).create("Ana");
    }
}
