package wiki.sogou.jmail.util;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import java.util.Base64;


/**
 * @author JimYip
 */
@SuppressWarnings({"rawtypes"})
public final class MimeUtils {

    private MimeUtils() {
    }

    public static String generateContentId(String from, String attachmentName) {
        // Unique string is <hashcode>$<attachmentName>$<currentTime>$<suffix>
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(sb.hashCode()));
        sb.append('$');
        sb.append(Integer.toHexString(attachmentName.hashCode()));
        sb.append('$');
        sb.append(Long.toHexString(System.currentTimeMillis()));
        sb.append('$');
        sb.append(new String(Base64.getUrlEncoder().encode(from.getBytes())));
        return sb.toString();
    }


    public static void setHeaderFold(Part hdr, String name, String val) throws MessagingException {
        setHeaderFold(hdr, name, val, false);
    }

    public static void setHeaderFold(Part hdr, String name, String val, boolean breakWord) throws MessagingException {
        hdr.setHeader(name, fold(name.length() + 2, val, breakWord));
    }

    public static void setSubject(MimeMessage msg, String subject, String charset) throws MessagingException {
        msg.setSubject((subject == null) ? null : subject.replaceAll("\\s*\n\\s*", " "), charset);
    }

    public static String fold(int used, String s, boolean breakWord) {

        int end;
        // Strip trailing spaces and newlines
        for (end = s.length() - 1; end >= 0; end--) {
            char c = s.charAt(end);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                break;
            }
        }
        s = s.substring(0, end + 1);

        // if the string fits now, just return it
        if (used + s.length() <= 76) {
            return s;
        }

        // have to actually fold the string
        StringBuilder sb = new StringBuilder(s.length() + 4);
        char lastc = 0;
        while (used + s.length() > 76) {
            int lastspace = -1;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\r' || c == '\n') {
                    used = -i;
                    lastspace = -1;
                    lastc = c;
                    continue;
                }

                if ((c == ' ' || c == '\t')
                        && !(lastc == ' ' || lastc == '\t' || lastc == '\r' || lastc == '\n')) {
                    lastspace = i;
                }

                if ((breakWord || lastspace != -1) && used + i >= 76) {
                    if (lastspace == -1) {
                        // break-word
                        lastspace = i;
                    }
                    break;
                }

                lastc = c;
            }

            if (lastspace == -1) {
                // no space, use the whole thing
                if (sb.length() == 0) {
                    return s;
                } else {
                    return sb.append(s).toString();
                }
            }

            sb.append(s, 0, lastspace);
            sb.append("\r\n");
            lastc = s.charAt(lastspace);
            if (!(lastc == ' ' || lastc == '\t')) {
                // break-word
                sb.append(' ');
                s = s.substring(lastspace);
            } else {
                sb.append(lastc);
                s = s.substring(lastspace + 1);
            }
            used = 1;
        }
        return sb.append(s).toString();
    }


}
