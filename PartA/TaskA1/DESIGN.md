# A1.1 – Record representation

## (a) Delimited text format
One record per line, UTF-8 encoded. Field order:
`passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid`

Example:
`P1001,192311001,Arun,12,Central,2027-03-31,4500.00`

The delimiter is a comma. A robust CSV writer must quote a field containing a comma or quote and double embedded quotes. For example, `Anna Nagar, West` is stored as `"Anna Nagar, West"`. A simple `String.split(",")` is not a complete CSV parser; the implementation in B1 uses a deliberately simple five-field format because the supplied Complete-the-Code skeleton defines that format. A production six/seven-field implementation should use a CSV parser or a documented escaping format.

## (b) Binary format using DataOutputStream
Use a sequence of records. Each record is written in the following order:
1. passId – `writeUTF` (length-prefixed modified UTF-8)
2. regNo – `writeUTF`
3. name – `writeUTF`
4. routeNo – `writeInt`
5. boardingStop – `writeUTF`
6. validityDate – `writeUTF` using ISO-8601 `yyyy-MM-dd`
7. amountPaid – `writeDouble`

This format is variable-length because the strings are length-prefixed. The reader must read fields in exactly the same order and handle EOF only at a record boundary. A record count or file header with a format version is useful for detecting incomplete trailing records.

# A1.2 – Text-file operations and stream choices

## ADD PASS
```text
ALGORITHM AddPass(pass)
INPUT: one validated pass record
OUTPUT: record appended to the pass file

BEGIN
    OPEN file using FileWriter in append mode
    WRAP FileWriter with BufferedWriter
    ENCODE every field using the documented delimiter/escaping rule
    WRITE the encoded record
    WRITE a newline
    FLUSH the writer
    CLOSE writer using try-with-resources
END
```
`FileWriter` performs character output; `BufferedWriter` reduces small write calls by collecting characters before writing.

## FIND PASS BY ID
```text
ALGORITHM FindPass(passId)
INPUT: passId
OUTPUT: matching record or NOT FOUND

BEGIN
    OPEN file using FileReader
    WRAP FileReader with BufferedReader
    WHILE a line can be read
        PARSE line using the documented CSV/escaping rule
        IF line is malformed THEN
            RECORD damaged-line diagnostic
            CONTINUE
        END IF
        IF parsed pass ID equals passId THEN
            RETURN parsed record
        END IF
    END WHILE
    RETURN NOT FOUND
    CLOSE reader using try-with-resources
END
```
`FileReader` reads characters; `BufferedReader` efficiently reads complete lines and reduces underlying read operations.

## LIST PASSES ON A ROUTE
```text
ALGORITHM ListByRoute(routeNo)
INPUT: route number
OUTPUT: every valid record whose route number matches

BEGIN
    OPEN file using FileReader
    WRAP FileReader with BufferedReader
    WHILE a line can be read
        PARSE and validate the record
        IF malformed THEN
            RECORD diagnostic
            CONTINUE
        END IF
        IF record.routeNo equals routeNo THEN
            DISPLAY record
        END IF
    END WHILE
    CLOSE reader using try-with-resources
END
```

`FileInputStream`/`FileOutputStream` are byte streams and are appropriate for raw binary records or byte-oriented files. They are not the preferred choice for a delimited text file when character decoding and line-oriented processing are required.

# A1.3 – Damaged-data design
- **Damaged line:** validate field count, required fields, numeric route/amount and date format. Skip the malformed record, log line number and reason, and continue so one bad record does not stop the full search.
- **Missing file:** distinguish `NoSuchFileException`/`FileNotFoundException` from other I/O failures. For a first-time store, create the file when adding; for a read request, report “file not found” rather than silently treating it as an empty file.
- **Incomplete write / power failure:** write a complete record to a temporary file and atomically replace the original when rewriting; for append-only files, flush after a complete record, include record length/checksum or a completion marker, and on startup detect/truncate an incomplete trailing record. Keep a backup where required.

# Alternative – RandomAccessFile with fixed-length records
Assign every field a fixed byte width (for example, pass ID 12 bytes, register number 12, name 40, route 4, boarding stop 30, date 10, amount 8). Encode strings in a fixed charset, pad unused bytes and reject/truncate overlong values according to a documented rule. Record size is the sum of the field widths.

For record index i, seek to `headerSize + i × recordSize` and read one record. This gives O(1) access when the index is already known. Finding by pass ID without a separate index still requires scanning n records: O(n). A HashMap index from pass ID to record offset can make lookup average O(1) after O(n) index construction.

Fixed-width storage wastes space when values are shorter than their allocated fields. Padding waste per record is the sum of unused string-field bytes; total waste grows linearly with record count.
