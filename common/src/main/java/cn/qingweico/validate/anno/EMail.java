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
public @interface EMail {

    String regExp() default "\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+";

    String error() default "{0}不是一个有效的EMail";
}
