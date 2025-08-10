package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.InEnumValidator")
public @interface InEnum {

    /**
     * 匹配的枚举类型
     * 该Class必须来自于枚举(enum)类,校验器将获取所有枚举值,从而校验当前字段值是否在枚举中
     */
    Class<? extends Enum<?>> value();

}
