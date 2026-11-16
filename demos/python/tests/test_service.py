from unittest.mock import create_autospec
import pytest
from delivery import Shipment, ShipmentService, ShipmentRepository, LabelGateway


def setup_service():
    repository = create_autospec(ShipmentRepository, instance=True, spec_set=True)
    gateway = create_autospec(LabelGateway, instance=True, spec_set=True)
    return ShipmentService(repository, gateway), repository, gateway


def test_saved_values():
    service, repository, gateway = setup_service()
    gateway.create_label.return_value = "LBL-7"
    result = service.book("REF-7", "Ana", 5000, True)
    assert result == Shipment("REF-7", "Ana", 300, "LBL-7")
    repository.save.assert_called_once_with(result)
    gateway.create_label.assert_called_once_with("Ana")


def test_invalid_input_has_no_interactions():
    service, repository, gateway = setup_service()
    with pytest.raises(ValueError):
        service.book(" ", "Ana", 5000, False)
    repository.save.assert_not_called()
    gateway.create_label.assert_not_called()


def test_gateway_failure_does_not_save():
    service, repository, gateway = setup_service()
    failure = RuntimeError("carrier unavailable")
    gateway.create_label.side_effect = failure
    with pytest.raises(RuntimeError) as error:
        service.book("REF-7", "Ana", 5000, False)
    assert error.value is failure
    repository.save.assert_not_called()


def test_missing_return_value_does_not_become_a_real_label():
    service, repository, gateway = setup_service()
    with pytest.raises(RuntimeError, match="empty label"):
        service.book("REF-7", "Ana", 5000, False)
    repository.save.assert_not_called()


def test_save_failure_propagates_after_label_creation():
    service, repository, gateway = setup_service()
    gateway.create_label.return_value = "LBL-7"
    failure = RuntimeError("storage unavailable")
    repository.save.side_effect = failure
    with pytest.raises(RuntimeError) as error:
        service.book("REF-7", "Ana", 5000, False)
    assert error.value is failure
    gateway.create_label.assert_called_once_with("Ana")
