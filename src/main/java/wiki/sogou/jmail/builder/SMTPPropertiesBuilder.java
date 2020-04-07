package wiki.sogou.jmail.builder;

import java.util.Properties;

/**
 * @author JimYip
 */
public class SMTPPropertiesBuilder {
    private String user;
    private String host;
    private int port;
    private int connectionTimeout;
    private int timeout;
    private String from;
    private String localhost;
    private boolean ehlo;
    private boolean auth;
    private String dsnNotify;
    private String dsnRet;
    private boolean allow8bitMime;
    private boolean sendPartial;
    private Properties otherProperties;

    public SMTPPropertiesBuilder user(String user) {
        this.user = user;
        return this;
    }

    public SMTPPropertiesBuilder host(String host) {
        this.host = host;
        return this;
    }

    public SMTPPropertiesBuilder port(int port) {
        this.port = port;
        return this;
    }

    public SMTPPropertiesBuilder connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public SMTPPropertiesBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public SMTPPropertiesBuilder from(String from) {
        this.from = from;
        return this;
    }

    public SMTPPropertiesBuilder localhost(String localhost) {
        this.localhost = localhost;
        return this;
    }

    public SMTPPropertiesBuilder ehlo(boolean ehlo) {
        this.ehlo = ehlo;
        return this;
    }

    public SMTPPropertiesBuilder auth(boolean auth) {
        this.auth = auth;
        return this;
    }

    public SMTPPropertiesBuilder dsnNotify(String dsnNotify) {
        this.dsnNotify = dsnNotify;
        return this;
    }

    public SMTPPropertiesBuilder dsnRet(String dsnRet) {
        this.dsnRet = dsnRet;
        return this;
    }

    public SMTPPropertiesBuilder allow8bitMime(boolean allow8bitMime) {
        this.allow8bitMime = allow8bitMime;
        return this;
    }

    public SMTPPropertiesBuilder sendPartial(boolean sendPartial) {
        this.sendPartial = sendPartial;
        return this;
    }

    public SMTPPropertiesBuilder put(Object key, Object value) {
        if (this.otherProperties == null) {
            this.otherProperties = new Properties();
        }
        this.otherProperties.put(key, value);
        return this;
    }

    public Properties build() {
        Properties properties = new Properties();
        properties.put("mail.smtp.user", user);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.connectiontimeout", connectionTimeout);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.from", from);
        properties.put("mail.smtp.localhost", localhost);
        properties.put("mail.smtp.ehlo", ehlo);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.dsn.notify", dsnNotify);
        properties.put("mail.smtp.dsn.ret", dsnRet);
        properties.put("mail.smtp.allow8bitmime", allow8bitMime);
        properties.put("mail.smtp.sendpartial", sendPartial);
        properties.putAll(otherProperties);
        return new UnmodifiableProperties(properties);
    }
}
