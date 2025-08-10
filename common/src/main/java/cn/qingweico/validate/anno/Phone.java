package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className ="cn.qingweico.validator.RegExpressFieldValidator")
public @interface Phone {

    String regExp() default "(1[0-9]{10})|((\\d{3,4}-?)?\\d{7,9}(-\\d{3,4})?)";

    String error() default "{0}不是一个有效的电话格式";

}
