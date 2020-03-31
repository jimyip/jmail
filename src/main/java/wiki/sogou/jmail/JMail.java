package wiki.sogou.jmail;

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
