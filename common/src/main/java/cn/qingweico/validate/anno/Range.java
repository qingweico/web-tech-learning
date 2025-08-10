package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.RangeFieldValidator")
public @interface Range {
    /**
     * 数字的最小值
     */
    double min();

    /**
     * 数字的最大值
     */
    double max();

    /**
     * 验证数据错误时显示的内容
     * 可使用占位符
     * {0}表示字段名或Name注解名
     * {1}表示min
     * {2}表示max
     */
    String error() default "{0}的大小必须间于{1}与{2}之间";
}
