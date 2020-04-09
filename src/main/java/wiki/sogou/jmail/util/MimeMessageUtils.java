package wiki.sogou.jmail.util;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author JimYip
 */
public final class MimeMessageUtils {
    private MimeMessageUtils() {
        super();
    }

    public static MimeMessage createMimeMessage(final Session session, final byte[] source)
            throws MessagingException, IOException {
        try (ByteArrayInputStream is = new ByteArrayInputStream(source)) {
            return new MimeMessage(session, is);
        }
    }

    public static MimeMessage createMimeMessage(final Session session, final File source)
            throws MessagingException, IOException {
        try (FileInputStream is = new FileInputStream(source)) {
            return createMimeMessage(session, is);
        }
    }

    public static MimeMessage createMimeMessage(final Session session, final InputStream source)
            throws MessagingException {
        return new MimeMessage(session, source);
    }

    public static MimeMessage createMimeMessage(final Session session, final String source)
            throws MessagingException, IOException {
        final byte[] byteSource = source.getBytes();
        try (ByteArrayInputStream is = new ByteArrayInputStream(byteSource)) {
            return createMimeMessage(session, is);
        }
    }

    public static void writeMimeMessage(final MimeMessage mimeMessage, final File resultFile)
            throws MessagingException, IOException {
        FileOutputStream fos = null;
        try {
            if (!resultFile.getParentFile().exists() && !resultFile.getParentFile().mkdirs()) {
                throw new IOException("Failed to create the following parent directories: "
                        + resultFile.getParentFile());
            }
            fos = new FileOutputStream(resultFile);
            mimeMessage.writeTo(fos);
            fos.flush();
            fos.close();
            fos = null;
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
