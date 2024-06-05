package be.intec.themarujohyperblog.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

public interface ForgotPasswordService {
    String generateToken(String email);

    LocalDateTime expireTimeRange();

    void sendEmail(String to, String subject, String emailLink, String userName) throws MessagingException, UnsupportedEncodingException;
}

