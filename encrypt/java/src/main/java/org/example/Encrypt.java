package org.example;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encrypt {
    private static final String KEY_FILE_NAME = "aes-key.txt";
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private static final String INITIAL_VECTOR = "FmsRVks2VoUDFPhXEVT0Tw==";

    private static final String DST_FILE_NAME = "encrypted.dat";
    private static final int READ_BUFF_SIZE = 1024;

    private static SecretKey readKey() throws IOException {
        try (FileReader fr = new FileReader(KEY_FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
            String keyStr = br.readLine();
            byte[] keyData = Base64.getDecoder().decode(keyStr);
            return new SecretKeySpec(keyData, KEY_ALGORITHM);
        }
    }

    private static void encrypt(String srcFileName, String dstFileName)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException {
        SecretKey secretKey = readKey();
        byte[] initialVector = Base64.getDecoder().decode(INITIAL_VECTOR.getBytes());

        final Cipher encryptCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(initialVector));

        try (FileInputStream fis = new FileInputStream(srcFileName);
                FileOutputStream fos = new FileOutputStream(dstFileName);
                CipherOutputStream cos = new CipherOutputStream(fos, encryptCipher)) {
            byte[] buff = new byte[READ_BUFF_SIZE];
            int readCount;
            while ((readCount = fis.read(buff)) >= 0) {
                if (readCount == 0) {
                    continue;
                }

                cos.write(buff, 0, readCount);
            }
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println(String.format("Usage: java %s <input-file>",
                        Encrypt.class.getCanonicalName()));
                System.exit(-1);
            }

            String inputFileName = args[0];

            encrypt(inputFileName, DST_FILE_NAME);

            System.out.println(String.format("Input file : %s", inputFileName));
            System.out.println(String.format("Output file: %s", DST_FILE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
