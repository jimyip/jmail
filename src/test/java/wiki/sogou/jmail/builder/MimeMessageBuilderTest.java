package wiki.sogou.jmail.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


class MimeMessageBuilderTest {

    @Spy
    private MimeMessageBuilder builder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuilder() throws MessagingException {
        builder.from("admin@test.com");
        MimeMessage message = builder.build();
        Address[] from = {new InternetAddress("admin@test.com")};
        Assertions.assertArrayEquals(message.getFrom(), from);
    }

}