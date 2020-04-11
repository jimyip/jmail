package wiki.sogou.jmail.builder;

import org.junit.Test;
import wiki.sogou.jmail.DefaultAuthenticator;
import wiki.sogou.jmail.JMail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

public class BuildersTest {
    @Test
    public void test() throws MessagingException {
        Properties properties = PropertiesBuilders.SMTP()
                .auth(true)
                .host("192.168.126.128")
                .user("admin@4chan.tv")
                .port(25)
                .build();
        System.out.println(properties);
        Session session = Session.getDefaultInstance(properties, DefaultAuthenticator.of("aabb123456"));

//        Transport transport = session.getTransport();
//        transport.connect();
//
        MimeMessage message = JMail.builder()
//                .session(session)
                .sender("admin@4chan.tv")
                .from("admin@4chan.tv")
                .to("admin@4chan.tv")
                .subject("hello0409")
                .html("plainText中文")
                .build();
        System.out.println(message);

//        transport.sendMessage(message, message.getAllRecipients());


//        Transport.send(message, "admin@4chan.tv", "aabb123456");

    }
}
