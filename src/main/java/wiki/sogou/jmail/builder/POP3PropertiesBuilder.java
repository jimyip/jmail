package wiki.sogou.jmail.builder;

import java.util.Properties;

/**
 * @author JimYip
 */
public class POP3PropertiesBuilder {
    private String user;
    private String host;
    private int port;
    private int connectionTimeout;
    private int timeout;
    private boolean rsetBeforeQuit;
    private int messageClass;
    private Properties otherProperties;

    public POP3PropertiesBuilder user(String user) {
        this.user = user;
        return this;
    }

    public POP3PropertiesBuilder host(String host) {
        this.host = host;
        return this;
    }

    public POP3PropertiesBuilder port(int port) {
        this.port = port;
        return this;
    }

    public POP3PropertiesBuilder connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public POP3PropertiesBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public POP3PropertiesBuilder rsetBeforeQuit(boolean rsetBeforeQuit) {
        this.rsetBeforeQuit = rsetBeforeQuit;
        return this;
    }

    public POP3PropertiesBuilder messageClass(int messageClass) {
        this.messageClass = messageClass;
        return this;
    }
    public POP3PropertiesBuilder put(Object key, Object value) {
        if (this.otherProperties == null) {
            this.otherProperties = new Properties();
        }
        this.otherProperties.put(key, value);
        return this;
    }

    public Properties build() {
        Properties properties = new Properties();
        properties.put("mail.pop3.user", user);
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);
        properties.put("mail.pop3.connectiontimeout", connectionTimeout);
        properties.put("mail.pop3.timeout", timeout);
        properties.put("mail.pop3.rsetbeforequit", rsetBeforeQuit);
        properties.put("mail.pop3.message.class", messageClass);
        properties.putAll(otherProperties);
        return new UnmodifiableProperties(properties);
    }
}
