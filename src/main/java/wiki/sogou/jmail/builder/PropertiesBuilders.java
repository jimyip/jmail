package wiki.sogou.jmail.builder;

/**
 * @author JimYip
 * https://eclipse-ee4j.github.io/mail/docs/api/
 */
public class PropertiesBuilders {
    private PropertiesBuilders() {
    }

    public static POP3PropertiesBuilder POP3() {
        POP3PropertiesBuilder builder = new POP3PropertiesBuilder();
        builder.protocol("pop3").storeProtocol("pop3");
        return builder;
    }

    public static IMAPPropertiesBuilder IMAP() {
        IMAPPropertiesBuilder builder = new IMAPPropertiesBuilder();
        builder.protocol("imap").storeProtocol("imap");
        return builder;
    }

    public static SMTPPropertiesBuilder SMTP() {
        SMTPPropertiesBuilder builder = new SMTPPropertiesBuilder();
        builder.protocol("smtp").storeProtocol("smtp");
        return builder;
    }

}
