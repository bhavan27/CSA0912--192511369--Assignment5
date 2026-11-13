# Task A3 – Generic Data-Access Design (Pseudocode)

## A3.1 Identify the repeated pattern
```text
ALGORITHM COMMON_DATABASE_OPERATION
BEGIN
    OBTAIN database connection
    PREPARE SQL operation
    EXECUTE SQL operation
    IF operation returns rows THEN
        WHILE a row is available
            MAP column values into an entity object
        END WHILE
    END IF
    CLOSE result set, statement and connection
END
```
The entity type, table name, primary key, SQL columns and object mapping differ among StudentDB, CourseDB and MarksDB. Connection acquisition, statement execution, row iteration and resource cleanup are repeated.

## A3.2 Generic DAO interface
```text
INTERFACE Dao<T, K>
    PROCEDURE insert(entity: T)
    FUNCTION findById(key: K) RETURNS T or NOT_FOUND
    FUNCTION findAll() RETURNS LIST<T>
    FUNCTION update(entity: T) RETURNS BOOLEAN
    FUNCTION delete(key: K) RETURNS BOOLEAN
END INTERFACE

ALGORITHM StudentDao_Insert(student)
BEGIN
    PREPARE parameterized INSERT for student table
    BIND student.id, student.name, student.email, student.department
    EXECUTE update
    CLOSE statement
END

ALGORITHM StudentDao_FindById(id)
BEGIN
    PREPARE SELECT from student WHERE student_id = ?
    BIND id
    EXECUTE query
    IF a row exists THEN
        CREATE Student from student_id, student_name, email, department
        RETURN Student
    ELSE
        RETURN NOT_FOUND
    END IF
    CLOSE result set and statement
END
```
The same interface operations are implemented for `findAll`, `update` and `delete`; StudentDao supplies the student-specific SQL and mapping.

## A3.3 Student-to-table mapping
| Student object field | student table column | Role |
|---|---|---|
| id | student_id | Primary key |
| name | student_name | Student name |
| email | email | Email address |
| department | department | Department |

## Alternative: SQL inside service/GUI classes
Without a DAO, SQL and mapping logic are repeated in each service or GUI class. A column rename then requires locating and changing every SQL statement. With a DAO, the change is concentrated in the relevant DAO implementation. The database operation has the same asymptotic time and result-space cost in either design; the DAO adds only a small constant method-call abstraction overhead.
