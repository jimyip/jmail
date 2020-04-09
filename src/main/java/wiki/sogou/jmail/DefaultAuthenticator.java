package wiki.sogou.jmail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author JimYip
 */
public class DefaultAuthenticator extends Authenticator {
    private final String pass;

    public static Authenticator of(String pass) {
        return new DefaultAuthenticator(pass);
    }

    private DefaultAuthenticator(String pass) {
        assert pass != null;

        this.pass = pass;
    }

    @Override
    protected final PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.getDefaultUserName(), this.pass);
    }
}
