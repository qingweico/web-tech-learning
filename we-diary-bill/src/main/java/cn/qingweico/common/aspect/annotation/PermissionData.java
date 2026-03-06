package cn.qingweico.common.aspect.annotation;

import java.lang.annotation.*;


/**
 * @author zhouqingwei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface PermissionData {
    String value() default "";

    String pageComponent() default "";
}
