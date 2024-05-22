package com.maids.cc.librarymanagementsystem.patron.model;

import java.security.SecureRandom;

public class Token {

    private static SecureRandom secureRandom = new SecureRandom();

    public static String generateToken(int len) {
        StringBuilder randomNumber = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            String combineNumber = "0123456789";
            randomNumber.append(combineNumber.charAt(secureRandom.nextInt(combineNumber.length())));
        }
        return new String(randomNumber);
    }
}
