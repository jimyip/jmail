package wiki.sogou.jmail.builder;

/**
 * @author JimYip
 * TODO 部分参数需要涵盖
 */
public class SMTPPropertiesBuilder extends AbstractPropertiesBuilder {

    public SMTPPropertiesBuilder clazz(String clazz) {
        this.properties.put("mail.smtp.class", clazz);
        return this;
    }

    public SMTPPropertiesBuilder user(String user) {
        this.properties.put("mail.smtp.user", user);
        return this;
    }

    public SMTPPropertiesBuilder host(String host) {
        this.properties.put("mail.smtp.host", host);
        return this;
    }

    public SMTPPropertiesBuilder port(int port) {
        this.properties.put("mail.smtp.port", port);
        return this;
    }

    public SMTPPropertiesBuilder connectionTimeout(int connectionTimeout) {
        this.properties.put("mail.smtp.connectiontimeout", connectionTimeout);
        return this;
    }

    public SMTPPropertiesBuilder timeout(int timeout) {
        this.properties.put("mail.smtp.timeout", timeout);
        return this;
    }

    public SMTPPropertiesBuilder from(String from) {
        this.properties.put("mail.smtp.from", from);
        return this;
    }

    public SMTPPropertiesBuilder localhost(String localhost) {
        this.properties.put("mail.smtp.localhost", localhost);
        return this;
    }

    public SMTPPropertiesBuilder ehlo(boolean ehlo) {
        this.properties.put("mail.smtp.ehlo", ehlo);
        return this;
    }

    public SMTPPropertiesBuilder auth(boolean auth) {
        this.properties.put("mail.smtp.auth", auth);
        return this;
    }

    public SMTPPropertiesBuilder dsnNotify(String dsnNotify) {
        this.properties.put("mail.smtp.dsn.notify", dsnNotify);
        return this;
    }

    public SMTPPropertiesBuilder dsnRet(String dsnRet) {
        this.properties.put("mail.smtp.dsn.ret", dsnRet);
        return this;
    }

    public SMTPPropertiesBuilder allow8bitMime(boolean allow8bitMime) {
        this.properties.put("mail.smtp.allow8bitmime", allow8bitMime);
        return this;
    }

    public SMTPPropertiesBuilder sendPartial(boolean sendPartial) {
        this.properties.put("mail.smtp.sendpartial", sendPartial);
        return this;
    }
}
