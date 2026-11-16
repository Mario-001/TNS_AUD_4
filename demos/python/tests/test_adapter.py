from unittest.mock import create_autospec, Mock
from delivery import CarrierSdk, CarrierAccount, LabelApi, CarrierAdapter


def test_explicit_sdk_chain():
    sdk = create_autospec(CarrierSdk, instance=True, spec_set=True)
    account = create_autospec(CarrierAccount, instance=True, spec_set=True)
    labels = create_autospec(LabelApi, instance=True, spec_set=True)
    sdk.account.return_value = account
    account.labels.return_value = labels
    labels.create.return_value = "LBL-7"
    assert CarrierAdapter(sdk).create_label("Ana") == "LBL-7"
    sdk.account.assert_called_once_with("teaching")
    labels.create.assert_called_once_with("Ana")


def test_unspecced_mock_accepts_a_typo():
    labels = Mock()
    labels.cretae.return_value = "LBL-7"
    assert labels.cretae("Ana") == "LBL-7"


def test_spec_rejects_a_typo():
    import pytest
    labels = create_autospec(LabelApi, instance=True, spec_set=True)
    with pytest.raises(AttributeError):
        labels.cretae.return_value = "LBL-7"
