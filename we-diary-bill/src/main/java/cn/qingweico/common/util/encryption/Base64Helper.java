package cn.qingweico.common.util.encryption;


import java.util.Base64;

/**
 * @author zhouqingwei
 */
public class Base64Helper {
    /**
     * BASE64Encoder 加密
     *
     * @param data 要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptBase64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * BASE64Decoder 解密
     *
     * @param data 要解密的字符串
     * @return 解密后的byte[]
     */
    public static byte[] decryptBase64(String data) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(data);
    }
}
