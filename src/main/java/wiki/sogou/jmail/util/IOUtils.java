package wiki.sogou.jmail.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    public static void readFully(InputStream in, byte[] b) throws IOException {
        readFully(in, b, 0, b.length);
    }

    public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int count = in.read(b, off, len);
            if (count < 0) {
                throw new EOFException();
            }
            off += count;
            len -= count;
        }
    }
}
