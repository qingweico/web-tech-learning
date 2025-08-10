package cn.qingweico.lock;

import cn.qingweico.concurrent.lock.IDistributedLocker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zqw
 * @date 2025/8/2
 */
public class RedissonLocker implements IDistributedLocker {
    @Autowired
    RedissonClient redissonClient;

    ThreadLocal<Map<String, RLock>> keyMapThreadLocal = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void lock(String name, long leaseTime) throws InterruptedException {
        RLock lock = redissonClient.getLock(name);
        lock.lockInterruptibly(leaseTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(String name, long leaseTimeMs) {
        try {
            return tryLock(name, 100, leaseTimeMs);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean tryLock(String name, long waitMs, long leaseTimeMs) throws InterruptedException {
        RLock lock = redissonClient.getLock(name);
        boolean success = lock.tryLock(waitMs, leaseTimeMs, TimeUnit.MILLISECONDS);
        if (success) {
            keyMapThreadLocal.get().put(name, lock);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unlock(String name) {
        RLock lock = keyMapThreadLocal.get().get(name);
        if (lock != null) {
            lock.unlock();
        }
        if (lock != null && !lock.isLocked()) {
            keyMapThreadLocal.get().remove(name);
        }
    }


}
