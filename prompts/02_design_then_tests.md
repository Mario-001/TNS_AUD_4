# Design before generation

Context: SPECIFICATION.md and DeliveryFee.java. Python: delivery.py.

> First propose a compact test-case table for the delivery fee calculator.
> For every row give its requirement, inputs and independently calculated expected
> result or exception. Include equivalence partitions, exact thresholds and nearby
> values, lower and upper input bounds, and express behavior at the threshold.
> Do not generate code yet. Flag any ambiguity instead of inventing a rule.

After reviewing the table:

> Generate JUnit 5 parameterized tests using CsvSource and ValueSource [or pytest
> parametrize] for the approved table. Instantiate the real calculator. Use exact
> expected values from the table. Do not reproduce the production formula in the
> assertion and do not change production code or dependencies. Include imports.
> Explain the behavior each test detects and list remaining gaps.
