package wiki.sogou.jmail.parser;

import org.junit.Test;
import wiki.sogou.jmail.JMail;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author JimYip
 */
public class MimeMessageParserTest {
    @Test
    public void test() throws Exception {
        MimeMessage message = JMail.builder()
                .sender("admin@4chan.tv")
                .from("admin@4chan.tv")
                .to("admin@4chan.tv")
                .subject("hello0409")
                .html("plainText中文")
                .addAttachment(new File("C:\\Users\\JimYip\\Desktop\\微信图片_20200314175443.jpg"))
                .build();
        MimeMessageParser parser = MimeMessageParser.of(message).parse();
        Optional<DataSource> attachment = parser.findAttachmentByName("微信图片_20200314175443.jpg");

        System.out.println(attachment);
    }

}