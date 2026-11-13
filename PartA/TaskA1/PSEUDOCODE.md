# TASK A1 – DESIGN A FILE FORMAT

## A1.1 Record layouts

**Delimited text (UTF-8, one record per line):**
```text
passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid
```
A comma inside a value is handled using CSV quoting: enclose that field in double quotes and represent an embedded double quote as two double quotes. Example: `"Anna Nagar, West"`.

**Binary record (DataOutputStream), in order:**
```text
passId        : writeUTF (2-byte encoded-length prefix + modified UTF-8 bytes)
regNo         : writeUTF (2-byte encoded-length prefix + modified UTF-8 bytes)
name          : writeUTF (2-byte encoded-length prefix + modified UTF-8 bytes)
routeNo       : writeInt (4 bytes)
boardingStop  : writeUTF (2-byte encoded-length prefix + modified UTF-8 bytes)
validityDate  : writeUTF (ISO-8601 yyyy-MM-dd)
amountPaid    : writeDouble (8 bytes, IEEE 754)
```
The binary record is variable length because the UTF fields have length prefixes. The reader must use the same field order and data types.

## A1.2 Pseudocode – three text-file operations

### Algorithm 1: AddPass
```text
ALGORITHM AddPass(pass)
INPUT: One bus-pass record
OUTPUT: Record appended, or validation/write error

BEGIN
    VALIDATE passId, regNo, name and boardingStop are not empty
    VALIDATE routeNo is an integer and amountPaid is a valid non-negative number
    VALIDATE validityDate follows yyyy-MM-dd
    IF any validation fails THEN
        DISPLAY validation error
        RETURN FAILURE
    END IF

    OPEN FileWriter for pass file in APPEND mode
    WRAP FileWriter with BufferedWriter
    ENCODE all seven fields as one CSV record
        QUOTE fields containing comma, quote or line-break characters
        DOUBLE any quote character inside a quoted field
    WRITE encoded record
    WRITE newline
    FLUSH BufferedWriter
    CLOSE BufferedWriter (which also closes FileWriter)
    RETURN SUCCESS
END
```
`FileWriter` writes characters; `BufferedWriter` reduces repeated small writes. The chosen character encoding should be fixed as UTF-8 in the implementation (for example, with `Files.newBufferedWriter`).

### Algorithm 2: FindPassById
```text
ALGORITHM FindPassById(searchPassId)
INPUT: Pass ID to search
OUTPUT: Matching record, or NOT_FOUND

BEGIN
    OPEN FileReader for pass file
    WRAP FileReader with BufferedReader
    lineNumber ← 0

    WHILE another line is available
        line ← READ next line
        lineNumber ← lineNumber + 1
        fields ← PARSE one CSV record using quote-aware CSV rules

        IF parsing fails OR number of fields is not 7 THEN
            RECORD lineNumber and damage reason
            CONTINUE
        END IF

        IF routeNo, amountPaid or validityDate has invalid format THEN
            RECORD lineNumber and damage reason
            CONTINUE
        END IF

        IF fields.passId equals searchPassId THEN
            SAVE fields as result
            CLOSE reader
            RETURN result
        END IF
    END WHILE

    CLOSE reader
    RETURN NOT_FOUND
END
```
`FileReader`/`BufferedReader` are character-oriented and support line-by-line parsing without loading the whole file into memory. The reader must be closed on every exit path (for example, using try-with-resources).

### Algorithm 3: ListPassesByRoute
```text
ALGORITHM ListPassesByRoute(searchRoute)
INPUT: Route number
OUTPUT: All valid pass records for that route

BEGIN
    OPEN FileReader for pass file
    WRAP FileReader with BufferedReader
    found ← FALSE
    lineNumber ← 0

    WHILE another line is available
        line ← READ next line
        lineNumber ← lineNumber + 1
        fields ← PARSE one CSV record using quote-aware CSV rules

        IF parsing fails OR number of fields is not 7 THEN
            RECORD lineNumber and damage reason
            CONTINUE
        END IF

        IF any typed field is invalid THEN
            RECORD lineNumber and damage reason
            CONTINUE
        END IF

        IF fields.routeNo equals searchRoute THEN
            DISPLAY fields
            found ← TRUE
        END IF
    END WHILE

    CLOSE reader
    IF found equals FALSE THEN
        DISPLAY "No valid pass records found for this route"
    END IF
END
```

### Stream selection
- `FileReader` + `BufferedReader`: text-file input; reads decoded characters and complete lines.
- `FileWriter` + `BufferedWriter`: text-file output; writes characters and buffers small writes.
- `FileInputStream` + `DataInputStream`: binary input; reads the exact primitive/string field sequence.
- `FileOutputStream` + `DataOutputStream`: binary output; writes primitive values and length-prefixed UTF strings.
- `FileInputStream`/`FileOutputStream` are byte streams and are not the preferred direct choice for parsing text records because character decoding and CSV quoting still need to be handled.

## A1.3 Damaged-data handling
```text
ALGORITHM ValidateAndReadRecord(line, lineNumber)
BEGIN
    ATTEMPT quote-aware CSV parsing
    IF parsing reports malformed quoting THEN
        REPORT lineNumber and "Malformed CSV quoting"
        RETURN INVALID
    END IF
    IF field count is not 7 THEN
        REPORT lineNumber and "Incorrect field count"
        RETURN INVALID
    END IF
    IF a required field is empty OR a typed value is invalid THEN
        REPORT lineNumber and "Invalid field value"
        RETURN INVALID
    END IF
    RETURN VALID RECORD
END
```
A damaged line is logged and skipped so later records can still be processed. A missing file is reported distinctly (`NoSuchFileException`/`FileNotFoundException`); an add operation may create the file, but a read should not silently claim that a missing file is a valid empty store. For a possible power failure during append, flush after each complete record and use a record terminator/checksum or length marker so a truncated final record can be detected. For a full rewrite, write to a temporary file and atomically replace the old file. A backup or journal provides recovery.

## Alternative concept: RandomAccessFile
With fixed-length fields, define a fixed `recordSize` and pad unused bytes. The byte offset for record index `i` is:
```text
recordPosition ← headerSize + (i × recordSize)
```
`seek(recordPosition)` gives O(1) access when the record index is already known. Searching for a pass ID still takes O(n) without an index; a separate ID-to-offset index can provide average O(1) lookup after index construction. Fixed-width fields waste the unused padding space and require rules for overlong values.
