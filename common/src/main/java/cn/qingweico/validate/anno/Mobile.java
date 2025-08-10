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
public @interface Mobile {

    String regExp() default "1[0-9]{10}";

    String error() default "{0}不是一个有效的手机号码格式";

}
