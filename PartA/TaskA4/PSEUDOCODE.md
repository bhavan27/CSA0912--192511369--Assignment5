# Task A4 – Serialization Design (Pseudocode)

## A4.1 Decide what to persist
```text
SERIALIZABLE CLASS Hostel
    hostelName
    listOfRooms
END CLASS

SERIALIZABLE CLASS Room
    roomNumber
    listOfResidents
END CLASS

SERIALIZABLE CLASS Resident
    name
    registerNumber
    phoneNumber
    TRANSIENT password
    TRANSIENT liveDatabaseConnection
END CLASS

ALGORITHM SaveHostel(hostel, file)
BEGIN
    OPEN ObjectOutputStream for file
    SERIALIZE hostel
        SERIALIZATION TRAVERSES each reachable Room
        SERIALIZATION TRAVERSES each reachable Resident
        SKIP transient fields
    CLOSE ObjectOutputStream
END

ALGORITHM RestoreHostel(file)
BEGIN
    OPEN ObjectInputStream for file
    READ and reconstruct Hostel object graph
    RETURN reconstructed Hostel
    CLOSE ObjectInputStream
END
```
The live JDBC Connection is transient because it is a runtime resource and cannot be restored as a valid live connection. The password is transient to avoid persisting a plaintext credential. In a real system, authentication data should be represented by a protected salted password hash in a credential store.

## A4.2 Plan for class change
When `phoneNumber` is added, an older serialized stream does not contain that field. If the class keeps the same explicit `serialVersionUID`, deserialization can proceed and the new String field receives its default value (`null`) unless migration logic supplies a value. If the serialVersionUID differs, deserialization fails with an incompatible-class version error.

## Alternative: CSV or JSON
CSV/JSON are text formats and can be inspected and exchanged more easily. Adding a field can be handled by a versioned parser or an optional column/property. Java serialization naturally follows object references but is Java-specific and sensitive to class versioning. Save and load are O(n) for n residents in either approach. File size depends on field values and format metadata; Java serialization includes class/graph metadata, CSV is compact for simple flat records, and JSON is self-describing but often more verbose.
