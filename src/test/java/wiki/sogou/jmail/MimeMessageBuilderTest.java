package wiki.sogou.jmail;

import org.junit.Test;
import wiki.sogou.jmail.builder.PropertiesBuilders;
import wiki.sogou.jmail.util.MimeUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MimeMessageBuilderTest {
//    String host = "127.0.0.1";
    String host = "192.168.1.128";
    int port = 25;

    @Test
    public void test() throws MessagingException {
        String id = MimeUtils.generateContentId("admin@4chan.tv", "aa.pdf");
        MimeMessage message = JMail.builder()
                .from("admin@wiki.sogou")
                .to("admin@wiki.sogou")
                .subject("hello")
                .text("plainText中文")
                .html("<span>textHtml中文</span>")
                .build();

        Properties props = PropertiesBuilders.SMTP()
                .host(host)
                .port(port)
                .build();

        Transport transport = Session.getDefaultInstance(props).getTransport();
        transport.connect("admin@wiki.sogou", "aabb123456");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
