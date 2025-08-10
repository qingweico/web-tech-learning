package cn.qingweico.validate.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValidateAnnotation(className = "cn.qingweico.validator.InFieldValidator")
public @interface In {

    /**
     * 取值集合
     * 若字段类型为数值/金额,将自动转换为字符串后比较
     */
    String[] value();

}
