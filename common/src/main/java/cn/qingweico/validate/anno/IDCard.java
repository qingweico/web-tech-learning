package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.RegExpressFieldValidator")
public @interface IDCard {

    String regExp() default "(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)";

    String error() default "{0}不是一个有效的身份证格式";

}
