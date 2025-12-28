package cn.qingweico.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * @author zqw
 * @date 2025/11/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SemaphoreLimit {
    // 实例名称
    String name() default "default";
    // 每次申请/释放的许可数
    int permits() default 1;
    // 允许的最大并发
    int maxConcurrent() default 10;
    // 0表示立即返回 大于0表示等待毫秒数
    int timeout() default 0;
    // 过期时间
    long ttl() default 0L;
    // 过期时间单位
    ChronoUnit unit() default ChronoUnit.SECONDS;
    // 提示文案
    String message() default "系统繁忙, 请稍后重试";
}
