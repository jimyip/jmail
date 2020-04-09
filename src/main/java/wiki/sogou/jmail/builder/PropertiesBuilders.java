package wiki.sogou.jmail.builder;

/**
 * @author JimYip
 * https://eclipse-ee4j.github.io/mail/docs/api/
 */
public class PropertiesBuilders {
    private PropertiesBuilders() {
    }

    public static POP3PropertiesBuilder POP3() {
        return new POP3PropertiesBuilder();
    }

    public static IMAPPropertiesBuilder IMAP() {
        return new IMAPPropertiesBuilder();
    }

    public static SMTPPropertiesBuilder SMTP() {
        return new SMTPPropertiesBuilder();
    }

}
