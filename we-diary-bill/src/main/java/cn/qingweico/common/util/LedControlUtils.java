package cn.qingweico.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @version 1.0
 * @author:  jdd尚岳
 * @date:  2021-01-27 09:03
 */
public class LedControlUtils {
    public static void main(String[] args) {
        try {
            byte[] bytes = ledScreenShowAgreement("鄂A0Z08M", "03000100");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 头信息
     */
    private static final String head = "AA55";
    /**
     * 流水号
     */
    private static final String serial = "00";
    /**
     * 地址
     */
    private static final String site = "64";
    /**
     * 业务类型
     */
    private static final String type = "00";
    /**
     * 临时播放文字命令
     */
    private static final String pushPlayOrder = "27";

    /**
     * 显示二维码命令
     **/
    private static final String pushQRCodeOrder = "28";

    /**
     * 广告文字命令
     **/
    private static final String pushPermanentOrder = "25";
    /**
     * 取消临时播放文字命令
     */
    private static final String cancelPushPlayOrder = "21";
    /**
     * 播放语音命令
     */
    private static final String playVoice = "22";
    /**
     * 结尾符
     */
    private static final String stopCode = "AF";
    /**
     * 取消临时播放内容
     */
    private static final String content = "07";

    /**
     * 向led屏幕发送广告信息
     *
     * @param str  发送的数据
     * @param line 行号  第一行010100 第二行020100 第三行030100 第四行040100
     * @return
     */
    public static byte[] ledScreenShowPermanent(String str, String line) throws Exception {
        if (str == null || str == "") {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        byte[] bytes = str.getBytes("GBK");
        String formatted = bytes2HexString(bytes);
        String a = line + formatted;
        //获取字节长度
        int length = a.length() / 2;
        //高位字节
        int mainVersion = (length & 0xFF00) >> 8;
        //低位
        int minorVersion = length & 0xFF;
        String tall = numToHex16(mainVersion);
        String low = numToHex16(minorVersion);
        //包头
        buffer.append(head);
        //流水号
        buffer.append(serial);
        //地址
        buffer.append(site);
        //业务类型
        buffer.append(type);
        //命令
        buffer.append(pushPermanentOrder);
        //指定长度的高位
        buffer.append(tall);
        //指定长度的低位
        buffer.append(low);
        buffer.append(line);
        buffer.append(formatted);
        //除去头尾信息用CRC-16/MODBUS算法得到校验字符串
        byte[] bytes1 = hexToByteArray(serial + site + type + pushPermanentOrder + tall + low + line + formatted + "0000");

        String crcData = getCRCData(bytes1);
        buffer.append(crcData);
        buffer.append(stopCode);

        return hexToByteArray(buffer.toString());

    }

    /**
     * 向led屏幕发送临时播放信息
     *
     * @param str  发送的数据
     * @param line 行号  第一行01000100 第二行02000100 第三行03000100 第四行04000100
     * @return
     */
    public static byte[] ledScreenShowAgreement(String str, String line) throws Exception {
        if (str == null || str == "") {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        byte[] bytes = str.getBytes("GB2312");
        String formatted = bytes2HexString(bytes);
        String a = line + formatted;
        //获取字节长度
        int length = a.length() / 2;
        //高位字节
        int mainVersion = (length & 0xFF00) >> 8;
        //低位
        int minorVersion = length & 0xFF;
        String tall = numToHex16(mainVersion);
        String low = numToHex16(minorVersion);
        //包头
        buffer.append(head);
        //流水号
        buffer.append(serial);
        //地址
        buffer.append(site);
        //业务类型
        buffer.append(type);
        //命令
        buffer.append(pushPlayOrder);
        //指定长度的高位
        buffer.append(tall);
        //指定长度的低位
        buffer.append(low);
        buffer.append(line);
        buffer.append(formatted);
        //除去头尾信息用CRC-16/MODBUS算法得到校验字符串
        byte[] bytes1 = hexToByteArray(serial + site + type + pushPlayOrder + tall + low + line + formatted + "0000");
         String crcData = getCRCData(bytes1);
        buffer.append(crcData);
        buffer.append(stopCode);

        return hexToByteArray(buffer.toString());

    }

    /**
     * led音量控制
     *
     * @return
     */
    public static byte[] ledVolumeRange(String volume) throws Exception {
        if (volume == null || volume == "") {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        byte[] bytes = volume.getBytes("GBK");
        String formatted = bytes2HexString(bytes);
        String st = "0101" + formatted;
        //获取字节长度
        int length = st.length() / 2;
        //高位字节
        int mainVersion = (length & 0xFF00) >> 8;
        //低位
        int minorVersion = length & 0xFF;
        String tall = numToHex16(mainVersion);
        String low = numToHex16(minorVersion);
        //包头
        buffer.append("FD");
        //指定长度的高位
        buffer.append(tall);
        //指定长度的低位
        buffer.append(low);
        //流水号
        buffer.append("01");
        //地址
        buffer.append("01");
        buffer.append(formatted);
        return hexToByteArray(buffer.toString());

    }

    /**
     * LED语音播放指令
     *
     * @param str //播放语音的内容
     * @return
     * @throws Exception
     */
    public static byte[] ledPlayVoice(String str) throws Exception {
        if (str == null || str == "") {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        byte[] bytes = str.getBytes("GBK");
        String formatted = bytes2HexString(bytes);
        String st = "0101" + formatted;
        //获取字节长度
        int length = st.length() / 2;
        //高位字节
        int mainVersion = (length & 0xFF00) >> 8;
        //低位
        int minorVersion = length & 0xFF;
        String tall = numToHex16(mainVersion);
        String low = numToHex16(minorVersion);
        //包头
        buffer.append("FD");
        //指定长度的高位
        buffer.append(tall);
        //指定长度的低位
        buffer.append(low);
        //流水号
        buffer.append("01");
        //地址
        buffer.append("01");
        buffer.append(formatted);
        return hexToByteArray(buffer.toString());

    }

    /**
     * 取消向led屏幕推送的临时播放信息
     *
     * @return
     */
    public static byte[] cancelLedScreenShowAgreement() throws Exception {

        StringBuilder buffer = new StringBuilder();

        //获取内容字节长度
        int length = content.length() / 2;
        //高位字节
        int mainVersion = (length & 0xFF00) >> 8;
        //低位
        int minorVersion = length & 0xFF;
        String tall = numToHex16(mainVersion);
        String low = numToHex16(minorVersion);
        //包头
        buffer.append(head);
        //流水号
        buffer.append(serial);
        //地址
        buffer.append(site);
        //业务类型
        buffer.append(type);
        //命令
        buffer.append(cancelPushPlayOrder);
        //指定长度的高位
        buffer.append(tall);
        //指定长度的低位
        buffer.append(low);
        buffer.append(content);
        //除去头尾信息用CRC-16/MODBUS算法得到校验字符串
        byte[] bytes1 = hexToByteArray(serial + site + type + cancelPushPlayOrder + tall + low + content + "0000");
        String crcData = getCRCData(bytes1);
        buffer.append(crcData);
        buffer.append(stopCode);
        return hexToByteArray(buffer.toString());

    }

    /**
     * 取消向led屏幕推送的临时播放信息
     *
     * @return
     */
    public static byte[]
    showQRCodeOrder(String imageStr) throws UnsupportedEncodingException {

        if (StringUtils.isEmpty(imageStr)) {
            return null;
        }
        //对二维码内容进行ASCII编码
        byte[] imageStrBytes = imageStr.getBytes();
        String imageHexStr = bytes2HexString(imageStrBytes);
        StringBuilder buffer = new StringBuilder();
        /**
         * 内容包括: 控制字1、控制字2、控制字3、内容,四部分
         * 控制字 1: 二维码显示模式, 有效值 0-2, =0 时表示二维码显示为两行居中模式, 1=二 维码显示为两行居左模式带 4 个汉字(8 个字节), =2 时, 表示二维码显示为三行模式, 两行模式 下, 二维码图象的最大容纳字符为 49 字节, 三行模式下最大容纳字符为 180 字节。
         * 控制字 2: 表示二维码显示的时长, 单位 秒, =0 时, 长期显示。二维码对应的行可以 被“取消临显指令取消”。
         * 控制字 3: 表示二维码本身的颜色 = 1 时红色, =2 时绿色, =3 时黄色, 其他无效
         */
        String content = "02" + "00" + "01" + imageHexStr;
        //获取字节长度
        int length = content.length() / 2;
        //高位字节
        int mainVersion = (length & 0xFF00) >> 8;
        //低位
        int minorVersion = length & 0xFF;
        String tall = numToHex16(mainVersion);
        String low = numToHex16(minorVersion);
        //包头
        buffer.append(head);
        //流水号
        buffer.append(serial);
        //地址
        buffer.append(site);
        //业务类型
        buffer.append(type);
        //命令
        buffer.append(pushQRCodeOrder);
        //指定长度的高位
        buffer.append(tall);
        //指定长度的低位
        buffer.append(low);
        buffer.append(content);
        //除去头尾信息用CRC-16/MODBUS算法得到校验字符串
        byte[] bytes1 = hexToByteArray(serial + site + type + pushQRCodeOrder + tall + low  + content + "0000");
        String crcData = getCRCData(bytes1);
        buffer.append(crcData);
        buffer.append(stopCode);

        return hexToByteArray(buffer.toString());

    }


    /**
     * int 转16进制字符串
     *
     * @param b
     * @return
     */
    public static String numToHex16(int b) {
        return String.format("%02x", b).toUpperCase();
    }

    /**
     * byte数组转16进制字节字符串
     *
     * @param data
     * @return
     */
    public static String bytes2HexString(byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }

        StringBuilder buffer = new StringBuilder();
        for (byte b : data) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                buffer.append('0');
            }
            buffer.append(hex);
        }
        return buffer.toString().toUpperCase();
    }

    /**
     * 计算CRC16/Modbus校验码 低位在后,高位在前
     * @param bytes
     * @return
     */
    public static String getCrcs( byte[] bytes) {

        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        String crc = Integer.toHexString(CRC);
        if(crc.length() == 3) {
            crc = "0" + crc;
        } else if (crc.length() == 2) {
            crc = "00" + crc;
        }else if(crc.length() == 1){
            crc = "000" + crc;
        }
        crc = crc.toUpperCase();
        return crc;
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC).toUpperCase();
    }

    /**
     * Hex转byte[], 两种情况, Hex长度为奇数最后一个字符会被舍去
     */
    public static byte[] hexTobytes(String hex) {
        if (hex.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hex.length() / 2];
            int j = 0;
            for (int i = 0; i < hex.length(); i += 2) {
                result[j++] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            }
            return result;
        }
    }
    //获取校验码
    private static String getCRCData(byte[] b){
        byte[] checkOut = CrcUtil.getCRC(b);
        byte checkByte1 = checkOut[0];
        byte checkByte2 = checkOut[1];
        return getBytestr(checkByte1)+getBytestr(checkByte2);
    }

    private static String getBytestr(byte b){
        return ByteUtils.bytes2HexStr(b);
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }


    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }




    /**
     * 查表法计算CRC16校验
     *
     * @param data 需要计算的字节数组
     */
    public static String getCRC3(byte[] data) {
        byte[] crc16_h = {
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41,
                (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40
        };

        byte[] crc16_l = {
                (byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04,
                (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8,
                (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC,
                (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12, (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10,
                (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4,
                (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38,
                (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C,
                (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7, (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0,
                (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4,
                (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68,
                (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C,
                (byte) 0xB4, (byte) 0x74, (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0,
                (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54,
                (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98,
                (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D, (byte) 0x4C, (byte) 0x8C,
                (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40
        };

        int crc = 0x0000ffff;
        int ucCRCHi = 0x00ff;
        int ucCRCLo = 0x00ff;
        int iIndex;
        for (int i = 0; i < data.length; ++i) {
            iIndex = (ucCRCLo ^ data[i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_h[iIndex];
            ucCRCHi = crc16_l[iIndex];
        }

        crc = ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
        //高低位互换, 输出符合相关工具对Modbus CRC16的运算
        //  crc = ( (crc & 0xFF00) >> 8) | ( (crc & 0x00FF ) << 8);
        return String.format("%04X", crc);
    }
}
