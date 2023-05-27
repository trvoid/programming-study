package org.example;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KeyGen {
    private static final String KEY_FILE_NAME = "aes-key.txt";
    private static final String KEY_ALGORITHM = "AES";

    private static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecretKey secretKey = keyGen.generateKey();
        byte[] keyData = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyData);
    }

    public static void main(String[] args) {
        try (FileWriter fw = new FileWriter(KEY_FILE_NAME)) {
            String keyStr = generateKey();
            System.out.println("KEY: [" + keyStr + "]");
            fw.write(keyStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
