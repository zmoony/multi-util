package com.boot.util.wiscom.加密解密;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.log4j.Log4j2;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class AESUtils {

    /**
     * 数据加密
     * @param content 待加密内容
     * @param key 加密秘钥
     * @return
     */
	public static  String encrypt(String content,String key){
		byte[] bytes= SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes())
                .getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES,bytes);

        return aes.encryptHex(content, CharsetUtil.CHARSET_GBK);
	}

    /**
     * 数据解密
     * @param content   密文
     * @param key  解密秘钥
     * @return
     */
    public static String decrypt(String content,String key){
        try {
			byte[] bytes=SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()
			        ,key.getBytes()).getEncoded();
			SymmetricCrypto aes=new SymmetricCrypto(SymmetricAlgorithm.AES,bytes);
			return aes.decryptStr(content, CharsetUtil.CHARSET_GBK);
		} catch (Exception e) {
			log.error("数据解密异常,"+content +":"+e, e);
			throw e;
		}
    }

    //使用示例
    public static void main(String[] args) throws NoSuchAlgorithmException{
    	KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128位密钥
        SecretKey key = keyGen.generateKey();
        System.out.println(key.toString());
	    String jiam=AESUtils.encrypt("{\"id_card\":\"\",\"device_sn\":\"35507378167\","
	    		+ "\"startTime\":\"2024-12-04 00:00:00\",\"endTime\":\"2024-12-06 12:00:00\","
	    		+ "\"page\":1,\"limit\":10}","087841ec80ec3fd7029fdcaa7f565eed");
        System.out.println(jiam);
        System.out.println(decrypt("70e30efedd3f3b0e4c77a8b3e14a1f53fff144462a27e6fca15bb633192ed8d2b656d1cd8da8abbf34ff1f99e3980538774545c0836eef6c3d8ca2f0f3b1cece16dd277e7124c93918d91bc7ca687684f2c2429d29eceac88ad20baca0234cbc3efbbeeea66e180943580fd5f8a42dc09a0e2a3d54e8c855687ca03bbd2393be8cd1553c9fc0eac1f495037152a41a8b442d425515f4d25a87396f4ce76b2108ff8875534e7332521bed84887e0c66a80a9f1ea69801186b774edded0c41f085ddda0dc2552c1efa2540031abbb417e9229ff98b033ff3692520568e632e721b72367240f256bf7fe1c9bf447641f3e05baf450072e5d116a6c2a2d2ef8318f0fc0422196e37c87b92d2020d62c536d4bd38d7c06c6ab2c4315f1900b292f6f74a7fd7805846111232c532553804cbd1b95df3a3771b3b80a6216e4c549edf5f3c3af1ff0fddde47accc9b373dcbb4f0bbbecbc403a8888e8b64f8a2d8b307899b913e8f527aee15f9714f19ac1aa7a39c26c4ebc0e10e1a079bfb14f3d8c69689ca8a07570513eb326e6ff9414e0069a9102dcb5efed3d18640e4922204c80f1bb182840b3514ef949a67b0b9bbb30dbe158154ebf348870c592340e2c513de16dd277e7124c93918d91bc7ca687684f2c2429d29eceac88ad20baca0234cbc3efbbeeea66e180943580fd5f8a42dc03fa84d7ed59323743cd6668648cb1ba18cd1553c9fc0eac1f495037152a41a8b442d425515f4d25a87396f4ce76b2108ff8a06f7f245edea8fff2b86b41f950d946e0c0d8a54e2f2d20665597318530eadf6607c11ad523005cbbb5bd44ca428b84084204d2ac8262dbc7805733284924db8a0ce9d508020cfe7cd046eec92ed9fef2fa20c7314cae9a0f502790aabae0705c61ef791212dbc33733ce4c8de2432fc4315bab721f4522cabcc5b4e090867b5d24fcdb2b3bfd1cfbb7a3b6a3f12822284c55f189f27cf35050dfc11a8cbaf046170f104a0fc70ff9c121437a79b5c02633368dbdbd81e21ae43df3405e966aa3d1341085d93d4870f3bcf077aeeb2225bca6243a36bd0504c06f30f24057ae360f95a9f42a8a06a6706aca870d60f2d767885d75fe72702b9019f24405835a62f9e93203db03db388dee7a4c108a8354fb5b0af4e5770cd5ae7455c5933131df69918d4e3bfa797f5b7c3ed2e6de0095b477c842f01cfe60ec58457440660df21c4130c3af867fa054ecd454ef6c702e62bfb70aa4d6a1f019eb7f811539e86092a05d2994a2b8299e0c329792aaa3439dd93e0ef7cffdb34abda7a506aecbc13a3101e776dfcc1a7e49eb82a371e078717bbe20f062ec90dac3c84206aa8fc2d8f8dcc36cf5661aa7fcc9eb1f11a370968168e83d35f05c768b21217be5f077cc63fb290d8bc6a66411d64ccd8bb4df8ab2e868996eefbbbb8b8ff3c2b415df2b4f98099bebcf9b062c2f22beb3388511b8e8408643d4630425c457c5fc5b84ec827c886a3b1bf9e36ddd3b3ef851f7732cac2e206c292200d7e23e0590677529543bd8e36ee203d925f0f63d4cef97d5bbc7c487c18ac2811a6dcf4ade0095b477c842f01cfe60ec5845744067f5d33a752943b6bd1a90ca1a6cff2846b8c3bea797c3c3c994161451253183c932454cf377dff95115be2f1791a2588775f354a729e4aa73d7af29dc37e7c222d2139da582f6437cc90045bdce3b70e82051dab6974c067a38ec8b7887e16a2","087841ec80ec3fd7029fdcaa7f565eed"));
//        System.out.println(AESUtils.encrypt("wiscom123!",Base64.getEncoder().encodeToString("wiscom123!".getBytes())));

    }
}
