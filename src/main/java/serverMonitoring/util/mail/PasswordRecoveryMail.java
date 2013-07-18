package serverMonitoring.util.mail;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * Handel's mail sending functionality for password recover page.
 */
@Component
@Secured("IS_AUTHENTICATED_ANONYMOUSLY")
public interface PasswordRecoveryMail {

    /**
     * sends mail with included params
     */
    public void sendMail(String from, String to, String subject, String msg);

}
