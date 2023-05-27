package org.example;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Decrypt {
    private static final String KEY_FILE_NAME = "aes-key.txt";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private static final String INITIAL_VECTOR = "FmsRVks2VoUDFPhXEVT0Tw==";

    private static final String SRC_FILE_NAME = "encrypted.dat";
    private static final String DST_FILE_NAME = "decrypted.dat";
    private static final int READ_BUFF_SIZE = 1024;

    private static SecretKey readKey() throws IOException {
        try (FileReader fr = new FileReader(KEY_FILE_NAME);
                BufferedReader br = new BufferedReader(fr)) {
            String keyStr = br.readLine();
            byte[] keyData = Base64.getDecoder().decode(keyStr);
            return new SecretKeySpec(keyData, KEY_ALGORITHM);
        }
    }

    private static void decrypt(String srcFileName, String dstFileName)
            throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey secretKey = readKey();
        byte[] initialVector = Base64.getDecoder().decode(INITIAL_VECTOR.getBytes());

        final Cipher decryptCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(initialVector));

        try (FileInputStream fis = new FileInputStream(srcFileName);
                CipherInputStream cis = new CipherInputStream(fis, decryptCipher);
                FileOutputStream fos = new FileOutputStream(dstFileName)) {
            byte[] buff = new byte[READ_BUFF_SIZE];
            int readCount;
            while ((readCount = cis.read(buff)) >= 0) {
                if (readCount == 0) {
                    continue;
                }

                fos.write(buff, 0, readCount);
            }
        }
    }

    public static void main(String[] args) {
        try {
            decrypt(SRC_FILE_NAME, DST_FILE_NAME);

            System.out.println(String.format("Input file : %s", SRC_FILE_NAME));
            System.out.println(String.format("Output file: %s", DST_FILE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
