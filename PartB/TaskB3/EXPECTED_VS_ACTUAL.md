# B3 – Expected Behaviour and Actual Results

## Expected behaviour
- Each of Statement, PreparedStatement and PreparedStatement batch inserts 10 rows correctly.
- SELECT and ResultSetMetaData report the table's five columns and their database types.
- Timings are recorded for 1,000, 10,000 and 100,000 records for all three approaches.
- With auto-commit ON, rows inserted before a forced failure remain committed.
- With a single transaction, a failure causes rollback and no rows from that transaction remain.

## Actual database output
Not executed in the build environment because no local MySQL Server/Connector-J connection was available. The Java source and `sql/schema.sql` are included. `output/execution-status.txt` and `output/B3_mysql_execution_required.png` identify this clearly.

Run `PassLoaderBenchmark.java` against your MySQL database, then save the actual full console output, screenshot and measured timing CSV in this folder. Do not fill the report with estimated or invented timings.
