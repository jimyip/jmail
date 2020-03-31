package wiki.sogou.jmail;

import org.junit.Test;
import wiki.sogou.jmail.util.MimeUtils;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
                .from("admin@4chan.tv")
                .to("admin@4chan.tv")
                .cc("admin@4chan.tv", "test@4chan.tv", "admin@4chan.tv", "test@coremail.cn")
                .bcc("叶沐 <zwye@coremail.cn>")
                .addBcc(InternetAddress.parse("叶镇武 <zwye@coremail.cn>"))
                .addBcc(new InternetAddress("zwye@4chan.tv", "叶镇武", "utf-8"))
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
        FileOutputStream fos = new FileOutputStream("C:\\Users\\JimYip\\Desktop\\stream\\test.eml");
        message.writeTo(fos);
    }
}
