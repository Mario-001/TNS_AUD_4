# Service and failure behavior

Context: SPECIFICATION.md, ShipmentService.java, DeliveryFee.java and Ports.java.
Python: delivery.py.

> Generate isolated unit tests for the real ShipmentService. Use real calculation
> and doubles only for repository and label gateway. Use JUnit 5 with MockitoExtension
> [or pytest with create_autospec(..., instance=True, spec_set=True)]. Test saved
> field values, invalid-input isolation, gateway failure, missing label and save
> failure. Capture arguments where useful. Verify ordering only where required by
> the specification. No sleeps, network, database or production edits. State what
> these tests cannot establish. Do not mock ShipmentService itself.
