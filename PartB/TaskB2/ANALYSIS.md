# B2 adversarial-input interpretation
The crafted value ` ' OR '1'='1' -- ` is appended into the unsafe SQL string as SQL syntax. It can make the predicate true and comment out the remaining password condition, depending on MySQL parsing. With PreparedStatement, the same characters are bound as a literal parameter and cannot change the SQL structure, so the unknown user/password pair is rejected.

The damaged CSV includes:
- A row with the wrong field count.
- A row with a non-numeric amount.
- An empty line.
- Valid rows surrounding the damaged data.

Quote escaping or regular-expression validation is weaker than parameter binding because escaping rules vary by database, character set and SQL context, and validation cannot reliably distinguish all data from SQL syntax. PreparedStatement separates query structure from values. Input validation remains useful for business rules but is not a substitute for parameterized SQL. Validation is O(L) for an input string of length L; PreparedStatement parameter binding is also O(L) for value handling, but it provides structural separation rather than relying on a fragile filter.
