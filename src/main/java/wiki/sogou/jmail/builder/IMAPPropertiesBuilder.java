package wiki.sogou.jmail.builder;

import wiki.sogou.jmail.IMAP;

import java.util.Properties;

/**
 * @author JimYip
 */
public class IMAPPropertiesBuilder {
    private String user;
    private String host;
    private int port;
    private boolean partialFetch;
    private int fetchSize;
    private int connectionTimeout;
    private int timeout;
    private int statusCacheTimeout;
    private int appendBufferSize;
    private int connectionPoolSize;
    private boolean separateStoreConnection;
    private boolean connectionPoolDebug;
    private Properties otherProperties;

    public IMAPPropertiesBuilder user(String user) {
        this.user = user;
        return this;
    }

    public IMAPPropertiesBuilder host(String host) {
        this.host = host;
        return this;
    }

    public IMAPPropertiesBuilder port(int port) {
        this.port = port;
        return this;
    }

    public IMAPPropertiesBuilder partialFetch(boolean partialFetch) {
        this.partialFetch = partialFetch;
        return this;
    }

    public IMAPPropertiesBuilder fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    public IMAPPropertiesBuilder connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public IMAPPropertiesBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public IMAPPropertiesBuilder statusCacheTimeout(int statusCacheTimeout) {
        this.statusCacheTimeout = statusCacheTimeout;
        return this;
    }

    public IMAPPropertiesBuilder appendBufferSize(int appendBufferSize) {
        this.appendBufferSize = appendBufferSize;
        return this;
    }

    public IMAPPropertiesBuilder connectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
        return this;
    }

    public IMAPPropertiesBuilder separateStoreConnection(boolean separateStoreConnection) {
        this.separateStoreConnection = separateStoreConnection;
        return this;
    }

    public IMAPPropertiesBuilder connectionPoolDebug(boolean connectionPoolDebug) {
        this.connectionPoolDebug = connectionPoolDebug;
        return this;
    }

    public IMAPPropertiesBuilder put(Object key, Object value) {
        if (this.otherProperties == null) {
            this.otherProperties = new Properties();
        }
        this.otherProperties.put(key, value);
        return this;
    }

    public Properties build() {
        Properties properties = new Properties();
        properties.put("mail.imap.user", user);
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", port);
        properties.put("mail.imap.partialfetch", partialFetch);
        properties.put("mail.imap.fetchsize", fetchSize);
        properties.put("mail.imap.connectiontimeout", connectionTimeout);
        properties.put("mail.imap.timeout", timeout);
        properties.put("mail.imap.statuscachetimeout", statusCacheTimeout);
        properties.put("mail.imap.appendbuffersize", appendBufferSize);
        properties.put("mail.imap.connectionpoolsize", connectionPoolSize);
        properties.put("mail.imap.connectionpooltimeout", connectionTimeout);
        properties.put("mail.imap.separatestoreconnection", separateStoreConnection);
        properties.put("mail.imap.connectionpool.debug", connectionPoolDebug);
        properties.putAll(otherProperties);
        return new UnmodifiableProperties(properties);
    }
}
