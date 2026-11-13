# Task A5 – Correctness and Safety Pseudocode

## A5.1 Atomic wallet transfer
```text
ALGORITHM TransferWallet(sourceId, destinationId, amount)
BEGIN
    OBTAIN JDBC connection
    SAVE original autoCommit value
    SET autoCommit FALSE
    TRY
        DEBIT amount from source wallet
        IF exactly one source row was not updated THEN
            RAISE transfer error
        END IF
        CREDIT amount to destination wallet
        IF exactly one destination row was not updated THEN
            RAISE transfer error
        END IF
        COMMIT transaction
    CATCH any failure
        ROLLBACK transaction
        REPORT transfer failure
    FINALLY
        RESTORE original autoCommit value
        CLOSE connection
    END TRY
END
```

## A5.2 Parameterized login check
```text
ALGORITHM VerifyLogin(id, password)
BEGIN
    PREPARE "SELECT user_id FROM users WHERE id = ? AND pwd_hash = ?"
    BIND id as parameter 1
    BIND securely computed password-verification value as parameter 2
    EXECUTE query
    IF a matching row exists THEN
        RETURN AUTHENTICATED
    ELSE
        RETURN REJECTED
    END IF
    CLOSE result set and statement
END
```
The input is bound as data and cannot change the SQL statement structure. The stored password should be a salted hash, not plaintext.
