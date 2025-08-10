package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@ValidateAnnotation(className = "cn.qingweico.validator.RegExpressFieldValidator")
public @interface Tel {

    String regExp() default "(\\d{3,4}-?)?\\d{7,9}(-\\d{3,4})?";

    String error() default "{0}不是一个有效的固定电话格式";

}
