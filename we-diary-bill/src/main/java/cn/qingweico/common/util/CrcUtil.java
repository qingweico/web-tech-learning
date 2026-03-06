package cn.qingweico.common.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;

public class CrcUtil {

	/**
	 * 为Byte数组做CRC校验, 返回两位
	 *
	 * @param buf(验证的byte数组)
	 * @return
	 */
	public static byte[] getTwoParamCRC(byte[] buf) {
		int checkCode = 0;
		checkCode = crc_16_CCITT_False(buf, buf.length);
		byte[] crcByte = new byte[2];
		crcByte[0] = (byte) ((checkCode >> 8) & 0xff);
		crcByte[1] = (byte) (checkCode & 0xff);
		// 将新生成的byte数组添加到原数据结尾并返回
		return crcByte;
	}

	 /**
     * 为Byte数组最后添加两位CRC校验
     *
     * @param buf(验证的byte数组)
     * @return
     */
    public static byte[] setParamCRC(byte[] buf) {
        int checkCode = 0;
        checkCode = crc_16_CCITT_False(buf, buf.length);
        byte[] crcByte = new byte[2];
        crcByte[0] = (byte) ((checkCode >> 8) & 0xff);
        crcByte[1] = (byte) (checkCode & 0xff);
        // 将新生成的byte数组添加到原数据结尾并返回
        return concatAll(buf, crcByte);
    }

    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     *
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     *
     * @param bytes
     * @param length
     * @return
     */
    public static int crc_16_CCITT_False(byte[] bytes, int length) {
        int crc = 0xffff; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        //输出String字样的16进制
       // String strCrc = Integer.toHexString(crc).toUpperCase();
        //System.out.println("crc_16_CCITT_False 获取数值: " + strCrc);
        return crc;
    }

    /***
     * CRC校验是否通过
     *
     * @param srcByte
     * @param length(验证码字节长度)
     * @return
     */
    public static boolean isPassCRC(byte[] srcByte, int length) {

        // 取出除crc校验位的其他数组, 进行计算, 得到CRC校验结果
        int calcCRC = calcCRC(srcByte, 0, srcByte.length - length);
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((calcCRC >> 8) & 0xff);
        bytes[1] = (byte) (calcCRC & 0xff);

        // 取出CRC校验位, 进行计算
        int i = srcByte.length;
        byte[] b = { srcByte[i - 2] ,srcByte[i - 1] };

        // 比较
        return bytes[0] == b[0] && bytes[1] == b[1];
    }

    /**
     * 对buf中offset以前crcLen长度的字节作crc校验, 返回校验结果
     * @param  buf
     * @param crcLen
     */
    private static int calcCRC(byte[] buf, int offset, int crcLen) {
        int start = offset;
        int end = offset + crcLen;
        int crc = 0xffff; // initial value
        int polynomial = 0x1021;
        for (int index = start; index < end; index++) {
            byte b = buf[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

	/**
     * 多个数组合并
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }


    //将一个字符串转成16进制字节数组
    public static byte[] change(String inputStr) {
	    byte[] result = new byte[inputStr.length() / 2];
	    for (int i = 0; i < inputStr.length() / 2; ++i) {
	    	 result[i] = (byte)(Integer.parseInt(inputStr.substring(i * 2, i * 2 +2), 16) & 0xff);
	    }
	    return result;
    }






    	static byte[] crc16_tab_h = { (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
        (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
        (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
        (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
        (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40 };

    	static byte[] crc16_tab_l = { (byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03, (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5, (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF, (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09, (byte) 0x08, (byte) 0xC8, (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB, (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D, (byte) 0x1C, (byte) 0xDC, (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17, (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12,
        (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1, (byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3, (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35, (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F, (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9, (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B, (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED, (byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
        (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3, (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65, (byte) 0x64, (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F, (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9, (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B, (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD, (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74,
        (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7, (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71, (byte) 0x70, (byte) 0xB0, (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53, (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95, (byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F, (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59, (byte) 0x58, (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B, (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
        (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47, (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81, (byte) 0x80, (byte) 0x40 };

		/**
		 * 计算CRC16校验
		 *
		 * @param data
		 *            需要计算的数组
		 * @return CRC16校验值
		 */
		public static int calcCrc16(byte[] data) {
		    return calcCrc16(data, 0, data.length);
		}

		/**
		 * 计算CRC16校验
		 *
		 * @param data
		 *            需要计算的数组
		 * @param offset
		 *            起始位置
		 * @param len
		 *            长度
		 * @return CRC16校验值
		 */
		public static int calcCrc16(byte[] data, int offset, int len) {
		    return calcCrc16(data, offset, len, 0xffff);
		}

		/**
		 * 计算CRC16校验
		 *
		 * @param data
		 *            需要计算的数组
		 * @param offset
		 *            起始位置
		 * @param len
		 *            长度
		 * @param preval
		 *            之前的校验值
		 * @return CRC16校验值
		 */
		public static int calcCrc16(byte[] data, int offset, int len, int preval) {
		    int ucCRCHi = (preval & 0xff00) >> 8;
		    int ucCRCLo = preval & 0x00ff;
		    int iIndex;
		    for (int i = 0; i < len; ++i) {
		        iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
		        ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
		        ucCRCHi = crc16_tab_l[iIndex];
		    }
		    return ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
		}


		//new 2
		/**
	     * 计算CRC16校验码
	     *
	     * @param bytes 字节数组
	     * @return {@link String} 校验码
	     * @since 1.0
	     */
	    public static byte[] getCRC(byte[] bytes) {
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
	        CRC &= 0xffff;
	        //String temp = Integer.toHexString(CRC).toUpperCase();
	        byte[] crcByte = new byte[2];
	        crcByte[0] = (byte) ((CRC >> 8) & 0xff);
			crcByte[1] = (byte) (CRC & 0xff);
	        return crcByte;
	    }

	    /**
	     * Convert hex string to byte[]
	     * @param hexString the hex string
	     * @return byte[]
	     */
	    public static byte[] hexStringToBytes(String hexString) {
	        if (hexString == null || hexString.equals("")) {
	            return null;
	        }
	        hexString = hexString.toUpperCase();
	        int length = hexString.length() / 2;
	        char[] hexChars = hexString.toCharArray();
	        byte[] d = new byte[length];
	        for (int i = 0; i < length; i++) {
	            int pos = i * 2;
	            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
	        }
	        return d;
	    }

	    /**
	     * Convert char to byte
	     * @param c char
	     * @return byte
	     */
	     private static byte charToByte(char c) {
	        return (byte) "0123456789ABCDEF".indexOf(c);
	    }

	    /**
	     * 将16进制单精度浮点型转换为10进制浮点型
	     *
	     * @return float
	     * @since 1.0
	     */
	    public float parseHex2Float(String hexStr) {
	        BigInteger bigInteger = new BigInteger(hexStr, 16);
	        return Float.intBitsToFloat(bigInteger.intValue());
	    }

	    /**
	     * 将十进制浮点型转换为十六进制浮点型
	     *
	     * @return String
	     * @since 1.0
	     */
	    public String parseFloat2Hex(float data) {
	        return Integer.toHexString(Float.floatToIntBits(data));
	    }

	    public static String stringToHexString(String strPart) {
	        String hexString = "";
	        for (int i = 0; i < strPart.length(); i++) {
	            int ch = (int) strPart.charAt(i);
	            String strHex = Integer.toHexString(ch);
	            hexString = hexString + strHex;
	        }
	        return hexString;
	    }

	    private static String hexString = "0123456789ABCDEF";

	    /*
	     * 将字符串编码成16进制数字,适用于所有字符(包括中文)
	     */
	    public static String encode(String str) {
	        // 根据默认编码获取字节数组
	        byte[] bytes;
	        StringBuilder sb  = null;
			try {
				  bytes = str.getBytes("gbk");

				  sb = new StringBuilder(bytes.length * 2);
			        // 将字节数组中每个字节拆解成2位16进制整数
			        for (int i = 0; i < bytes.length; i++) {
			        	char tmp1 = hexString.charAt((bytes[i] & 0xf0) >> 4);
			        	sb.append(tmp1);
			            char tmp2 = hexString.charAt((bytes[i] & 0x0f) >> 0);
			            sb.append(tmp2);

			        }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			 return sb.toString();
	     }


	  //16进制字符串转换为byte[]
	    public static byte[] HexToByte(String hexString){
	        int len = hexString.length();
	        byte[] b = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            // 两位一组, 表示一个字节,把这样表示的16进制字符串, 还原成一个字节
	            b[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
	                    .digit(hexString.charAt(i + 1), 16));
	        }
	        return b;
	    }


	    /**
	     * 将字符串编码成16进制数字,适用于所有字符(包括中文)
	     */
	    public static String encode(String str, String charset) throws UnsupportedEncodingException {
	        //根据默认编码获取字节数组
	        byte[] bytes=str.getBytes(charset);
	        StringBuilder sb=new StringBuilder(bytes.length*2);
	        //将字节数组中每个字节拆解成2位16进制整数
	        for(int i=0;i<bytes.length;i++){
	            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
	            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
	        }
	        return sb.toString();
	    }


	    /**
	     * 将16进制数字解码成字符串,适用于所有字符(包括中文)
	     */
	    public static String decode(String bytes, String charset) throws UnsupportedEncodingException {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length()/2);
	        //将每2位16进制整数组装成一个字节
	        for(int i=0;i<bytes.length();i+=2)
	            baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
	        return new String(baos.toByteArray(), charset);
	    }


	    /**
	     * byte[]转换为16进制字符串
	     * @param src byte[]数组
	     * @return String
	     */
	    public static String bytesToHexString(byte[] src) {
	        StringBuilder stringBuilder = new StringBuilder("");
	        if ((src == null) || (src.length <= 0)) {
	            return null;
	        }
	        for (int i = 0; i < src.length; ++i) {
	            int v = src[i] & 0xFF;
	            String hv = Integer.toHexString(v);
	            if (hv.length() < 2) {
	                stringBuilder.append(0);
	            }
	            stringBuilder.append(hv);
	        }
	        return stringBuilder.toString();
	    }

	//byte转为16进制字符串

	public static String hexString(byte b) {


	String hex = Integer.toHexString(b & 0xFF);
	if (hex.length() == 1) {
	hex = '0' + hex;
	}
	  return hex;
	}



}
