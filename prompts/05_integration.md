# Integration with a stub outside the boundary

Context: SPECIFICATION.md and all main Java sources plus pom.xml.
Python: delivery.py and pytest.ini.

> Generate an integration test for the real service, fee calculator, repository
> and isolated H2 [SQLite] database. Stub only LabelGateway. Assert exact persisted
> values by reading through a new connection, missing-reference behavior and the
> duplicate-reference constraint. Use a unique database per test and cleanup.
> Do not introduce Docker, Spring or a mocked repository. Explain the boundary and
> why this does not prove compatibility with a production database or carrier SDK.
> Do not modify production code to make a test pass.
