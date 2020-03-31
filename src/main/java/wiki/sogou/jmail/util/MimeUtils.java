package wiki.sogou.jmail.util;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;


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


    public static void replaceVar(MimePart part, Map varMap) throws MessagingException, IOException {
        String contentType = part.getContentType();
        if (contentType.startsWith("text")) {
            Object content = part.getContent();
            if (content instanceof String) {
                String text = (String) content;
                String text1 = StringUtils.replaceVars(text, varMap);
                // noinspection StringEquality
                if (text != text1) {
                    part.setContent(text1, contentType);
                }
            }
        } else if (contentType.startsWith("multipart")) {
            Object content = part.getContent();
            if (content instanceof MimeMultipart) {
                MimeMultipart multipart = (MimeMultipart) content;
                int count = multipart.getCount();
                for (int i = 0; i < count; i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                    replaceVar(bodyPart, varMap);
                }
            }
        }
    }


    public static void replaceVar(MimeMessage message, Map varMap) throws MessagingException, IOException {
        String subject = message.getHeader("Subject", null);
        if (subject != null) {
            if (!subject.contains("?=")) {
                subject = new String(StringUtils.iso2Bytes(subject), "GBK");
            } else {
                subject = message.getSubject();
            }
            String subject1 = StringUtils.replaceVars(subject, varMap);
            // noinspection StringEquality
            if (subject != subject1) {
                message.setSubject(subject1, "GBK");
            }
        }

        replaceVar((MimePart) message, varMap);
    }

    public static int getBase64EncodeSize(int actualSize) {
        final long charactersPerLine = 76;
        final long bytesPerLine = 76 + 2;
        long base64Size = (actualSize + 2L) / 3 * 4;
        long fullLines = base64Size / charactersPerLine;
        long remaining = base64Size - (fullLines * charactersPerLine);
        long result = (fullLines * bytesPerLine) + remaining;
        return (result >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) result;
    }

    public static int getBase64DecodeSize(long bodySize) {
        final long charactersPerLine = 76;
        final long bytesPerLine = 76 + 2;
        long fullLines = bodySize / bytesPerLine;
        long remaining = bodySize - (fullLines * bytesPerLine);
        long base64Size = (fullLines * charactersPerLine) + remaining;
        long result = base64Size / 4 * 3;
        return (result >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) result;
    }


    public static void setHeaderFold(Part hdr, String name, String val) throws MessagingException {
        setHeaderFold(hdr, name, val, false);
    }

    public static void addHeaderFold(Part hdr, String name, String val) throws MessagingException {
        addHeaderFold(hdr, name, val, false);
    }

    public static void setHeaderFold(InternetHeaders hdr, String name, String val) {
        setHeaderFold(hdr, name, val, false);
    }

    public static void addHeaderFold(InternetHeaders hdr, String name, String val) {
        addHeaderFold(hdr, name, val, false);
    }

    public static void setHeaderFold(Part hdr, String name, String val, boolean breakWord) throws MessagingException {
        hdr.setHeader(name, fold(name.length() + 2, val, breakWord));
    }

    public static void addHeaderFold(Part hdr, String name, String val, boolean breakWord) throws MessagingException {
        hdr.addHeader(name, fold(name.length() + 2, val, breakWord));
    }

    public static void setHeaderFold(InternetHeaders hdr, String name, String val, boolean breakWord) {
        hdr.setHeader(name, fold(name.length() + 2, val, breakWord));
    }

    public static void addHeaderFold(InternetHeaders hdr, String name, String val, boolean breakWord) {
        hdr.addHeader(name, fold(name.length() + 2, val, breakWord));
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

            sb.append(s.substring(0, lastspace));
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

    private static String replaceVars(String s, Map varMap) {
        int i0 = 0;
        int i1 = s.indexOf('$');
        int i2;

        if (i1 < 0) {
            return s;
        }

        StringBuilder sb = new StringBuilder();
        while ((i2 = s.indexOf('$', i1 + 1)) > 0) {
            String var = s.substring(i1 + 1, i2).toLowerCase(Locale.US);
            Object val = varMap.get(var);
            if (val == null) {
                if (!varMap.containsKey(var)) {
                    i1 = i2;
                    continue;
                }
                val = "";
            }
            sb.append(s, i0, i1);
            sb.append(val);
            i0 = i2 + 1;
            i1 = s.indexOf('$', i0);
            if (i1 < 0) {
                break;
            }
        }

        if (i0 == 0) {
            return s;
        }

        return sb.append(s.substring(i0)).toString();
    }
}
