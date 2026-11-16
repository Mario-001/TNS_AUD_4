"""Run a controlled boundary mutation in an isolated temporary copy."""
import argparse
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]


def run(project, language, weak):
    if language == "python":
        report = project / "result.xml"
        selection = "weak_tests" if weak else "tests/test_fee.py"
        command = [sys.executable, "-m", "pytest", selection, "-q", "--junitxml=" + str(report)]
    else:
        wrapper = ["cmd", "/c", "mvnw.cmd"] if sys.platform == "win32" else ["sh", "mvnw"]
        command = wrapper + ["-B", "-ntp", "test"]
        command += ["-Pweak", "-Dtest=WeakFeeTest"] if weak else ["-Dtest=DeliveryFeeTest"]
        report = project / "target/surefire-reports/TEST-mk.ukim.finki.aitesting.DeliveryFeeTest.xml"
    completed = subprocess.run(command, cwd=project, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    print(completed.stdout)
    failures = errors = 0
    if report.exists():
        tree = ET.parse(report)
        failures = len(tree.findall(".//failure"))
        errors = len(tree.findall(".//error"))
    return completed.returncode, failures, errors


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--language", choices=["java", "python"], required=True)
    args = parser.parse_args()
    with tempfile.TemporaryDirectory(prefix="ai-testing-") as directory:
        project = Path(directory) / args.language
        shutil.copytree(ROOT / "demos" / args.language, project,
                        ignore=shutil.ignore_patterns("target", ".venv", "__pycache__", ".pytest_cache"))
        print("Baseline: reviewed fee tests must pass")
        if run(project, args.language, False)[0] != 0:
            raise SystemExit("Baseline failed. Fix setup before interpreting the mutation.")
        source = project / ("delivery.py" if args.language == "python" else
                            "src/main/java/mk/ukim/finki/aitesting/DeliveryFee.java")
        text = source.read_text()
        if text.count(">= 5_000") != 1:
            raise SystemExit("Expected exactly one boundary expression")
        source.write_text(text.replace(">= 5_000", "> 5_000"))
        print("Mutant: free delivery incorrectly starts above 5000")
        weak = run(project, args.language, True)
        strong = run(project, args.language, False)
        if weak[0] == 0 and strong[0] != 0 and strong[1] > 0 and strong[2] == 0:
            print("EXPECTED RESULT: weak tests pass; reviewed tests detect the wrong boundary.")
        else:
            raise SystemExit("Unexpected result. Inspect output; compilation or setup errors are not mutation detection.")


if __name__ == "__main__":
    main()
