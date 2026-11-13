# Task B2 – Engineer an Adversarial Input

**Requirements:** JDK 17+, MySQL Server and MySQL Connector/J.

1. Execute `sql/schema.sql` in MySQL Workbench or the MySQL command line. It creates the assignment-only `co5_transport.test_users` table and one test account.
2. Set `CO5_DB_URL`, `CO5_DB_USER` and `CO5_DB_PASSWORD` environment variables (defaults are localhost:3306/co5_transport, root, blank password).
3. From `src`, compile with Connector/J on the classpath:
```powershell
javac -cp ".;C:\path\to\mysql-connector-j.jar" LoginSecurityTest.java DamagedPassFileTest.java
java -cp ".;C:\path\to\mysql-connector-j.jar" LoginSecurityTest
java DamagedPassFileTest ..\input\damaged-passes.csv
```
The damaged-file test was executed and its actual output is included. The unsafe-vs-PreparedStatement login comparison was not executed here because no MySQL server/credentials were available; its output is explicitly marked pending.
