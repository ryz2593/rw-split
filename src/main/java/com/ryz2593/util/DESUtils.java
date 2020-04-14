package com.ryz2593.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author ryz2593
 * @date 2019/4/10
 * @desc
 */
public class DESUtils {

    private static Key key;
    private static String KEY_STR = "rwKey";
    private static String CHARSET_NAME = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            generator.init(new SecureRandom(KEY_STR.getBytes()));
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    static {
//        try {
//            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PPNG");
//            secureRandom.setSeed(KEY_STR.getBytes());
//            generator.init(secureRandom);
//            key = generator.generateKey();
//            generator = null;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String getEncryptString(String str) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            byte[] bytes = str.getBytes(CHARSET_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ;
            byte[] doFinal = cipher.doFinal(bytes);
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDecryptString(String str) {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            byte[] bytes = base64Decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] doFinal = cipher.doFinal(bytes);

            return new String(doFinal, CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String cod = getEncryptString("test01");
        System.out.println(cod);

        System.out.println(getDecryptString(getEncryptString("123456")));
    }
}
