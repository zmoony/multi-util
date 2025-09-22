/*
package com.boot.util.wiscom.加密解密;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;


*/
/**
 * 3des cbc加密解密
 *
 * @author hwang
 * *//*

public class DesCbcCoder {
    */
/**3des cbc解密，第一个参数是密文，第二个参数是秘钥，第三个参数是IV*//*

    public static String decrypt(String arg1, String arg2, byte[] arg3) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
//        return new String(decrypt(Base64.decode(arg1), arg2.getBytes(), arg3));
        return "";
    }
    private static byte[] decrypt(byte[] arg2, byte[] arg3, byte[] arg4) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        SecretKey v3 = SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(arg3));
        Cipher v0 = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        v0.init(2, ((Key) v3), new IvParameterSpec(arg4));
        return v0.doFinal(arg2);
    }

    */
/**3des cbc加密，第一个参数是明文，第二个参数是秘钥，第三个参数是IV*//*

    public static String encrypt(String arg0, String arg1, byte[] arg2) {
        try {
            byte[] doFinal = encrypt(arg0.getBytes(), arg1.getBytes(), arg2);
            return Base64.encode(doFinal);
        } catch (Exception v0) {
            v0.printStackTrace();
            return null;
        }
    }
    private static byte[] encrypt(byte[] arg2, byte[] arg3, byte[] arg4) throws Exception {
        SecretKey v3 = SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(arg3));
        IvParameterSpec v0 = new IvParameterSpec(arg4);
        Cipher v4 = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        v4.init(1, ((Key) v3), ((AlgorithmParameterSpec) v0));
        return v4.doFinal(arg2);
    }
}


*/
