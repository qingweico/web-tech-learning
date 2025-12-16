package cn.qingweico.aop;

import cn.hutool.core.text.StrPool;
import cn.qingweico.annotation.SemaphoreLimit;
import cn.qingweico.exception.SemaphoreLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redisson {@link RSemaphore} 的分布式信号量限流切面
 *
 * @author zqw
 * @date 2025/11/12
 * @see SemaphoreLimit
 */
@Aspect
@Component
public class SemaphoreLimitAspect {
    private final RedissonClient redissonClient;
    private static final String INIT_SEMAPHORE_PREFIX = "semaphore:init:";
    private static final String LOCK_SEMAPHORE_PREFIX = "semaphore:lock:";
    private static final String SEMAPHORE_KEY_PREFIX = "semaphore:limit:";
    // 信号量过期时间(分钟)
    private static final long SEMAPHORE_EXPIRE_MINUTES = 10;
    // 分布式锁获取超时时间(秒)
    private static final long LOCK_WAIT_TIMEOUT_SECONDS = 5;
    // 分布式锁持有时间(秒)
    private static final long LOCK_LEASE_TIME_SECONDS = 10;

    public SemaphoreLimitAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(sl)")
    public Object around(ProceedingJoinPoint joinPoint, SemaphoreLimit sl) throws Throwable {
        String semaphoreName = SEMAPHORE_KEY_PREFIX + sl.name();
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreName);
        // 记录信号量是否已初始化
        String initFlagKey = INIT_SEMAPHORE_PREFIX + sl.name() + StrPool.COLON + sl.maxConcurrent();
        RBucket<Boolean> initFlag = redissonClient.getBucket(initFlagKey);

        // 使用 DLC(双重锁检查) 避免并发重复初始化, 减少不必要的 Redis 操作
        if (initFlag.get() == null) {
            String lockKey = LOCK_SEMAPHORE_PREFIX + sl.name() + StrPool.COLON + sl.maxConcurrent();
            RLock lock = redissonClient.getLock(lockKey);
            boolean lockAcquired = false;
            try {
                lockAcquired = lock.tryLock(LOCK_WAIT_TIMEOUT_SECONDS, LOCK_LEASE_TIME_SECONDS, TimeUnit.SECONDS);
                if (lockAcquired) {
                    if (initFlag.get() == null) {
                        // 设置许可数
                        semaphore.trySetPermits(sl.maxConcurrent());
                        // 避免每次请求都更新过期时间
                        // 防止覆盖掉上次的过期时间, 注意expire方法对不存在的key无效, 先使用trySetPermits()创建信号量再设置过期时间
                        semaphore.expire(Duration.ofMinutes(SEMAPHORE_EXPIRE_MINUTES));
                        // 设置和信号量相同的过期时间
                        initFlag.set(true, Duration.ofMinutes(SEMAPHORE_EXPIRE_MINUTES));
                    }
                }
            } catch (InterruptedException e) {
                // 重置中断状态
                Thread.currentThread().interrupt();
                throw new SemaphoreLimitException(sl.message());
            } finally {
                if (lockAcquired && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        boolean acquired = false;
        try {
            if (sl.timeout() > 0) {
                acquired = semaphore.tryAcquire(sl.permits(), Duration.ofMillis(sl.timeout()));
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
