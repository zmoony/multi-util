package com.boot.util.common;



import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密
 * Cipher 使用Cipher.getInstance("AES/CBC/PKCS5Padding")，否则init方法必须带IvParameterSpec参数
 * Cipher 使用Cipher.getInstance("AES")，init方法必须带IvParameterSpec参数
 *
 * jdk17 随机SecureRandom.getInstance("SHA1PRNG")必须指定SHA1PRNG算法，否则解密会变化
 */
@Log4j2
public class AESencryptFixedSeedUtil {

    public static String encryptAES(String myData,String pwd){
        try{
            //密钥
            KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
            SecureRandom random =  SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(pwd.getBytes());
            keyGenerator.init(128,random);

            SecretKey key=keyGenerator.generateKey();
//            byte[] codeFormat=secretKey.getEncoded();
//            SecretKeySpec key=new SecretKeySpec(codeFormat,"AES");

            //随机生成初始化向量数组
            //SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16]; // AES的block size是16字节
            random.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            //密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //// 也可以选择其他模式，如AES/ECB/NoPadding
            cipher.init(Cipher.ENCRYPT_MODE, key,ivParams);
            //加密后数组
            byte[] encMyData=cipher.doFinal(myData.getBytes());
            //通常将加密后数组和向量数组一起传输
//            byte[] result = new byte[ivParams.getIV().length + encMyData.length];
//            System.arraycopy(ivParams.getIV(), 0, result, 0, ivParams.getIV().length);
//            System.arraycopy(encMyData, 0, result, ivParams.getIV().length, encMyData.length);
//            log.info("iv长度{}，加密后长度{}",ivParams.getIV().length, encMyData.length);

            return Base64.getEncoder().encodeToString(encMyData);
        }catch (Exception ex){
            return "error";
        }
    }


    public static  String decryptAES(String encMyData,String pwd){

        try{
            byte[] ivAndEncryptedData=Base64.getDecoder().decode(encMyData);
            //密钥
            KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
            SecureRandom random =  SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(pwd.getBytes());
            keyGenerator.init(128,random);

            SecretKey key=keyGenerator.generateKey();

            byte[] iv = new byte[16]; // AES的block size是16字节
            random.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

//            byte[] codeFormat=secretKey.getEncoded();
//            SecretKeySpec key=new SecretKeySpec(codeFormat,"AES");
            //获取矢量数组
//            byte[] iv = new byte[16]; // IV的大小
//            System.arraycopy(ivAndEncryptedData, 0, iv, 0, iv.length);
//            IvParameterSpec ivParams = new IvParameterSpec(iv);

            //创建密码器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //// 也可以选择其他模式，如AES/ECB/NoPadding
            cipher.init(Cipher.DECRYPT_MODE,key,ivParams);

            //获取加密数组
//            byte[] encryptedData = new byte[ivAndEncryptedData.length - iv.length];
//            System.arraycopy(ivAndEncryptedData, iv.length, encryptedData, 0, encryptedData.length);

            byte[] decryptedData =cipher.doFinal(ivAndEncryptedData);
            return new String(decryptedData);
        }catch (Exception ex){
            log.error("解密异常：",ex);
            return "error";
        }
    }

}

