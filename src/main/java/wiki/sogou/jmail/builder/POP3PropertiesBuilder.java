package wiki.sogou.jmail.builder;

/**
 * @author JimYip
 * TODO 部分参数需要涵盖
 */
public class POP3PropertiesBuilder extends AbstractPropertiesBuilder {

    public POP3PropertiesBuilder clazz(String clazz) {
        this.properties.put("mail.pop3.class", clazz);
        return this;
    }

    public POP3PropertiesBuilder user(String user) {
        this.properties.put("mail.pop3.user", user);
        return this;
    }

    public POP3PropertiesBuilder host(String host) {
        this.properties.put("mail.pop3.host", host);
        return this;
    }

    public POP3PropertiesBuilder port(int port) {
        this.properties.put("mail.pop3.port", port);
        return this;
    }

    public POP3PropertiesBuilder connectionTimeout(int connectionTimeout) {
        this.properties.put("mail.pop3.connectiontimeout", connectionTimeout);
        return this;
    }

    public POP3PropertiesBuilder timeout(int timeout) {
        this.properties.put("mail.pop3.timeout", timeout);
        return this;
    }

    public POP3PropertiesBuilder rsetBeforeQuit(boolean rsetBeforeQuit) {
        this.properties.put("mail.pop3.rsetbeforequit", rsetBeforeQuit);
        return this;
    }

    public POP3PropertiesBuilder messageClass(int messageClass) {
        this.properties.put("mail.pop3.message.class", messageClass);
        return this;
    }
}
