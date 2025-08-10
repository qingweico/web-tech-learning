package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.MaxFieldValidator")
public @interface Max {
    /**
     * 数字的最大值
     *
     */
    double value();

    /**
     * 验证数据错误时显示的内容
     * 可使用占位符
     * {0}表示字段名或Name注解名
     * {1}表示max
     *
     */
    String error() default "{0}的大小不能超过{1}";
}
