import java.io.*;
import java.nio.file.*;

public class BufferedLineCounter {
    public static long countUnbuffered(Path file) throws IOException {
        long lines = 0;
        try (InputStream in = new FileInputStream(file.toFile())) {
            int b;
            while ((b = in.read()) != -1) if (b == '\n') lines++;
        }
        return lines;
    }

    public static long countBuffered(Path file) throws IOException {
        long lines = 0;
        byte[] buffer = new byte[64 * 1024];
        try (InputStream in = new BufferedInputStream(new FileInputStream(file.toFile()), buffer.length)) {
            int n;
            while ((n = in.read(buffer)) != -1) {
                for (int i = 0; i < n; i++) if (buffer[i] == '\n') lines++;
            }
        }
        return lines;
    }
}
