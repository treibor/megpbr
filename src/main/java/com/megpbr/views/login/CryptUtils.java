package com.megpbr.views.login;

import java.util.Base64;

public class CryptUtils {

    public static String decryptUsername(String encryptedUsername, String key) {
        return decrypt(encryptedUsername, key);
    }

    public static String decryptPassword(String encryptedPassword, String key) {
        return decrypt(encryptedPassword, key);
    }

    private static String decrypt(String encryptedText, String key) {
        try {
            // Convert the key to bytes
            byte[] keyBytes = key.getBytes("UTF-8");

            // Convert the encrypted text (Base64) to bytes
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

            // Perform XOR operation with the key
            byte[] decryptedBytes = new byte[encryptedBytes.length];
            for (int i = 0; i < encryptedBytes.length; i++) {
                decryptedBytes[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]);
            }

            // Convert the decrypted bytes to string
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}