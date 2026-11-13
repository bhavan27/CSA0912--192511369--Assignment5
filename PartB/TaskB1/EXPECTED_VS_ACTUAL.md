# B1 – Expected Behaviour, Actual Output and Interpretation

## Expected behaviour and test cases

| Test | Input | Expected behaviour |
|---|---|---|
| Add/find normal record | P9001 | Appended record can be found and all five fields returned |
| Missing ID | P-NOT-FOUND | `find` returns `null` |
| Route listing | Route 12 | All valid matching records are listed |
| Damaged field count | `BAD,LINE` | Row is skipped; later valid rows remain searchable |
| Invalid numeric amount | `not-a-number` | Row is skipped |
| Empty line | Empty row | Row is skipped |

## Actual output

See `output/execution-output.txt` and `output/B1_execution_screenshot.png`.

Observed file-store output:
```text
Find P9001: [P9001, 192319001, Kavya, 12, 4500.0]
Find P-NOT-FOUND: null
Route 12 records: 3
Damaged-file find P3: [P3, R3, Valid, 5, 200.0]
Damaged-file find P2: null
```

Observed measurements (milliseconds, one execution on the build machine):

| File size | Unbuffered | Buffered |
|---:|---:|---:|
| 1 MB | 447 | 11 |
| 10 MB | 4,223 | 21 |
| 50 MB | 20,574 | 41 |

| Records | Time to find last record |
|---:|---:|
| 1,000 | 20 ms |
| 10,000 | 67 ms |
| 100,000 | 124 ms |

Interpretation: the buffered version was faster in this run because it reads blocks. Sequential search time increased as the target moved further into larger files. These are one-run machine-specific measurements, not universal guarantees.

## B1.2 – Comparison with Part A design

The Part A scenario defines seven fields: pass ID, register number, name, route number, boarding stop, validity date and amount paid. The supplied B1 Complete-the-Code skeleton explicitly defines a five-field `add()` method and expects five fields in `find()`. The implementation follows that supplied skeleton without changing its method signature or expected output. Therefore, the B1 code omits boarding stop and validity date. This is a limitation of the provided skeleton and should be stated when comparing the implementation with the full seven-field Part A design.

The supplied skeleton also uses a simple comma-delimited representation. It does not implement full CSV quote escaping for commas inside names. The Part A design specifies quote-aware CSV handling; the five-field skeleton is retained for the assessed TODO as instructed.
