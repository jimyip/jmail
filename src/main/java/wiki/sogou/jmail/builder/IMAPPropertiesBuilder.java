package wiki.sogou.jmail.builder;

/**
 * @author JimYip
 * TODO 部分参数需要涵盖
 */
public class IMAPPropertiesBuilder extends AbstractPropertiesBuilder {

    public IMAPPropertiesBuilder user(String user) {
        this.properties.put("mail.imap.user", user);
        return this;
    }

    public IMAPPropertiesBuilder clazz(String clazz) {
        this.properties.put("mail.imap.class", clazz);
        return this;
    }

    public IMAPPropertiesBuilder host(String host) {
        this.properties.put("mail.imap.host", host);
        return this;
    }

    public IMAPPropertiesBuilder port(int port) {
        this.properties.put("mail.imap.port", port);
        return this;
    }

    public IMAPPropertiesBuilder partialFetch(boolean partialFetch) {
        this.properties.put("mail.imap.partialfetch", partialFetch);
        return this;
    }

    public IMAPPropertiesBuilder fetchSize(int fetchSize) {
        this.properties.put("mail.imap.fetchsize", fetchSize);
        return this;
    }

    public IMAPPropertiesBuilder connectionTimeout(int connectionTimeout) {
        this.properties.put("mail.imap.connectiontimeout", connectionTimeout);
        return this;
    }

    public IMAPPropertiesBuilder timeout(int timeout) {
        this.properties.put("mail.imap.timeout", timeout);
        return this;
    }

    public IMAPPropertiesBuilder statusCacheTimeout(int statusCacheTimeout) {
        this.properties.put("mail.imap.statuscachetimeout", statusCacheTimeout);
        return this;
    }

    public IMAPPropertiesBuilder appendBufferSize(int appendBufferSize) {
        this.properties.put("mail.imap.appendbuffersize", appendBufferSize);
        return this;
    }

    public IMAPPropertiesBuilder connectionPoolSize(int connectionPoolSize) {
        this.properties.put("mail.imap.connectionpoolsize", connectionPoolSize);
        return this;
    }

    public IMAPPropertiesBuilder connectionpooltimeout(int connectionTimeout) {
        this.properties.put("mail.imap.connectionpooltimeout", connectionTimeout);
        return this;
    }

    public IMAPPropertiesBuilder separateStoreConnection(boolean separateStoreConnection) {
        this.properties.put("mail.imap.separatestoreconnection", separateStoreConnection);
        return this;
    }

    public IMAPPropertiesBuilder connectionPoolDebug(boolean connectionPoolDebug) {
        this.properties.put("mail.imap.connectionpool.debug", connectionPoolDebug);
        return this;
    }

}
