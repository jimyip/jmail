package wiki.sogou.jmail.builder;

/**
 * @author JimYip
 * https://eclipse-ee4j.github.io/mail/docs/api/
 */
public class PropertiesBuilders {
    private PropertiesBuilders() {
    }

    public static POP3PropertiesBuilder POP3() {
        POP3PropertiesBuilder pop3PropertiesBuilder = new POP3PropertiesBuilder();
        pop3PropertiesBuilder.setProtocol("pop3");
        return pop3PropertiesBuilder;
    }

    public static IMAPPropertiesBuilder IMAP() {
        IMAPPropertiesBuilder imapPropertiesBuilder = new IMAPPropertiesBuilder();
        imapPropertiesBuilder.setProtocol("imap");
        return imapPropertiesBuilder;
    }

    public static SMTPPropertiesBuilder SMTP() {
        SMTPPropertiesBuilder smtpPropertiesBuilder = new SMTPPropertiesBuilder();
        smtpPropertiesBuilder.setProtocol("smtp");
        return smtpPropertiesBuilder;
    }

}
