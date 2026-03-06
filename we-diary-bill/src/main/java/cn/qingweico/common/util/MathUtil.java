package cn.qingweico.common.util;


import cn.hutool.core.util.ObjectUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author liuyaowen
 * */
public final class MathUtil {

    /**
     * 加法, 
     * @param number1 加数
     * @param number2 加数
     * @return number1 + number2
     * */
    public static <T> BigDecimal add(T number1,T number2)
    {
        if(ObjectUtil.isEmpty(number1))
        {
            number1 = (T)"0";
        }
        if(ObjectUtil.isEmpty(number2))
        {
            number2 = (T)"0";
        }
        BigDecimal bigDecimal1 = new BigDecimal(number1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(number2.toString());
        return bigDecimal1.add(bigDecimal2);
    }

    /**
     * 减法
     * @param number1 被减数
     * @param number2 减数
     * @return number1 - number2
     * */
    public static <T> BigDecimal subtract(T number1,T number2)
    {
        if(ObjectUtil.isEmpty(number1))
        {
            number1 = (T)"0";
        }
        if(ObjectUtil.isEmpty(number2))
        {
            number2 = (T)"0";
        }
        BigDecimal bigDecimal1 = new BigDecimal(number1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(number2.toString());
        return bigDecimal1.subtract(bigDecimal2);
    }

    /**
     * 乘法
     * @param number1 乘数
     * @param number2 乘数
     * @return number1 * number2
     * */
    public static <T> BigDecimal multiply(T number1,T number2)
    {
        if(ObjectUtil.isEmpty(number1)||ObjectUtil.isEmpty(number2))
        {
            return new BigDecimal("0");
        }

        BigDecimal bigDecimal1 = new BigDecimal(number1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(number2.toString());
        return bigDecimal1.multiply(bigDecimal2);
    }
    /**
     * 除法
     * @param number1 被除数
     * @param number2 除数
     * @return number1 / number2
     * */
    public static <T> BigDecimal divide(T number1,T number2)
    {

        if(ObjectUtil.isEmpty(number1)||ObjectUtil.isEmpty(number2))
        {
            return new BigDecimal("0");
        }
        BigDecimal bigDecimal1 = new BigDecimal(number1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(number2.toString());
        return bigDecimal1.divide(bigDecimal2);
    }

    /**
     * 将数据转换成正整数, 使用四射无如的方式
     * @param number
     * @return
     */
    public static String toInteger(String number){
        if(ObjectUtil.isEmpty(number))
        {
            return "0";
        }
        BigDecimal bigDecimal1 = new BigDecimal(number);
        BigDecimal bigDecimal2 = new BigDecimal(1);
        return bigDecimal1.multiply(bigDecimal2).setScale(0, RoundingMode.HALF_UP).toEngineeringString();
    }
}
