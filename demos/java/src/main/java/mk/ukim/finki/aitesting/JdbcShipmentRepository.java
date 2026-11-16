package mk.ukim.finki.aitesting;

import java.sql.*;
import java.util.Optional;
import mk.ukim.finki.aitesting.Ports.*;

public class JdbcShipmentRepository implements ShipmentRepository {
    private final String url;
    public JdbcShipmentRepository(String url) { this.url = url; }
    public void initialize() {
        try (Connection c = DriverManager.getConnection(url); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE shipments (reference VARCHAR(60) PRIMARY KEY, customer VARCHAR(100) NOT NULL, fee INTEGER NOT NULL, label VARCHAR(100) NOT NULL)");
        } catch (SQLException e) { throw new IllegalStateException(e); }
    }
    public void save(Shipment shipment) {
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement s = c.prepareStatement("INSERT INTO shipments(reference, customer, fee, label) VALUES (?, ?, ?, ?)")) {
            s.setString(1, shipment.reference()); s.setString(2, shipment.customer());
            s.setInt(3, shipment.feeCents()); s.setString(4, shipment.label());
            s.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException(e); }
    }
    public Optional<Shipment> find(String reference) {
        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement s = c.prepareStatement("SELECT reference, customer, fee, label FROM shipments WHERE reference=?")) {
            s.setString(1, reference);
            try (ResultSet result = s.executeQuery()) {
                return result.next() ? Optional.of(new Shipment(result.getString(1), result.getString(2), result.getInt(3), result.getString(4))) : Optional.empty();
            }
        } catch (SQLException e) { throw new IllegalStateException(e); }
    }
}
