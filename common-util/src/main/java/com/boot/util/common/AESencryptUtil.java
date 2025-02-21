package com.boot.util.common;

import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 注意：只要种子（seed）是一样的，那么生成的密钥（SecretKey）和初始化向量（IV）必定都是一样的。
 * 所以有的时候，会使用密码进行固定的加密{@link AESencryptFixedSeedUtil}
 */
@Log4j2
public class AESencryptUtil {

    /**
     * 采用CBC模式，随机生成key 和初始化向量
     * 如果固定了seed 就可以固定出密钥和初始化向量
     * @param myData
     * @param pwd
     * @return
     */
    public static String encryptAES(String myData, String pwd) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(pwd.getBytes());
            keyGenerator.init(128, random);

            SecretKey key = keyGenerator.generateKey();

            byte[] iv = new byte[16];
            long seed = System.currentTimeMillis();
            random.setSeed(seed); //使用相同的seed 生成相同的IV 所以也可以不用传递 IV。一般建议 使用随机的seed
            random.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

            byte[] encMyData = cipher.doFinal(myData.getBytes());

            byte[] result = new byte[iv.length + encMyData.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encMyData, 0, result, iv.length, encMyData.length);

            return Base64.getEncoder().encodeToString(result);
        } catch (Exception ex) {
            return "error";
        }
    }

    public static String decryptAES(String encMyData, String pwd) {
        try {
            byte[] ivAndEncryptedData = Base64.getDecoder().decode(encMyData);

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(pwd.getBytes());
            keyGenerator.init(128, random);

            SecretKey key = keyGenerator.generateKey();

            byte[] iv = new byte[16];
            System.arraycopy(ivAndEncryptedData, 0, iv, 0, iv.length);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);

            byte[] encryptedData = new byte[ivAndEncryptedData.length - iv.length];
            System.arraycopy(ivAndEncryptedData, iv.length, encryptedData, 0, encryptedData.length);

            byte[] decryptedData = cipher.doFinal(encryptedData);
            return new String(decryptedData);
        } catch (Exception ex) {
            log.error("解密异常：", ex);
            return "error";
        }
    }

    public static void main(String[] args) {
    	String pwd = "123456";
    	String data = "hello world";

    	String encrypt = encryptAES(data, pwd);
    	System.out.println("加密后：" + encrypt);

    	String decrypt = decryptAES(encrypt, pwd);
    	System.out.println("解密后：" + decrypt);
	}
}
