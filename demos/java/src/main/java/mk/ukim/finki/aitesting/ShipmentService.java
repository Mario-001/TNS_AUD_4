package mk.ukim.finki.aitesting;

import java.util.Objects;
import mk.ukim.finki.aitesting.Ports.*;

public class ShipmentService {
    private final ShipmentRepository repository;
    private final LabelGateway gateway;
    private final DeliveryFee calculator;
    public ShipmentService(ShipmentRepository repository, LabelGateway gateway, DeliveryFee calculator) {
        this.repository = Objects.requireNonNull(repository);
        this.gateway = Objects.requireNonNull(gateway);
        this.calculator = Objects.requireNonNull(calculator);
    }
    public Shipment book(String reference, String customer, int orderCents, boolean express) {
        if (reference == null || reference.isBlank()) throw new IllegalArgumentException("Reference required");
        if (customer == null || customer.isBlank()) throw new IllegalArgumentException("Customer required");
        int fee = calculator.calculate(orderCents, express);
        String label = gateway.createLabel(customer);
        if (label == null || label.isBlank()) throw new IllegalStateException("Carrier returned empty label");
        Shipment shipment = new Shipment(reference, customer, fee, label);
        repository.save(shipment);
        return shipment;
    }
}
