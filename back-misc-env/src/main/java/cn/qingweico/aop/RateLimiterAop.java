package cn.qingweico.aop;

import cn.qingweico.annotation.AccessInterceptor;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author zqw
 * @date 2024/6/22
 */
@Aspect
@Component
@Slf4j
public class RateLimiterAop {
    @Pointcut("@annotation(cn.qingweico.annotation.AccessInterceptor)")
    public void aopPoint() {
    }


    // 个人限频记录1分钟
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    // 个人限频黑名单24h
    private final Cache<String, Long> blacklist = CacheBuilder.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build();

    @Around("cn.qingweico.aop.RateLimiterAop.aopPoint() && @annotation(accessInterceptor)")
    public Object doRouter(ProceedingJoinPoint jp, AccessInterceptor accessInterceptor) throws Throwable {
        String key = accessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("annotation RateLimiter key is null！");
        }
        // 获取拦截字段的 value
        String keyAttrValue = getAttrValue(key, jp.getArgs());
        log.info("Intercept key : {}", key);
        log.info("Intercept value : {}", keyAttrValue);
        log.info("Intercept method : {} ", getMethod(jp));
        // 黑名单拦截
        Long presentVal = blacklist.getIfPresent(keyAttrValue);
        if (accessInterceptor.blacklistCount() != 0 && blacklist.getIfPresent(keyAttrValue) != null
                && (presentVal != null && presentVal > accessInterceptor.blacklistCount())) {
            log.warn("限流-黑名单拦截(24h) {}", keyAttrValue);
            return fallbackMethodResult(jp, accessInterceptor.fallbackMethod());
        }

        // 获取限流 -> Guava 缓存1分钟
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttrValue);
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(accessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttrValue, rateLimiter);
        }

        // 限流拦截
        if (!rateLimiter.tryAcquire()) {
            if (accessInterceptor.blacklistCount() != 0) {
                presentVal = blacklist.getIfPresent(keyAttrValue);
                if (presentVal == null) {
                    blacklist.put(keyAttrValue, 1L);
                } else {
                    blacklist.put(keyAttrValue, presentVal + 1L);
                }
            }
            log.info("限流-超频次拦截 {}", keyAttrValue);
            return fallbackMethodResult(jp, accessInterceptor.fallbackMethod());
        }
        // 返回结果
        return jp.proceed();
    }

    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                filedValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("{}", e.getMessage());
            }
        }
        return filedValue;
    }

    private Object getValueByName(Object obj, String name) {
        try {
            Field field = getFieldByName(obj, name);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(obj);
            field.setAccessible(false);
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private Field getFieldByName(Object obj, String name) {
        try {
            Field field;
            try {
                field = obj.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                field = obj.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
