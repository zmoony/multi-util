package com.boot.websocket.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * CommonCryptoUtil
 *
 * @author yuez
 * @since 2024/11/4
 */
public class CommonCryptoUtil{

    private static final byte[] KEY = "087841ec80ec3fd7029fdcaa7f565eed".getBytes();

    /**
     * 普通加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String aesCrypto(String content) throws Exception{
        //构建
        SymmetricCrypto aes = SecureUtil.aes(KEY);
        //加密
       return aes.encryptBase64(content);
    }

    /**
     * 普通解密
     * @param content
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String content) throws Exception{
        //构建
        SymmetricCrypto aes = SecureUtil.aes(KEY);
        //解密
        return aes.decryptStr(content);
    }

    /**
     * 16进制加密
     */
    public static String aesHexCrypto(String content) throws Exception{
        //构建
        SymmetricCrypto aes = SecureUtil.aes(KEY);
        //加密为16进制表示
        String encryptHex = aes.encryptHex(content);
        return encryptHex;
    }

    /**
     * 16进制解密
     */
    public static String aesHexDecrypt(String content) throws Exception{
        //构建
        SymmetricCrypto aes = SecureUtil.aes(KEY);
        //解密为字符串
        String decryptStr = aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }


    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "yuez");
        map.put("age", "18");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);

        String commonE = CommonCryptoUtil.aesCrypto(json);
        System.out.println("普通加密："+commonE);
        String commonD = CommonCryptoUtil.aesDecrypt(commonE);
        System.out.println("普通解密："+commonD);
        String hexE = CommonCryptoUtil.aesHexCrypto(json);
        System.out.println("16进制加密："+hexE);
        String hexD = CommonCryptoUtil.aesHexDecrypt(hexE);
        System.out.println("16进制解密："+hexD);
    }
}
