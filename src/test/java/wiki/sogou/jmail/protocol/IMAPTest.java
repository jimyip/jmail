package wiki.sogou.jmail.protocol;

import org.junit.Test;
import wiki.sogou.jmail.builder.PropertiesBuilders;
import wiki.sogou.jmail.parser.MimeMessageParser;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Properties;

/**
 * @author JimYip
 */
public class IMAPTest {
    @Test
    public void test() throws MessagingException, IOException {
        Properties properties = PropertiesBuilders.IMAP()
                .host("192.168.1.128")
                .port(143)
                .build();

        Session session = Session.getInstance(properties);
        Store store = session.getStore();
        store.connect("admin@wiki.sogou", "aabb123456");

        Folder defaultFolder = store.getDefaultFolder();

        Folder[] allFolder = defaultFolder.list();
        for (Folder folder : allFolder) {
            System.out.println("folder=" + folder.getName());
        }

//        Folder folder = store.getFolder("Drafts");
//        folder.open(Folder.READ_WRITE);
//
//        System.out.println("收件箱中共" + folder.getMessageCount() + "封邮件!");
//        System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
//        System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
//        System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");
//
//        System.out.println("------------------------邮件分割线------------------------------");
//        Message[] messages = folder.getMessages(1, folder.getMessageCount());
//        for (Message message : messages) {
//            MimeMessage msg = (MimeMessage) message;
//            String subject = MimeUtility.decodeText(msg.getSubject());
//            System.out.println("[" + subject + "]");
//            parseMessage(msg);
//        }
//
//        store.close();
    }

    private void parseMessage(MimeMessage msg) {
        try {
            MimeMessageParser parser = MimeMessageParser.of(msg);
            System.out.println(parser.getTo());
            System.out.println(parser.getSize());
            System.out.println(parser.getHtml());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
