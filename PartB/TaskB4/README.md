# Task B4 – Final Engineering Decision

**Requirements:** JDK 17+, MySQL Server and MySQL Connector/J.

1. Run `sql/schema.sql` to create `co5_transport.bus_pass`.
2. Configure `CO5_DB_URL`, `CO5_DB_USER`, `CO5_DB_PASSWORD`.
3. From `src`, compile and run with Connector/J on the classpath:
```powershell
javac -cp ".;C:\path\to\mysql-connector-j.jar" *.java
java -cp ".;C:\path\to\mysql-connector-j.jar" MigrationProgram ..\input\legacy-passes.csv ..\output
```
The program validates legacy rows, writes a rejection report, loads valid rows using PreparedStatement batches in a transaction and prints DatabaseMetaData.

The CSV validation-only preview was executed; actual MySQL migration and metadata inspection remain pending because MySQL was not available in the build environment. The validation preview is not evidence of a completed database migration.
