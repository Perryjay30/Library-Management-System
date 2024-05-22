package com.maids.cc.librarymanagementsystem.notification;

import jakarta.mail.MessagingException;

@FunctionalInterface
public interface EmailSender {
    void send(String emailAddress, String emailContent);
}

