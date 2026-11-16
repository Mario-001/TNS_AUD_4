from types import SimpleNamespace
import sqlite3
import pytest
from delivery import SqliteShipmentRepository, ShipmentService, Shipment


@pytest.mark.integration
def test_real_service_repository_and_database(tmp_path):
    repository = SqliteShipmentRepository(tmp_path / "shipments.sqlite")
    repository.initialize()
    stub = SimpleNamespace(create_label=lambda customer: "LBL-7")
    service = ShipmentService(repository, stub)
    service.book("REF-7", "Ana", 5000, True)
    assert repository.find("REF-7") == Shipment("REF-7", "Ana", 300, "LBL-7")
    assert repository.find("MISSING") is None
    with pytest.raises(sqlite3.IntegrityError):
        service.book("REF-7", "Ana", 5000, True)
    assert repository.find("REF-7").customer == "Ana"
