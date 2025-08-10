package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.LengthFieldValidator")
public @interface Length {

    /**
     * 字符串的最小长度
     */
    int min() default -1;

    /**
     * 最大长度
     */
    int max();


    /**
     * 验证数据错误时显示的内容
     * 可使用占位符
     * {0}表示字段名或Name注解名
     * {1}表示min
     * {2}表示max
     */
    String error() default "{0}的长度必须介于{1}与{2}之间";
}
