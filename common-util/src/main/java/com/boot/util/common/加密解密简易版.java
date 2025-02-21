package com.boot.util.common;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
/**
 * 加密解密简易版
 *
 * @author yuez
 * @since 2024/10/10
 */
public class 加密解密简易版 {
}


/**
 * 对称加密
 * 采用是ECB的模式 所以解密前必须知道加密时的key
 * 密钥长度必须为16位 否则会报错
 *
 * 1. 密钥生成：
 * 使用给定的字符串 key 生成密钥 secretKey。
 * 这种方法简单，但不够安全，因为密钥直接依赖于字符串 key。
 * 2. 加密模式：
 * 使用 ECB 模式，这是一种块加密模式，每个块独立加密。
 * ECB 模式的缺点是如果相同的数据块出现在不同位置，加密后的结果也会相同，容易暴露模式。
 * 3. 初始化向量（IV）：
 * 不使用 IV，因此每次加密的结果相同。
 */
 class AesEncryptionExample {

    public static void main(String[] args) throws Exception {
        String key = "1234567890123456"; // 密钥长度必须为 16 字节
        String message = "Hello, World!";

        byte[] encrypted = encrypt(message, key);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

        String decrypted = decrypt(encrypted, key);
        System.out.println("Decrypted: " + decrypted);
    }

    //指定key ,没有使用iv向量
    private static byte[] encrypt(String message, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(message.getBytes());
    }

    private static String decrypt(byte[] encrypted, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(encrypted));
    }
}


/**
 * 非对称加密
 */
 class RsaEncryptionExample {

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String message = "Hello, World!";
        byte[] encrypted = encrypt(message, publicKey);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

        String decrypted = decrypt(encrypted, privateKey);
        System.out.println("Decrypted: " + decrypted);
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    private static byte[] encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    private static String decrypt(byte[] encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(encrypted));
    }
}


