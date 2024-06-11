package com.megpbr.views.login;

//Encrypting UserName & Password
import java.util.Base64;

public class CryptUtils {

    private static final String SECRET_KEY = "iadei"; // Change this to your secret key

    public static String decryptUsername(String encryptedUsername) {
        return decrypt(encryptedUsername);
    }

    public static String decryptPassword(String encryptedPassword) {
        return decrypt(encryptedPassword);
    }

    private static String decrypt(String encryptedText) {
        try {
            // Convert the key to bytes
            byte[] keyBytes = SECRET_KEY.getBytes("UTF-8");

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