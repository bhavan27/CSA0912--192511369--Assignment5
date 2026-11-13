# B3 measurement and interpretation
The benchmark executes Statement, PreparedStatement and PreparedStatement batch inserts for 10, 1,000, 10,000 and 100,000 rows. It prints actual timings from the configured local MySQL server. Run multiple trials and compare medians; results depend on server configuration, network latency, indexes, storage engine, JDBC driver and batch rewrite settings.

Expected engineering trade-offs:
- Statement requires constructing SQL text for each row and is unsafe for untrusted values if concatenated.
- PreparedStatement separates SQL and values and can reuse a parsed statement; one executeUpdate per row still incurs many round trips.
- Batching groups parameter sets and reduces client/server round trips; it is generally suitable for bulk imports.
- A transaction provides all-or-nothing behaviour. With auto-commit on, earlier inserts can remain committed if a later insert fails. With a single transaction, rollback removes all inserts in that transaction.
- For a nightly 100,000-row import, use PreparedStatement batching in a transaction. For one pass at a counter, use a single PreparedStatement execution inside a short transaction.

Benchmark fairness note: Statement, PreparedStatement and PreparedStatement batch are each timed inside one transaction per dataset. The separate B3.3 test then demonstrates auto-commit partial persistence versus rollback of one transaction. This keeps transaction mode consistent for the B3.2 performance comparison.
