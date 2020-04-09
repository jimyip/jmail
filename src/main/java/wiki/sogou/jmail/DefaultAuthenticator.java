package wiki.sogou.jmail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author JimYip
 */
public class DefaultAuthenticator extends Authenticator {
    private final String pass;
    private final String user;

    public static Authenticator of(String pass) {
        return of(null, pass);
    }

    public static Authenticator of(String user, String pass) {
        return new DefaultAuthenticator(user, pass);
    }

    private DefaultAuthenticator(String user, String pass) {
        assert pass != null;
        this.user = user;
        this.pass = pass;
    }

    @Override
    protected final PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user == null ? this.getDefaultUserName() : user, this.pass);
    }
}
