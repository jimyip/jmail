package wiki.sogou.jmail;

import org.junit.Test;

import javax.mail.internet.MimeMessage;

public class MimeMessageBuilderTest {
    @Test
    public void test() {
        MimeMessage message = JMail.builder().from("test@4chan.tv").build();
        System.out.println(message);
    }
}
