# Task B1 – Implement and Instrument the File Store

**Requirements:** JDK 17+; no external database or library required.

From this folder:
```powershell
cd src
javac PassStore.java BufferedLineCounter.java B1Runner.java
java B1Runner ..\input\passes.csv ..\output
```
The runner tests add/find/missing ID/route listing/damaged rows, then measures 1 MB, 10 MB and 50 MB line counting and lookup growth at 1,000, 10,000 and 100,000 records. Actual output is in `output/execution-output.txt`; measurements are in `output/measurement-summary.csv`.

**Implementation note:** The assignment's supplied Complete-the-Code skeleton fixes PassStore to five fields. See `EXPECTED_VS_ACTUAL.md` for the comparison with the seven-field Part A design.
