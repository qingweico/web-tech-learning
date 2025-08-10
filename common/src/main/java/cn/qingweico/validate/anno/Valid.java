package cn.qingweico.validate.anno;

import cn.qingweico.validate.IValidator;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ValidateAnnotation(className = "cn.qingweico.validator.CustomValidFieldValidator")
public @interface Valid {


    /**
     * 要用于字段验证的接口,需实现IValidator
     * 该Class必须有实例被注入到Spring上下文
     */
    Class<? extends IValidator> value();

    /**
     * 验证数据错误时显示的内容
     * 可使用占位符
     * {0}表示字段名或Name注解名
     */
    String error() default "{0}的格式不符合要求";

}
