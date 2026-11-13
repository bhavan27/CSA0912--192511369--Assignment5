# Task B3 – Statement vs PreparedStatement vs Batch

**Requirements:** JDK 17+, MySQL Server and MySQL Connector/J.

1. Run `sql/schema.sql` to create `co5_transport.bus_pass`.
2. Configure `CO5_DB_URL`, `CO5_DB_USER`, `CO5_DB_PASSWORD`.
3. From `src`, compile and run with Connector/J on the classpath:
```powershell
javac -cp ".;C:\path\to\mysql-connector-j.jar" *.java
java -cp ".;C:\path\to\mysql-connector-j.jar" PassLoaderBenchmark
```
The benchmark verifies 10 rows and ResultSetMetaData, then measures 1,000, 10,000 and 100,000 rows for Statement, PreparedStatement and PreparedStatement batch. Each timed approach uses a single transaction for a fair comparison. It also demonstrates auto-commit partial persistence and transaction rollback after a forced failure.

**Execution status:** Java source compilation is verified. Database execution, timing data and database screenshots remain pending because MySQL was not available in the build environment. Do not treat the status image as actual database evidence.
