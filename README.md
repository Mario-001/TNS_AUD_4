# AI-assisted testing practical

Read `SPECIFICATION.md` before evaluating the code or tests. The examples are
prepared teaching specimens, not captured responses from a particular AI product.
Live model output will vary. `weak` examples intentionally illustrate inadequate
assertions. Reviewed examples improve confidence but are not an exhaustive test suite.

## Requirements

- Java demonstrations: JDK 17+, internet for the first Maven run. The Maven wrapper
  is included. H2 runs in the test process. No Spring or Docker setup is needed.
- Python demonstrations: Python 3.10+ and the packages in `requirements.txt`.
- Live generation: an AI chat tool that accepts source files, or your existing IDE
  assistant. Use the prompts in `prompts/`. No particular paid product is required
  to run the prepared demonstrations.

## Java (Windows PowerShell)

From `demos/java`:

```powershell
.\mvnw.cmd -version
.\mvnw.cmd test
.\mvnw.cmd -Pweak test
.\mvnw.cmd test "-Dtest=DeliveryFeeTest"
.\mvnw.cmd test "-Dtest=ShipmentServiceTest"
.\mvnw.cmd test "-Dtest=CarrierAdapterTest"
.\mvnw.cmd test "-Dtest=ShipmentIntegrationTest"
```

Linux/macOS: replace `.\mvnw.cmd` with `sh mvnw`.
The ordinary run excludes the weak specimen. The `weak` profile selects it instead.
The integration class ends in `Test`, so Surefire runs it in the `test` phase.

## Python (Windows PowerShell)

From `demos/python`:

```powershell
py -m venv .venv
.\.venv\Scripts\python.exe -m pip install -r requirements.txt
.\.venv\Scripts\python.exe -m pytest -q
.\.venv\Scripts\python.exe -m pytest weak_tests -q
.\.venv\Scripts\python.exe -m pytest tests/test_adapter.py -v
.\.venv\Scripts\python.exe -m pytest -m integration -v
```

Linux/macOS: create the environment with `python3 -m venv .venv`, then use
`.venv/bin/python` in place of `.\.venv\Scripts\python.exe`.
The default Python run selects `tests/` and excludes `weak_tests/`.

## Controlled fault demonstration

From the package root, using a Python interpreter with pytest installed:

```powershell
.\demos\python\.venv\Scripts\python.exe tools/compare_mutant.py --language python
.\demos\python\.venv\Scripts\python.exe tools/compare_mutant.py --language java
```

The script makes an isolated temporary copy, verifies the baseline, changes the
free-delivery boundary from `>= 5000` to `> 5000`, then compares weak and reviewed
tests. It leaves the original project unchanged. Expected: weak tests still pass;
reviewed tests fail with assertion failures. Compilation and setup errors do not
count as detection. This is one controlled fault, not a full mutation score.

## Discussion files

`REVIEW_CHECKLIST.md` supports peer review. `prompts/` contains copyable prompts
and exact context-file lists. Create live-generated tests in a separate copy or
branch so they do not overwrite the prepared comparisons.
