package wiki.sogou.jmail.exception;

import javax.mail.MessagingException;
import java.util.Objects;

/**
 * @author JimYip
 */
public class UncheckedMessagingException extends RuntimeException {

    public UncheckedMessagingException(String message, MessagingException cause) {
        super(message, Objects.requireNonNull(cause));
    }

    public UncheckedMessagingException(MessagingException cause) {
        super(Objects.requireNonNull(cause));
    }

    @Override
    public MessagingException getCause() {
        return (MessagingException) super.getCause();
    }


}
