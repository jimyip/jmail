package wiki.sogou.jmail;

import org.junit.Test;
import wiki.sogou.jmail.util.MimeUtils;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

public class MimeMessageBuilderTest {
    @Test
    public void test() throws IOException, MessagingException {
        File file = new File("C:\\Users\\JimYip\\Desktop\\微信图片_20200314173215.jpg");
        FileInputStream fis = new FileInputStream(file);
        FileInputStream fis2 = new FileInputStream(file);
        String id = MimeUtils.generateContentId("admin@4chan.tv", "aa.pdf");
        MimeMessage message = JMail.builder()
                .host("192.168.1.128")
                .port(25)
                .from("admin@wiki.sogou")
                .to("test@4chan.tv")
                .cc("test@4chan.tv")
                .bcc("叶沐 <test@4chan.tv>")
                .subject("hello")
                .plainText("plainText中文")
                .text("plainText中文")
                .htmlText("<span class=\"spnEditorSign\">textHtml中文<img src=\"cid:" + id + "\" alt=\"\"><span style=\"font-family:SimSun;\"></span></span>")
                .html("<span class=\"spnEditorSign\">textHtml中文<img src=\"cid:" + id + "\" alt=\"\"><span style=\"font-family:SimSun;\"></span></span>")
                .addAttachment("微信图片_20200314173215.jpg", fis2)
                .addAttachment(new FileDataSource(file))
                .addInline(id, file.getName(), fis)
                .recipientFilter(addresses -> addresses.stream()
                        .filter(address -> !((InternetAddress) address).getAddress().contains("admin"))
                        .collect(Collectors.toSet()))
//                .checkRecipients(true)
                .build();

        Transport transport = Session.getDefaultInstance(null).getTransport();
        transport.sendMessage(message, null);

        Transport.send(message, "admin@wiki.sogou", "aabb123456");

    }
}
