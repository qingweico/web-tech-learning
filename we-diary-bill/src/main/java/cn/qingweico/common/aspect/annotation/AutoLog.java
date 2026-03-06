package cn.qingweico.common.aspect.annotation;

import cn.qingweico.common.constant.CommonConstant;

import java.lang.annotation.*;

/**
 * @author zhouqingwei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

    /**
     * 日志内容
     */
    String value() default "";

    /**
     * 日志类型
     *
     * @return 0:操作日志;1:登录日志;
     */
    int logType() default CommonConstant.LOG_TYPE_2;
}
