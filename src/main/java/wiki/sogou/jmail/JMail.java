package wiki.sogou.jmail;

import wiki.sogou.jmail.builder.MimeMessageBuilder;

/**
 * @author JimYip
 */
public class JMail {


    public static MimeMessageBuilder builder() {
        return new MimeMessageBuilder();
    }

    @Deprecated
    public static MimeMessageBuilder newBuilder() {
        return builder();
    }


}
