import pytest
from delivery import delivery_fee


@pytest.mark.parametrize("value,express,expected", [
    (1, False, 500), (4999, False, 500), (5000, False, 0),
    (5001, False, 0), (4999, True, 800), (5000, True, 300),
    (1_000_000, True, 300),
])
def test_fee(value, express, expected):
    assert delivery_fee(value, express) == expected


@pytest.mark.parametrize("value", [-1, 0, 1_000_001])
def test_invalid(value):
    with pytest.raises(ValueError):
        delivery_fee(value, False)
