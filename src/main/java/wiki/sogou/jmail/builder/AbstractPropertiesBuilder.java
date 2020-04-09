package wiki.sogou.jmail.builder;

import java.util.Properties;

/**
 * @author JimYip
 * TODO 部分代码需要调整
 */
public abstract class AbstractPropertiesBuilder {

    Properties properties;
    private Properties otherProperties;

    public AbstractPropertiesBuilder() {
        this.properties = new Properties();
    }


    public AbstractPropertiesBuilder mailDebug(boolean debug) {
        this.properties.put("mail.debug", debug);
        return this;
    }

    public AbstractPropertiesBuilder mailFrom(String from) {
        this.properties.put("mail.from", from);
        return this;
    }


    public AbstractPropertiesBuilder mimeAddressStrict(boolean mimeAddressStrict) {
        this.properties.put("mail.mime.address.strict", mimeAddressStrict);
        return this;
    }


    public AbstractPropertiesBuilder mailHost(String host) {
        this.properties.put("mail.host", host);
        return this;
    }


    public AbstractPropertiesBuilder storeProtocol(String storeProtocol) {
        this.properties.put("mail.store.protocol", storeProtocol);
        return this;
    }


    void setProtocol(String protocol) {
        this.properties.put("mail.transport.protocol", protocol);
    }


    public AbstractPropertiesBuilder mailUser(String user) {
        this.properties.put("mail.user", user);
        return this;
    }

    public AbstractPropertiesBuilder put(Object key, Object value) {
        if (this.otherProperties == null) {
            this.otherProperties = new Properties();
        }
        this.otherProperties.put(key, value);
        return this;
    }


    public Properties build() {
        if (this.otherProperties != null) {
            this.properties.putAll(this.otherProperties);
        }
        return new UnmodifiableProperties(this.properties);
    }
}
