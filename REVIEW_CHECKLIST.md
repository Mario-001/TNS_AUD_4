# Review of generated tests

For every proposed test, record:

| Item | Review question |
| --- | --- |
| Requirement | Which sentence of the specification does this test check? |
| Test boundary | Which implementations execute, and which dependencies are doubles? |
| Inputs | Which partition or boundary does each value represent? |
| Oracle | Where did the expected result come from? Compute it independently. |
| Assertion | Would a plausible wrong result still satisfy this assertion? |
| Failures | Are exception type and forbidden side effects checked? |
| Isolation | Could time, network, shared state or execution order affect the result? |
| Mock setup | Are return values concrete and compatible with the real interface? |
| Maintainability | Does the test depend on private structure unnecessarily? |
| Remaining risk | Which behavior or external contract remains unverified? |

Passing tests and high execution coverage do not establish that requirements are
correct or fully tested. A failing test can indicate a bad test, a production bug,
a dependency mismatch, or a broken environment. Diagnose before editing either side.

## Review exercise

A proposed test is:

```java
ShipmentService service = mock(ShipmentService.class);
Shipment expected = new Shipment("REF-7", "Ana", 300, "LBL-7");
when(service.book("REF-7", "Ana", 5000, true)).thenReturn(expected);
assertEquals(expected, service.book("REF-7", "Ana", 5000, true));
```

State which production behavior executes. Suggest a meaningful replacement and
one defect the replacement would detect. Then identify a risk that still requires
an integration or vendor-contract test.
