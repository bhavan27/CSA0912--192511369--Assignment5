# B2 – Expected Behaviour and Actual Results

## Expected behaviour
- The crafted SQL payload may alter the concatenated unsafe query and cause an invalid login to be accepted.
- PreparedStatement must treat the same payload as a literal parameter and reject it.
- The malformed bus-pass file should accept valid rows and reject wrong-field-count rows, non-numeric amounts and empty lines.

## Actual file-validation output
See `output/damaged-file-test-output.txt` and `output/B2_damaged_file_execution.png`.
The executed file test accepted 2 valid rows and rejected 3 damaged rows:
- line 2: wrong number of fields
- line 3: non-numeric route/amount
- line 5: empty line
The actual output is in `output/damaged-file-test-output.txt` and the screenshot is `output/B2_damaged_file_execution.png`.

## Database login comparison
Not executed in the build environment because a local MySQL Server and Connector/J connection were not available. The source, SQL setup script and crafted input are included. Run `LoginSecurityTest.java` locally and save the actual console output and screenshot here before submission. No unsafe/safe login result is claimed as an observed result in this package.
