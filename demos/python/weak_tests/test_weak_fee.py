from delivery import delivery_fee


def test_nonnegative_fee():
    for value in (100, 6000):
        for express in (False, True):
            assert delivery_fee(value, express) >= 0
