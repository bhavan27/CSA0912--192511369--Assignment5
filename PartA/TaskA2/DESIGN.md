# A2.1 – Predict the cost
A 50 MiB file contains 52,428,800 bytes. A loop that calls `FileInputStream.read()` once per byte makes approximately 52,428,800 Java-level read calls (plus the final EOF call). Each call has method/native-call overhead and may invoke lower-level read operations. Repeated tiny reads are therefore much slower than reading blocks.

# A2.2 – Buffered redesign
Use `BufferedInputStream` or a byte array (for example, 8 KiB or 64 KiB) and count newline bytes in each block.

```text
ALGORITHM CountLinesBuffered(path)
BEGIN
    CREATE byte array buffer of 8192 bytes
    OPEN FileInputStream and wrap it with BufferedInputStream
    lineCount ← 0
    REPEAT
        bytesRead ← READ a block into buffer
        IF bytesRead equals EOF THEN EXIT LOOP
        FOR each byte in the returned block
            IF byte equals newline THEN lineCount ← lineCount + 1
        END FOR
    UNTIL EOF
    CLOSE stream
    RETURN lineCount
END
```
For a 50 MiB file and an 8 KiB buffer, approximately 6,400 block reads cover the file rather than about 52.4 million one-byte reads. The exact lower-level I/O behaviour is implementation- and OS-dependent. The program also makes fewer Java-level read calls because it requests a block at a time.

# A2.3 – When buffering gives little benefit
Buffering may provide little additional benefit when the data is already delivered in large blocks by an upstream buffered source, when the file is very small, or when processing time rather than I/O is the dominant cost.

# Alternative – Files.readAllLines()
```text
lines ← Files.readAllLines(path, UTF_8)
lineCount ← lines.size()
```
Both approaches take O(file size) time to read the bytes/characters. Buffered streaming uses O(buffer size) extra memory and processes each line/byte incrementally. `Files.readAllLines()` stores all lines in memory, using O(file size) memory plus object and string overhead. It is convenient for small files that comfortably fit in memory; streaming is preferable for a 50 MB file or larger logs where memory should remain bounded.
