package cn.qingweico.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 工具类
 * @author:
 * @date: 2022/3/3 13:52
 */
public class BusinessUtils {

    private static final Pattern CHINESE_NAME_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5·]+");

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]*");

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");

    /**
     * 验证小数点后面最多2位的数
     */
    public static boolean isMoney(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        String regMoney = "\\d+(\\.\\d{1,2})?";
        return value.matches(regMoney);
    }

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188,198
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186,146,166
     * 电信号段: 133,149,153,170,173,177,180,181,189 ,199,174,141
     * 虚拟运营商: 1700, 1750, 1707, 1708, 1709
     *
     * @param mobile 待检测的字符串
     * @return 待检测的字符串
     */
    public static boolean checkMobile(String mobile) {
        // "[1]"代表第1位为数字1, "[358]"代表第二位可以为3、5、8中的一个, "\\d{9}"代表后面是可以是0～9的数字, 有9位。
        String telRegex = "^((13[0-9])|(14[1,5,6,7,9])|(15[^4])|(166)|(18[0-9])|(19[8,9])|(17[0,1,3,4,5,6,7,8]))\\d{8}$";
        if (org.apache.commons.lang.StringUtils.isEmpty(mobile)) {
            return false;
        } else {
            return mobile.matches(telRegex);
        }
    }

    /**
     * 银行卡号一般是16位或者19位。
     * 由如下三部分构成。
     * 1,前六位是: 发行者标识代码
     * 2,中间的位数是: 个人账号标识(从卡号第七位开始), 一般由6－12位数字组成。最多可以使用12位数字。
     * 3,最后一位是:根据卡号前面的数字,采用Luhn算法计算出的最后一位校验位
     */
    public static boolean checkBankCard(String cardId) {
        if (cardId == null || cardId.trim().length() == 0) {
            return false;
        }
        if (cardId.length() < 16 || cardId.length() > 19) {
            return false;
        }

        //根据Luhm法则得到校验位
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }

        //和银行卡号的校验位(最后一位)比较,相同为true 不同为false
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 该校验的过程: 
     * 1、从卡号最后一位数字开始, 逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始, 逆向将偶数位数字(0、2、4等等), 先乘以2(如果乘积为两位数, 则将其减去9或个位与十位相加的和), 再求和。
     * 3、将奇数位总和加上偶数位总和, 结果应该可以被10整除。
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        //如果传的不是数字返回N
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        /**
         * 注意是从下标为0处开始的
         */
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            /**
             * 是偶数位数字做处理先乘以2(如果乘积为两位数, 则将其减去9或个位与十位相加的和), 再求和。
             * 不是则不做处理
             */
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 中文姓名校验
     *
     * @param @param  str
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: isChineseName
     */

    public static boolean checkChineseName(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = CHINESE_NAME_PATTERN.matcher(String.valueOf(c[i]));
            if (!matcher.matches()) {
                return false;
            }
        }
        if (str.length() < 2 || str.length() > 21) {
            return false;
        }
        return true;
    }

    /**
     * 身份证有效
     */
    public static final String VALIDITY = "该身份证有效！";
    /**
     * 位数不足
     */
    public static final String LACKDIGITS = "身份证号码长度应该为15位或18位。";
    /**
     * 最后一位应为数字
     */
    public static final String LASTOFNUMBER = "身份证15位号码都应为数字 ; 18位号码除最后一位外, 都应为数字。";
    /**
     * 出生日期无效
     */
    public static final String INVALIDBIRTH = "身份证出生日期无效。";
    /**
     * 生日不在有效范围
     */
    public static final String INVALIDSCOPE = "身份证生日不在有效范围。";
    /**
     * 月份无效
     */
    public static final String INVALIDMONTH = "身份证月份无效";
    /**
     * 日期无效
     */
    public static final String INVALIDDAY = "身份证日期无效";
    /**
     * 身份证地区编码错误
     */
    public static final String CODINGERROR = "身份证地区编码错误。";
    /**
     * 身份证校验码无效
     */
    public static final String INVALIDCALIBRATION = "身份证校验码无效, 不是合法的身份证号码";

    /**
     * 检验身份证号码是否符合规范
     *
     * @param IDStr 身份证号码
     * @return 错误信息或成功信息
     */
    public static boolean checkIDCardValidate(String IDStr) {
        // 记录错误信息
        boolean checked = false;
        String checkedMsg = "";
        String Ai = "";
        // 判断号码的长度 15位或18位
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            checkedMsg = LACKDIGITS;
            return checked;
        }

        // 18位身份证前17位位数字, 如果是15位的身份证则所有号码都为数字
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            checkedMsg = LASTOFNUMBER;
            return checked;
        }

        // 判断出生年月是否有效
        // 年份
        String strYear = Ai.substring(6, 10);
        // 月份
        String strMonth = Ai.substring(10, 12);
        // 日期
        String strDay = Ai.substring(12, 14);
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            checkedMsg = LASTOFNUMBER;
            return checked;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                checkedMsg = INVALIDSCOPE;
                return checked;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            checkedMsg = INVALIDBIRTH;
            return checked;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            checkedMsg = INVALIDDAY;
            return checked;
        }

        // 判断地区码是否有效
        Hashtable<String, String> areacode = GetAreaCode();
        // 如果身份证前两位的地区码不在Hashtable, 则地区码有误
        if (areacode.get(Ai.substring(0, 2)) == null) {
            checkedMsg = CODINGERROR;
            return checked;
        }

        if (isVarifyCode(Ai, IDStr) == false) {
            checkedMsg = INVALIDCALIBRATION;
            return checked;
        }
        return true;
    }

    /**
     * 判断第18位校验码是否正确 第18位校验码的计算方式: 
     * 1. 对前17位数字本体码加权求和 公式为: S = Sum(Ai * Wi), i =
     * 0, ... , 16 其中Ai表示第i个位置上的身份证号码数字值, Wi表示第i位置上的加权因子, 其各位对应的值依次为:  7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     * 2. 用11对计算结果取模 Y = mod(S, 11)
     * 3. 根据模的值得到对应的校验码
     * 对应关系为:  Y值:  0 1 2 3 4 5 6 7 8 9 10 校验码:  1 0 X 9 8 7 6 5 4 3 2
     */
    private static boolean isVarifyCode(String Ai, String IDStr) {
        String[] VarifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = sum % 11;
        String strVerifyCode = VarifyCode[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                return false;

            }
        }
        return true;
    }

    /**
     * 将所有地址编码保存在一个Hashtable中
     *
     * @return Hashtable 对象
     */

    private static Hashtable<String, String> GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 判断字符串是否为数字,0-9重复0次或者多次
     *
     * @param strNum
     * @return true, 符合; false, 不符合。
     */
    public static boolean isNumeric(String strNum) {
        Matcher isNum = NUMERIC_PATTERN.matcher(strNum);
        return isNum.matches();
    }

    /**
     * 功能: 判断字符串出生日期是否符合正则表达式: 包括年月日, 闰年、平年和每月31天、30天和闰月的28天或者29天
     *
     * @param strDate
     * @return true, 符合; false, 不符合。
     */
    public static boolean isDate(String strDate) {
        Matcher m = DATE_PATTERN.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkVehicleNum(String vehicleNum) {
        if (StringUtils.isEmpty(vehicleNum)) {
            return false;
        }
        String plateNumMatch = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";
        return vehicleNum.matches(plateNumMatch);
    }

    public static boolean checkVehicleColor(String vehicleColor) {
        if (StringUtils.isEmpty(vehicleColor)) {
            return false;
        }
        String str = "蓝色,⻩色,⿊色,⽩色,渐变绿,黄绿双拼,蓝白渐变";
        if (str.indexOf(vehicleColor) >= 0) {
            return true;
        } else {
            return false;
        }
    }

}
