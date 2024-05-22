package com.maids.cc.librarymanagementsystem.notification;

import com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Async
    @Override
    public void sendNotificationEmailForRegistration(String recipientMail, String content) {
        try{
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, "utf-8");
            mimeMessageHelper.setSubject("Verify Your Email Address!!");
            mimeMessageHelper.setTo(recipientMail);
            mimeMessageHelper.setFrom("pelumijsh@gmail.com");
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mailMessage);
            System.out.println("mail sent successfully");
        } catch(MessagingException e){
            log.info("Problem: " + e.getMessage());
            throw new RuntimeException();
        } catch(MailException e) {
            log.info("Problem 2: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNotificationEmailToResetPassword(String recipientEmail, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("pelumijsh@gmail.com");
            helper.setTo(recipientEmail);
            String subject = "Here's the token to reset your password";
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch(MessagingException e){
            log.info("Problem: " + e.getMessage());
            throw new RuntimeException();
        } catch(MailException e) {
            log.info("Problem 2: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailForSuccessfulOrder(String recipientEmail, String name, Integer orderId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom("pelumijsh@gmail.com");
        messageHelper.setTo(recipientEmail);
        String subject = "Successful Order";
        String content = "<p>Hello " + name + ",</p>"
                + "<p>You have successfully placed an order. Your goods will be delivered to you within " +
                "3 - 4 business working days</p>"
                + "<p>Here is your orderId</p>" + orderId
                + "<p>. Thank you for choosing Starlight Online Store!!!.</p>";
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void emailForAssignRole(String recipientEmail, String name) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom("pelumijsh@gmail.com");
        String subject = "Admin Notification";
        String content = "<p>Hello " + name + ",</p>"
                + "<p>This is to notify you that you're now an Admin of Starlight Online Store. " +
                "Kindly login to the app to confirm.</p>";
        messageHelper.setSubject(subject);
        messageHelper.setTo(recipientEmail);
        messageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);

    }

    public String buildEmailForRegistration(String name, String token) {
        return "<div style=\"font-family:Helvetica, Arial, sans-serif; font-size:16px; margin:0; color:#0b0c0c\">\n" +
                "<span style=\"display:none; font-size:1px; color:#fff; max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse; min-width:100%; width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse; max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px; line-height:1.315789474; margin-top:4px; padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica, Arial, sans-serif; font-weight:700; color:#ffffff; text-decoration:none; vertical-align:top; display:inline-block\">Confirm your email</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; max-width:580px; width:100%!important\" width=\"100%\">\n" +
                "  <tr>\n" +
                "    <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "    <td>\n" +
                "      <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "        <tr>\n" +
                "          <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; max-width:580px; width:100%!important\" width=\"100%\">\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica, Arial, sans-serif; font-size:19px; line-height:1.315789474; max-width:560px\">\n" +
                "      <p style=\"margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "      <p style=\"margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">\n" +
                "        Welcome to Perry Library Management System! We're thrilled to have you onboard. To ensure the security of your account and complete the registration process, please verify your email address by entering the OTP (One-Time Password) provided below:\n" +
                "      </p>\n" +
                "      <blockquote style=\"margin:0 0 20px 0; border-left:10px solid #b1b4b6; padding:15px 0 0.1px 15px; font-size:19px; line-height:25px\">\n" +
                "        <p style=\"margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">" + token + "</p>\n" +
                "      </blockquote>\n" +
                "      <p>Token will expire in 10 minutes. See you soon</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "<div class=\"yj6qo\"></div>\n" +
                "<div class=\"adL\"></div>\n" +
                "</div>";
    }

    public String buildEmailForResetPassword(String name, String token) {
        return "<p>Hello,</p>" + name
                + "<p>You have requested to reset your password.</p>"
                + "<p>Copy the token below to change your password:</p>"
                + token
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
    }


}

