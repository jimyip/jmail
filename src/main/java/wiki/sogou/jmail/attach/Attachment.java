package wiki.sogou.jmail.attach;

import java.net.URL;

/**
 * @author JimYip
 * TODO 后需要需要添加到builder里
 */
public class Attachment {
    public static final String ATTACHMENT = javax.mail.Part.ATTACHMENT;

    public static final String INLINE = javax.mail.Part.INLINE;

    private String name = "";

    private String description = "";

    private String path = "";

    private URL url;

    private String disposition = Attachment.ATTACHMENT;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }
}
