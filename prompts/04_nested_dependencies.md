# Dependency boundaries and nested mocks

Context: SPECIFICATION.md, ShipmentService.java, CarrierAdapter.java and Ports.java.
Python: delivery.py.

> Explain the difference between testing ShipmentService with a mocked LabelGateway
> and testing the real CarrierAdapter with an SDK double. Draw the dependency chain
> in words. Implement the adapter test with explicit SDK, account and labels mocks.
> Explain why mocking LabelGateway prevents execution of CarrierAdapter and its SDK.
> Then show a separate Mockito RETURNS_DEEP_STUBS comparison [Python: chained Mock
> return values]. Explain coupling and what the test does not prove. Do not present
> shorter setup as evidence of stronger testing. Identify return values that are
> mocks rather than real values. Use real Shipment records/dataclasses.
