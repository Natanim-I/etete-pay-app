package com.oasis.FIFAFanWallet.service;

import com.oasis.FIFAFanWallet.exception.EmailDeliveryException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String verificationToken){
        String link = frontendUrl + "/auth/verify?token=" + verificationToken;
        MimeMessage message = mailSender.createMimeMessage();
        try
        {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("Verify Your Account");
            helper.setTo(to);
            helper.setText("""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6;">
        
            <h2>Welcome to Etete Pay</h2>

            <p>
                Thank you for registering with Etete Pay.
            </p>

            <p>
                Please verify your email address by clicking the button below:
            </p>

            <p>
                <a href="%s"
                   style="
                        background-color: #0d6efd;
                        color: white;
                        padding: 12px 20px;
                        text-decoration: none;
                        border-radius: 5px;
                        display: inline-block;">
                    Verify Account
                </a>
            </p>

            <p>
                If the button does not work, copy and paste the following link into your browser:
            </p>

            <p>%s</p>

            <hr>

            <p>
                This verification link will expire in 1 hour.
            </p>

            <p>
                If you did not create an account, please ignore this email.
            </p>

            <br>

            <p>
                Regards,<br>
                Etete Pay Team
            </p>

        </body>
        </html>
        """.formatted(link, link), true
            );
        }catch (MessagingException ex){
            throw new EmailDeliveryException("Failed to send verification email.");
        }
        mailSender.send(message);
    }

    public void sendForgotPassEmail(String to, String resetToken){
        String link = frontendUrl + "/user/reset-password?token=" + resetToken;
        MimeMessage message = mailSender.createMimeMessage();
        try
        {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("Reset Your Password");
            helper.setTo(to);
            helper.setText("""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6;">
        
            <h2>Reset your Etete Pay account password with the below options.</h2>

            <p>
                Please reset your password by clicking the button below:
            </p>

            <p>
                <a href="%s"
                   style="
                        background-color: #0d6efd;
                        color: white;
                        padding: 12px 20px;
                        text-decoration: none;
                        border-radius: 5px;
                        display: inline-block;">
                    Reset Password
                </a>
            </p>

            <p>
                If the button does not work, copy and paste the following link into your browser:
            </p>

            <p>%s</p>

            <hr>

            <p>
                This reset link will expire in 1 hour.
            </p>

            <p>
                If you did not request to reset your password, please ignore this email.
            </p>

            <br>

            <p>
                Regards,<br>
                Etete Pay Team
            </p>

        </body>
        </html>
        """.formatted(link, link), true
            );
        }catch (MessagingException ex){
            throw new EmailDeliveryException("Failed to send password reset email.");
        }
        mailSender.send(message);
    }

    public void sendPassResetConfirmationEmail(String to){
        MimeMessage message = mailSender.createMimeMessage();
        try
        {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("Password Reset Success.");
            helper.setTo(to);
            helper.setText("""
        <html>
        <body style="font-family: Arial, sans-serif; line-height: 1.6;">
        
            <h2>Your Etete Pay account password has been successfully changed.</h2>
   
            <p>
                If you did not request to reset your password, please reach out to us.
            </p>

            <br>

            <p>
                Regards,<br>
                Etete Pay Team
            </p>

        </body>
        </html>
        """);
        }catch (MessagingException ex){
            throw new EmailDeliveryException("Failed to send password reset confirmation email.");
        }
        mailSender.send(message);
    }
}
