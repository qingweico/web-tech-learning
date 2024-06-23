package cn.qingweico.annotation;

import cn.qingweico.enums.InterceptAction;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2024/6/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AccessInterceptor {
    /** 拦截 Key */
    String key() default "";

    /** 限制频次(每秒请求次数) */
    double permitsPerSecond();

    /** 黑名单拦截(多少次限制后加入黑名单) 0 不限制 */
    double blacklistCount() default 0;

    /** 拦截后的执行方法 */
    String fallbackMethod();

    InterceptAction type() default InterceptAction.CUSTOMER;

}
