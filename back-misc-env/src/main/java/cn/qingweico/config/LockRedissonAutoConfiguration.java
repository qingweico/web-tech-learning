package cn.qingweico.config;

import cn.qingweico.concurrent.lock.IDistributedLocker;
import cn.qingweico.lock.RedissonLocker;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2025/8/2
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnMissingBean(IDistributedLocker.class)
public class LockRedissonAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedissonLocker redissonLocker() {
        return new RedissonLocker();
    }
}
