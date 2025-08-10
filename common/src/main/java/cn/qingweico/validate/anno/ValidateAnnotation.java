package cn.qingweico.validate.anno;

import cn.qingweico.validate.IValidator;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ValidateAnnotation {

    /**
     * 该元注解标记的验证注解所用的校验器
     */
    Class<? extends IValidator>[] value() default {};

    /**
     * 该元注解标记的验证注解所用的校验器完整类名(该类需实现IFieldValidator接口)
     */
    String[] className() default {};

    /**
     * 该元注解标记的验证注解是否需要校验null字段或对象
     */
    boolean nullable() default false;

}
