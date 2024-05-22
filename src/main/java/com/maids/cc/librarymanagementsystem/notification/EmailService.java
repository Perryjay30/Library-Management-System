package com.maids.cc.librarymanagementsystem.notification;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendNotificationEmailForRegistration(String recipientMail, String content);
    void sendNotificationEmailToResetPassword(String recipientEmail, String content);
    String buildEmailForRegistration(String name, String token);
    String buildEmailForResetPassword(String name, String token);

    void sendEmailForSuccessfulOrder(String recipientEmail, String name,  Integer orderId) throws MessagingException;
    void emailForAssignRole(String recipientEmail, String name) throws MessagingException;
}
