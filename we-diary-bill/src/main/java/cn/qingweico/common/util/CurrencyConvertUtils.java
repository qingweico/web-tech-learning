package cn.qingweico.common.util;

import cn.qingweico.common.constant.JddConstant;

/**
 * @author : jdd孙庆伟
 * @date : Created in 2021/10/20 19:02
 * @description: 
 * @modified By: `
 * @version: 1.0
 */
public class CurrencyConvertUtils {

    public final static String CN_NUM = "0123456789";
    public final static String CN_CCY = "分角整元拾百千万拾百千亿拾百千";

    public static String numToChinese(String input) {
        if (input.equals(JddConstant.StringNumber.STRING_ZERO)) {
            return JddConstant.StringNumber.STRING_ZERO;
        }
        String reg = "^\\d{0,9}\\.?+\\d{0,2}$";
        if (!input.trim().matches(reg)) {
            throw new NumberFormatException("Invalid digit format!");
        }
        String temp = input.trim();
        String result = "";
        int len = 0;
        if (temp.indexOf(".") == -1) {
            len = temp.length();
        } else {
            len = temp.indexOf(".");
        }
        int n1;
        String num = "";
        String unit = "";
        for (int i = 0; i < temp.length(); i++) {
            if (i > len + 2) {
                break;
            }
            if (i == len) {
                continue;
            }
            n1 = Integer.parseInt(String.valueOf(temp.charAt(i)));
            num = CN_NUM.substring(n1, n1 + 1);
            n1 = len - i + 2;
            unit = CN_CCY.substring(n1, n1 + 1);
            result = result.concat(num).concat(unit);
        }
        return formatFinalResult(result);
    }

    private static String formatFinalResult(String money) {
        StringBuffer sb = new StringBuffer(money);
        money = sb.reverse().toString();
        boolean isFormatted = false;
        while (!isFormatted) {
            char digit = money.charAt(1);
            if (digit == '0') {
                money = money.substring(2);
            } else {
                isFormatted = true;
            }
        }
        sb = new StringBuffer(money);
        return sb.reverse().toString();
    }
}
