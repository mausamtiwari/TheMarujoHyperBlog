package be.intec.themarujohyperblog.service;

import be.intec.themarujohyperblog.model.ForgotPasswordToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final int MINUTES_TO_EXPIRE = 10;

    @Override
    public String generateToken(String email) {
        return UUID.randomUUID().toString();
    }

    @Override
    public LocalDateTime expireTimeRange() {
        return LocalDateTime.now().plusMinutes(MINUTES_TO_EXPIRE);
    }

    @Override
    public void sendEmail(String to, String subject, String emailLink, String userName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String emailBody = "<p>Hello " + userName + ",</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<p><a href=\"" + emailLink + "\">Reset Password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you did not request a password reset.</p>";

        helper.setText(emailBody, true);
        helper.setFrom("mausamtiwari93@gmail.com", "Password Reset");
        helper.setSubject(subject);
        helper.setTo(to);
        javaMailSender.send(message);
    }

    public boolean isExpired(ForgotPasswordToken forgotPasswordToken) {
        return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
    }

    public String checkValidity(ForgotPasswordToken forgotPasswordToken, Model model) {
        if (forgotPasswordToken == null) {
            model.addAttribute("error", "Invalid Token");
            return "error-page";
        } else if (forgotPasswordToken.isUsed()) {
            model.addAttribute("error", "The token is already used");
            return "error-page";
        } else if (isExpired(forgotPasswordToken)) {
            model.addAttribute("error", "The token is expired");
            return "error-page";
        } else {
            return "reset-password";
        }
    }
}

