package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.DateRangeFieldValidator")
public @interface DateRange {
    /**
     * 日期允许的最小值
     * 日期格式使用format字段定义的值
     */
    String min();

    /**
     * 日期允许的最大值
     * 日期格式使用format字段定义的值
     */
    String max();

    /**
     * 日期的格式(默认为yyyy-MM-dd)
     */
    String format() default "yyyy-MM-dd";

    /**
     * 验证数据错误时显示的内容
     * 可使用占位符
     * {0}表示字段名或Name注解名
     * {1}表示min
     * {2}表示max
     */
    String error() default "{0}的值必须间于{1}与{2}之间";
}
