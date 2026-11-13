# A3.1 – Common pattern
Repeated steps in StudentDB, CourseDB and MarksDB:
1. Obtain a JDBC Connection.
2. Build SQL.
3. Create Statement/PreparedStatement.
4. Execute the operation.
5. Read ResultSet rows and map columns to Java objects.
6. Close ResultSet, statement and connection.

The entity/table, SQL text, key fields, columns and object-mapping rules differ. Connection handling, statement execution, result iteration and resource cleanup are common.

# A3.2 – Generic DAO design
`Dao<T,K>` defines insert, findById, findAll, update and delete. A StudentDao supplies student-specific SQL and mapping. The generic interface and StudentDao operation sequence are expressed as algorithmic pseudocode in `PSEUDOCODE.md`; no Java implementation is submitted in Part A.

# A3.3 – Object/table mapping
| Student field | student column | Mapping |
|---|---|---|
| id | student_id | Primary key |
| name | student_name | VARCHAR |
| email | email | VARCHAR |
| department | department | VARCHAR |

# Alternative – SQL in service/GUI
If SQL is placed directly in service or GUI classes, a column rename may require edits in every class containing that SQL. With a DAO, SQL and row mapping are centralised in the StudentDao, so fewer places need modification. Both designs have broadly the same database-side time complexity for the same query (for example, indexed key lookup is typically O(log n) in a B-tree index, subject to DB implementation). DAO abstraction adds a small constant amount of Java call/indirection overhead, not a different asymptotic query complexity. Space per operation is determined mainly by result rows and JDBC buffers; the DAO pattern does not inherently change it.
