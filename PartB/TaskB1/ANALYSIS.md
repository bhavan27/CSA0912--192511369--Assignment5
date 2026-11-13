# B1 test interpretation template
| Test | Input | Expected/observed purpose |
|---|---|---|
| Normal find | P9001 | Confirms a valid record can be appended and retrieved. |
| Missing ID | P-NOT-FOUND | Confirms null/NOT FOUND is returned. |
| Damaged field count | `BAD,LINE` | Confirms malformed row is skipped and scanning continues. |
| Damaged numeric field | amount=`not-a-number` | Confirms numeric validation rejects the row. |
| Empty line | empty record | Confirms the search continues past an empty row. |

The runner prints actual elapsed times for 1 MB, 10 MB and 50 MB line counting, and for lookups in files with 1,000, 10,000 and 100,000 rows. Timings depend on the computer, filesystem cache, JDK and background load. Do not treat one run as a universal benchmark; repeat runs and compare medians for a formal report.

Expected asymptotic behaviour:
- A streaming scan to find an ID is O(n) time and O(buffer size) extra memory.
- Building a HashMap once is O(n) expected time and O(n) memory; each later lookup is O(1) expected. For q lookups, scan-each-time is O(qn), while build-once-and-query is O(n+q).

## Comparison with Part A
Part A models seven fields. The provided B1 Complete-the-Code skeleton fixes the implementation to five fields (`passId, regNo, name, route, amount`); the implementation retains that required signature and expected output. Boarding stop and validity date are therefore not represented in B1. The simple `split`-based parser also does not support quoted commas in names. These are skeleton constraints, not claims that the complete Part A design has changed.

## Measured interpretation
The actual timing values are recorded in `output/measurement-summary.csv` and `output/execution-output.txt`. The 50 MB run took 20,574 ms unbuffered and 41 ms buffered on the build machine. The lookup scan grew from 20 ms at 1,000 records to 124 ms at 100,000 records. A fair repeated experiment should run multiple trials and compare medians because file-system cache and machine load affect elapsed time.
