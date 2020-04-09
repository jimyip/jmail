package wiki.sogou.jmail.builder;

import java.util.Properties;

public abstract class AbstractPropertiesBuilder {

    Properties properties;
    private Properties otherProperties;

    public AbstractPropertiesBuilder() {
        this.properties = new Properties();
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
