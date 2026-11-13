# CSA09 – Programming in Java | Assignment 5 – CO5
## Data Persistence Engineering – Files and Databases

**Student Name:** ______________________________
**Register Number:** ____________________________
**Java version used for build checks:** OpenJDK 21

This repository is arranged to match the assignment PDF's submission requirements.

## Submission structure

```text
PartA/
  TaskA1/ ... TaskA5/
    README.md
    DESIGN.md
    PSEUDOCODE.md (where separately required)
    input/
    output/ (design diagrams)
PartB/
  TaskB1/ ... TaskB4/
    README.md
    src/       Java source
    sql/       database/table creation scripts
    input/     test records and adversarial inputs
    output/    expected behaviour, actual outputs, screenshots, measurements
    ANALYSIS.md
```

## Part A – Persistence Design & Pseudocode (25 marks)
Part A contains record layouts, algorithmic pseudocode, diagrams and reasoning for A1–A5. **There are no Java source files in Part A**, because the PDF explicitly says Java code will not be accepted in place of pseudocode.

## Part B – Implementation & Experimental Investigation (25 marks)
Part B contains Java source, SQL scripts, test inputs, expected behaviour, actual outputs where executed, measurement data and interpretation.

### Execution status and evidence honesty
- B1 was compiled and executed. Actual add/find/list, damaged-line, line-count timing and lookup-growth output are included.
- B2 damaged-file validation was compiled and executed. The SQL-injection login comparison requires local MySQL and Connector/J; its pending status is explicitly labelled.
- B3 JDBC insert comparisons and transaction tests require local MySQL and Connector/J. No database timings or outcomes have been fabricated.
- B4 CSV validation preview was compiled and executed; actual validation output and rejected-line report are included. The database migration and DatabaseMetaData inspection still require local MySQL and Connector/J.

## Java and database setup
Use JDK 17 or later. For JDBC tasks, install MySQL Server and MySQL Connector/J, then execute the task's `sql/schema.sql`.

Set environment variables:
- `CO5_DB_URL=jdbc:mysql://localhost:3306/co5_transport`
- `CO5_DB_USER=root`
- `CO5_DB_PASSWORD=your_local_password`

Do not put a real password into source code or commit it to GitHub.

For Windows PowerShell, include the Connector/J JAR in the classpath using `;`:
```powershell
javac -cp ".;C:\path\to\mysql-connector-j.jar" *.java
java -cp ".;C:\path\to\mysql-connector-j.jar" ClassName
```
For macOS/Linux, use `:` as the classpath separator.

## Progressive GitHub commits
The assignment asks for at least one commit per task. Suggested commit messages:
- `Complete Task A1 – file format design`
- `Complete Task A2 – buffered I/O design`
- `Complete Task A3 – DAO design`
- `Complete Task A4 – serialization design`
- `Complete Task A5 – transaction and injection design`
- `Implement Task B1 – instrumented file store`
- `Implement Task B2 – adversarial input tests`
- `Implement Task B3 – JDBC comparison and batch tests`
- `Implement Task B4 – migration and metadata`

## Before final submission
Run the remaining MySQL-dependent tests on your machine. Save the complete console output and screenshots showing the test inputs/results, not just a final success message. Add the actual B2 login, B3 benchmark/transaction and B4 migration/metadata evidence into the corresponding `PartB/TaskB*/output/` folders.
