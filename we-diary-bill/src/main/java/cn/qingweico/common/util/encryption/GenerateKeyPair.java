package cn.qingweico.common.util.encryption;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * 生成一对密钥, 即私钥和公钥,
 * 对于密钥的保存可以使用对象流的方式进行保存和传送,也可以使用编码的方式保存；
 */
@Slf4j
public class GenerateKeyPair {

    private String priKey;
    private String pubKey;

    public void generate(String name) {
        try {
            java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator
                    .getInstance("RSA");
            SecureRandom secrand = new SecureRandom();
            // 初始化随机产生器
            secrand.setSeed(name.getBytes());
            keygen.initialize(512, secrand);
            KeyPair keys = keygen.genKeyPair();
            PublicKey pubkey = keys.getPublic();
            PrivateKey prikey = keys.getPrivate();

            pubKey = Base64Helper.encryptBase64(pubkey.getEncoded());
            priKey = Base64Helper.encryptBase64(prikey.getEncoded());

            log.info("pubKey=" + pubKey);
            log.info("priKey=" + priKey);
            log.info("生成密钥对成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成密钥对失败");
        }
        ;

    }

    /**
     * Transform the specified byte into a Hex String form.
     */
    public static final String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);

        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }

        return s.toString();
    }

    /**
     * Transform the specified Hex String into a byte array.
     */
    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }

        return bytes;
    }

    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * @param args
     */
    public static void main(String[] args) {
        GenerateKeyPair n = new GenerateKeyPair();
        n.generate("www.jdd966");
    }
}
