# B4 – Expected Behaviour and Actual Results

## Expected behaviour
- The migration program reads each legacy record and validates its field count and numeric fields.
- Valid records are loaded using PreparedStatement batches in one transaction.
- Invalid rows are recorded with line number and rejection reason.
- DatabaseMetaData reports the database product/version, JDBC driver/version and available tables.

## Actual database output
The MySQL migration and metadata inspection were not executed in the build environment because no local MySQL Server/Connector-J connection was available. The Java source, SQL schema and legacy CSV are included. See `output/execution-status.txt`.

Run `MigrationProgram.java` locally and save the actual migration report, metadata output and screenshot in this folder before submission. No database migration success is claimed until that run is completed.

## Actual CSV-validation preview (executed)
The independent validation stage was executed without a database. See `output/validation-preview-actual-output.txt` and `output/B4_validation_preview_actual.png`. It accepted 4 valid rows and rejected 2 malformed rows. This verifies the input-validation stage only; it does not claim that the rows were inserted into MySQL.
