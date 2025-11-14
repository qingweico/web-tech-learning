package cn.qingweico.config;

import cn.qingweico.annotation.SemaphoreLimit;
import cn.qingweico.exception.SemaphoreLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zqw
 * @date 2025/11/12
 */
@Aspect
@Component
public class SemaphoreLimitAspect {
    private final RedissonClient redissonClient;

    public SemaphoreLimitAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(sl)")
    public Object around(ProceedingJoinPoint joinPoint, SemaphoreLimit sl) throws Throwable {
        String semaphoreName = "semaphore:limit:" + sl.name();
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreName);
        semaphore.trySetPermits(sl.maxConcurrent());

        boolean acquired = false;
        try {
            if (sl.timeout() > 0) {
                acquired = semaphore.tryAcquire(sl.permits(), sl.timeout(), TimeUnit.MILLISECONDS);
            } else {
                acquired = semaphore.tryAcquire(sl.permits());
            }
            if (!acquired) {
                throw new SemaphoreLimitException(sl.message());
            }

            return joinPoint.proceed();
        } finally {
            if (acquired) {
                semaphore.release(sl.permits());
            }
        }
    }
}
