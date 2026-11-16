package mk.ukim.finki.aitesting;

public final class Ports {
    private Ports() { }
    public record Shipment(String reference, String customer, int feeCents, String label) { }
    public interface LabelGateway { String createLabel(String customer); }
    public interface ShipmentRepository {
        void save(Shipment shipment);
        java.util.Optional<Shipment> find(String reference);
    }
    public interface CarrierSdk { CarrierAccount account(String id); }
    public interface CarrierAccount { LabelApi labels(); }
    public interface LabelApi { String create(String customer); }
}
