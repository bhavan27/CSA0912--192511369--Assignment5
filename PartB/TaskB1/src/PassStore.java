import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PassStore {
    private final Path file;
    public PassStore(Path file) { this.file = file; }

    public void add(String passId, String regNo, String name, int route, double amount)
            throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(String.join(",", passId, regNo, name,
                    String.valueOf(route), String.valueOf(amount)));
            w.newLine();
        }
    }

    /** Returns five fields for a valid matching pass, or null if not found. */
    public String[] find(String passId) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length != 5) continue; // damaged field count
                try {
                    if (fields[0].isBlank() || fields[1].isBlank() || fields[2].isBlank()) continue;
                    Integer.parseInt(fields[3]);
                    Double.parseDouble(fields[4]);
                } catch (NumberFormatException ex) {
                    continue; // damaged numeric data
                }
                if (fields[0].equals(passId)) return fields;
            }
        }
        return null;
    }

    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = line.split(",", -1);
                if (f.length != 5) continue;
                try {
                    if (Integer.parseInt(f[3]) == route) {
                        Double.parseDouble(f[4]);
                        result.add(f);
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return result;
    }
}
