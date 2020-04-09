package wiki.sogou.jmail.builder;

import org.junit.Test;
import wiki.sogou.jmail.DefaultAuthenticator;
import wiki.sogou.jmail.JMail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
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
        Session session = Session.getDefaultInstance(properties, DefaultAuthenticator.of("aabb123456"));


        MimeMessage message = JMail.builder()
                .session(session)
                .sender("admin@4chan.tv")
                .from("admin@4chan.tv")
                .to("admin@4chan.tv")
                .cc("admin@4chan.tv")
                .subject("hello1")
                .text("plainText中文")
                .build();

        MimeMessage message2 = JMail.builder()
                .session(session)
                .sender("admin@4chan.tv")
                .from("admin@4chan.tv")
                .to("admin@4chan.tv")
                .cc("admin@4chan.tv")
                .subject("hello2")
                .text("plainText中文")
                .build();

        if (message.getSession() == message2.getSession()) {
            System.out.println(true);
        }

        Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());

        transport.sendMessage(message2, message.getAllRecipients());

//        Transport.send(message, "admin@4chan.tv", "aabb123456");

    }
}
