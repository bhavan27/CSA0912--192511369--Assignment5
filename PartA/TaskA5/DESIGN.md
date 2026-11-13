# A5.1 – Wallet transfer transaction
```text
ALGORITHM TransferWallet(sourceId, destinationId, amount)
BEGIN
    OBTAIN JDBC connection
    SET autoCommit FALSE
    TRY
        UPDATE wallet SET balance = balance - amount
            WHERE wallet_id = sourceId AND balance >= amount
        IF exactly one source row was not updated THEN
            RAISE insufficient balance / missing wallet error
        END IF

        UPDATE wallet SET balance = balance + amount
            WHERE wallet_id = destinationId
        IF exactly one destination row was not updated THEN
            RAISE destination wallet error
        END IF

        COMMIT
    CATCH any failure
        ROLLBACK
        REPORT transfer failed
    FINALLY
        RESTORE original autoCommit setting
        CLOSE connection
    END TRY
END
```
Both updates form one atomic unit. If the program fails between them, rollback prevents only one side of the transfer from becoming permanent.

# A5.2 – SQL injection
Unsafe query:
`SELECT * FROM users WHERE id='` + id + `' AND pwd='` + pwd + `'`

A crafted password such as `' OR '1'='1' -- ` can alter the SQL predicate so the condition becomes true and the remainder is commented out, depending on the database's comment syntax and query construction. This can bypass the intended password check.

Safe design:
```java
String sql = "SELECT * FROM users WHERE id=? AND pwd_hash=?";
try (PreparedStatement ps = con.prepareStatement(sql)) {
    ps.setString(1, id);
    ps.setString(2, passwordHash);
    try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
    }
}
```
PreparedStatement binds values separately from SQL syntax. Passwords should be stored as salted password hashes, not plaintext.

# Alternative – two auto-commit updates
If each wallet update commits independently and the program fails after the debit but before the credit, the source wallet loses ₹500 while the destination receives nothing. If failure occurs before the debit, neither change happens. A transaction adds commit/rollback coordination and logging overhead, but ensures all-or-nothing correctness. Both approaches perform two update statements (asymptotically O(1) indexed row updates in the simplified model); a transaction adds constant protocol/locking/logging overhead. The correctness risk of independent auto-commit statements is unacceptable for a transfer.
