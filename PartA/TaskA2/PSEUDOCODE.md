# TASK A2 – DON'T READ WHAT YOU DON'T NEED

## A2.1 Predict the cost
For a 50 MB file (using 1 MB = 1,048,576 bytes):
```text
fileSize ← 50 × 1,048,576
          ← 52,428,800 bytes

Single-byte Java read calls ≈ 52,428,800
```
The loop makes approximately one Java `read()` call per byte, plus a final call to detect EOF. This is the Java-level method-call count. The operating system may combine/cache lower-level disk I/O, so it is not necessarily the exact number of system calls. Repeated method/native-call overhead makes this approach inefficient.

## A2.2 Buffered redesign – byte-array block reading
```text
ALGORITHM CountLinesBuffered(filePath)
INPUT: Path of attendance log
OUTPUT: Number of newline characters

BEGIN
    lineCount ← 0
    bufferSize ← 8192
    CREATE byte array buffer of bufferSize bytes
    OPEN FileInputStream for file
    WRAP stream with BufferedInputStream

    REPEAT
        bytesRead ← READ up to bufferSize bytes into buffer
        IF bytesRead equals END_OF_FILE THEN
            EXIT LOOP
        END IF

        FOR index ← 0 TO bytesRead - 1
            IF buffer[index] equals NEWLINE_BYTE THEN
                lineCount ← lineCount + 1
            END IF
        END FOR
    UNTIL END_OF_FILE

    CLOSE buffered stream
    RETURN lineCount
END
```
With an 8 KiB block, approximately 6,400 block reads cover 50 MiB, instead of approximately 52.4 million one-byte Java reads. A 64 KiB buffer would require approximately 800 block reads. Actual lower-level I/O calls depend on the Java runtime and operating system.

## A2.3 When buffering gives little benefit
Buffering adds little benefit when the input is already buffered upstream, the file is very small, or the bottleneck is CPU processing rather than I/O.

## Alternative concept: Files.readAllLines()
```text
ALGORITHM CountLinesAllAtOnce(filePath)
INPUT: Path of attendance log
OUTPUT: Number of lines

BEGIN
    lines ← Files.readAllLines(filePath, UTF_8)
    lineCount ← SIZE(lines)
    RETURN lineCount
END
```
Both streaming and whole-file reading require O(n) time for a file of n bytes/characters. Buffered streaming uses O(b) additional memory for buffer size b. `Files.readAllLines()` stores all lines and therefore uses O(n) memory plus String/List overhead. Whole-file reading is convenient for small files that comfortably fit in memory; buffered streaming is preferable for a 50 MB log or larger files where memory should remain bounded.
