package cn.qingweico.common.util.encryption;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author zhouqingwei
 */
public class RASUtil {

    /**
     * @param info 需要签名的数据
     */
    public static String sign(String info) throws Exception {
        PKCS8EncodedKeySpec priPKCS8  = new PKCS8EncodedKeySpec(Base64Helper.decryptBase64(EncryptedString.priKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(priPKCS8);
        // 用私钥对信息生成数字签名
        java.security.Signature signet = java.security.Signature
                .getInstance("SHA256withRSA");
        signet.initSign(privateKey);
        signet.update(info.getBytes(StandardCharsets.UTF_8));
        // 对信息的数字签名
        byte[] signed = signet.sign();
        return Base64Helper.encryptBase64(signed);

    }

    /**
     * @param sign 签名信息
     * @param info 原数据
     * @return
     */
    public static boolean verifySign(String sign, String info) throws Exception {
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64Helper.decryptBase64(EncryptedString.pubKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
        byte[] signed = Base64Helper.decryptBase64(sign);
        java.security.Signature signetcheck = java.security.Signature.getInstance("MD5withRSA");
        signetcheck.initVerify(pubKey);
        signetcheck.update(info.getBytes());
        return signetcheck.verify(signed);
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

    public static void main(String[] args) throws Exception {
        String sign = sign("钱多多");
        System.err.println(sign);

//        Boolean info = verifySign("eUZ5y1VWEQpzzVrLVPopFEmP6/HQtctxQHUC+cafz8qCWKAbB7eUZIxSzphxUedMcJ7VplkcPsvZwuxTKgNltg==",
//                "钱多多");
//        System.err.println(info);
    }

}

