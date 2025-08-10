package cn.qingweico.lock;

import cn.qingweico.concurrent.lock.IDistributedLocker;
import cn.qingweico.entity.Locker;
import cn.qingweico.mapper.LockerMapper;
import cn.qingweico.model.BusinessException;
import cn.qingweico.model.BaseErrorCode;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zqw
 * @date 2025/8/2
 */
public class DbLocker implements IDistributedLocker {
    @Autowired
    LockerMapper lockerMapper;
    private final int interval = 200;
    ThreadLocal<Map<String, String>> keyMapThreadLocal = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void lock(String name, long leaseTime) throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            boolean success = this.tryLock(name, leaseTime);
            if (success) {
                return;
            }

            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException var6) {
                throw var6;
            }
        }

        throw new InterruptedException();
    }

    @Override
    public boolean tryLock(String name, long leaseTimeMs) {
        try {
            this.lockerMapper.deleteOutOfDate(name, new Date());
        } catch (Exception var9) {
            throw new BusinessException(BaseErrorCode.SYSTEM_ERROR, "获取锁[" + name + "]失败", var9);
        }
        String key = this.keyMapThreadLocal.get().get(name);
        if (StringUtil.isNotEmpty(key)) {
            int count;
            try {
                count = this.lockerMapper.increaseByCondition(name, key, new Date((new Date()).getTime() + leaseTimeMs));
            } catch (Exception var8) {
                throw new BusinessException(BaseErrorCode.SYSTEM_ERROR, "获取锁[" + name + "]失败", var8);
            }

            if (count > 0) {
                return true;
            }

            this.keyMapThreadLocal.get().remove(name);
        }

        Locker locker = new Locker();
        locker.setCreateTime(new Date());
        locker.setLimitTime(new Date(locker.getCreateTime().getTime() + leaseTimeMs));
        locker.setLockCount(1);
        locker.setLockKey(UUID.randomUUID().toString());
        locker.setLockName(name);

        try {
            this.lockerMapper.insert(locker);
            (this.keyMapThreadLocal.get()).put(name, locker.getLockKey());
            return true;
        } catch (Exception var7) {
            return false;
        }
    }

    @Override
    public boolean tryLock(String name, long waitMs, long leaseTimeMs) throws InterruptedException {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - waitMs < start) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            boolean success = this.tryLock(name, leaseTimeMs);
            if (success) {
                return true;
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException();
            }
        }

        return false;
    }

    @Override
    public void unlock(String name) {
        String key = (this.keyMapThreadLocal.get()).get(name);
        if (StringUtil.isNotEmpty(key)) {
            if (this.lockerMapper.reduceByCondition(name, key) > 0) {
                if (this.lockerMapper.deleteByCondition(name, key, new Date(), 1) > 0) {
                    (this.keyMapThreadLocal.get()).remove(name);
                }
            } else {
                (this.keyMapThreadLocal.get()).remove(name);
            }
        }
    }
}
