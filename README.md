# CSA09 – Programming in Java | Assignment 5 (CO5)
## Data Persistence Engineering – Files and Databases

**Student:** V. C. Bhavan Bairav  
**Register Number:** 192511369  
**Course:** Programming in Java – CSA0912 (CO5)  
**Assignment:** Assignment 5 – 50 Marks

This repository contains my Java Assignment 5, covering file handling, data persistence, database connectivity and performance analysis. It is organised into Part A and Part B according to the assignment requirements.

## Repository Structure

```text
PartA/
  TaskA1/ ... TaskA5/
    README.md
    DESIGN.md
    PSEUDOCODE.md
    input/
    output/

PartB/
  TaskB1/ ... TaskB4/
    README.md
    src/
    sql/
    input/
    output/
    ANALYSIS.md
```

## Part A – Persistence Design and Pseudocode

Part A focuses on the design aspects of data persistence. It covers record formats, buffered I/O, data-access patterns, object serialization and database transactions.

The five tasks include:

- **A1:** File format design using text and binary storage.
- **A2:** Buffered and unbuffered I/O design.
- **A3:** Data Access Object (DAO) pattern.
- **A4:** Java object serialization.
- **A5:** Database transactions and SQL injection prevention.

This section contains design explanations, diagrams, reasoning and pseudocode. It does not contain Java implementations, as the assignment requires pseudocode for Part A.

## Part B – Implementation and Experimental Investigation

Part B contains the Java programs, SQL scripts, test inputs, execution outputs and performance analysis.

| Task | Description |
|---|---|
| B1 | Instrumented file store with record operations and performance measurements |
| B2 | Adversarial input validation and SQL injection testing |
| B3 | Statement, PreparedStatement, batch processing and transaction comparison |
| B4 | Data migration validation and final persistence decision |

## Java and Database Requirements

- **Java:** JDK 17 or later.
- **Database:** MySQL Server.
- **JDBC Driver:** MySQL Connector/J.

For database-related tasks, execute the respective `sql/schema.sql` file to create the required database and tables.

The following environment variables are used for database connectivity:

```text
CO5_DB_URL=jdbc:mysql://localhost:3306/co5_transport
CO5_DB_USER=root
CO5_DB_PASSWORD=your_local_password
```

Database credentials are kept outside the source code to avoid exposing sensitive information in the repository.

## Compile and Run

For JDBC-based tasks, include the MySQL Connector/J JAR in the classpath.

**Windows PowerShell:**

```powershell
javac -cp ".;C:\path\to\mysql-connector-j.jar" *.java
java -cp ".;C:\path\to\mysql-connector-j.jar" ClassName
```

**macOS/Linux:** Use `:` instead of `;` as the classpath separator.

## Execution Status and Evidence

- **B1:** Compiled and executed. Includes record addition, searching, route filtering, damaged-line handling, file-reading timings and lookup-growth measurements.
- **B2:** Damaged-file validation compiled and executed. The SQL injection login comparison requires MySQL and Connector/J.
- **B3:** JDBC insert comparisons and transaction tests require a configured MySQL environment. Database benchmark results are not included because the tests have not been executed.
- **B4:** CSV validation preview compiled and executed, including valid and rejected records. Database migration and DatabaseMetaData inspection require MySQL and Connector/J.

The output folders contain the available test results, screenshots and measurement data. Pending database experiments are identified separately rather than presented as completed results.

## Progressive Git Commits

The repository follows a task-wise structure to keep the design, implementation and testing evidence organised. Progressive commits maintain a clear development history, with at least one meaningful commit for each task.

## Assignment Overview

The assignment covers the complete persistence workflow, from designing file formats and algorithms to implementing file operations, validating input, investigating database performance and selecting a suitable storage solution.

**Total: 50 Marks – Part A: 25 Marks | Part B: 25 Marks**
