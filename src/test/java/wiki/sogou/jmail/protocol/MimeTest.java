package wiki.sogou.jmail.protocol;

import com.sun.mail.imap.IMAPMessage;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Properties;

/**
 * @author JimYip
 */
public class MimeTest {
    @Test
    public void test() throws MessagingException, IOException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "192.168.1.128");
        props.setProperty("mail.imap.port", "143");

        Session session = Session.getInstance(props);

        Store store = session.getStore("imap");

        store.connect("admin@wiki.sogou", "aabb123456");

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        Message[] messages = folder.getMessages();

        System.out.println("收件箱中共" + messages.length + "封邮件!");
        System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
        System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
        System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");

        System.out.println("------------------------开始解析邮件----------------------------------");

        for (Message message : messages) {
            IMAPMessage msg = (IMAPMessage) message;
            String subject = MimeUtility.decodeText(msg.getSubject());
            System.out.println("[" + subject + "]");
            parseMessage(msg);
        }

        folder.close(false);
        store.close();
    }

    private void parseMessage(IMAPMessage msg) {

    }
}
