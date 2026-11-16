# Parcel booking behavior

This independent demonstration uses Java 17/JUnit 5/Mockito and Python 3.10+/pytest.
It does not contain solutions to the Mini Shop or Mini Rental homework.

## Delivery fee

- Order value is integer cents, in the inclusive range 1 to 1,000,000.
- Invalid values raise `IllegalArgumentException` in Java or `ValueError` in Python.
- Standard delivery costs 500 cents below 5,000 cents. At exactly 5,000 and above it is free.
- Express adds 300 cents regardless of order value.
- Inputs in the Python demonstration use the documented types. Boolean-as-integer,
  float and coercion policies are outside this demonstration's contract.

## Booking

- Validate nonblank reference, nonblank customer, then order value, before any external call.
- Preserve supplied reference and customer strings without trimming them.
- Ask the label gateway once for a label. A label must be a nonblank string.
- If validation or the gateway fails, do not save a shipment.
- Save once with reference, customer, calculated fee and returned label, then return it.
- If saving fails, propagate the failure. The external label was already created.
  This example has no distributed rollback or cancellation mechanism.
- In Java a missing label causes IllegalStateException. In Python it causes RuntimeError.

## SDK adapter and persistence

The adapter calls `sdk.account("teaching").labels().create(customer)`.
The SDK is represented by interfaces, not a live vendor service. Adapter mock tests
check calls and mapping only. They do not establish vendor compatibility.

Persistence requires a unique reference and all other fields. Repository operations
use real JDBC/H2 or SQLite and a new connection for each operation. JDBC uses its
default auto-commit; SQLite commits its context on successful completion. Duplicate
references fail, and the existing row remains. The Java repository wraps SQL exceptions
in IllegalStateException; Python exposes sqlite3.IntegrityError.
