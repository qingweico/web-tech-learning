package cn.qingweico.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author zqw
 * @date 2025/8/9
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {
    private final RedissonProperties redissonProperties;

    public RedissonAutoConfiguration(RedissonProperties redissonProperties) {
        this.redissonProperties = redissonProperties;
    }

    /**
     * 单机模式
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.mode", havingValue = "single")
    public RedissonClient singleRedissonClient() {
        Config config = new Config();
        // 使用 Jackson JSON 编解码器(Redisson默认使用Java序列化来存储对象)
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer()
                .setAddress("redis://" + redissonProperties.getHost() + ":" + redissonProperties.getPort())
                .setDatabase(redissonProperties.getDatabase())
                .setTimeout(redissonProperties.getTimeout())
                .setConnectionPoolSize(redissonProperties.getPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getMinIdleSize());
        if (!StringUtils.isEmpty(redissonProperties.getPassword())) {
            config.useSingleServer().setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 集群模式
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.mode", havingValue = "cluster")
    public RedissonClient clusterRedissonClient() {
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress(redissonProperties.getClusterNodes().split(","))
                .setScanInterval(redissonProperties.getScanInterval())
                .setReadMode(ReadMode.valueOf(redissonProperties.getReadMode()))
                .setSubscriptionMode(SubscriptionMode.valueOf(redissonProperties.getSubscriptionMode()));
        if (!StringUtils.isEmpty(redissonProperties.getPassword())) {
            config.useSingleServer().setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 哨兵模式
     * 其需要专门的 Sentinel 进程来监控 Redis 主从节点
     * 单机 Redis 不支持 Sentinel 命令
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.mode", havingValue = "sentinel")
    public RedissonClient sentinelRedissonClient() {
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName(redissonProperties.getMasterName())
                .addSentinelAddress(redissonProperties.getSentinelAddresses())
                .setDatabase(redissonProperties.getDatabase())
                .setTimeout(redissonProperties.getTimeout());
        if (!StringUtils.isEmpty(redissonProperties.getPassword())) {
            config.useSingleServer().setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 主从模式
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.mode", havingValue = "master-slave")
    public RedissonClient masterSlaveRedissonClient() {
        Config config = new Config();
        config.useMasterSlaveServers()
                .setMasterAddress("redis://" + redissonProperties.getMasterAddress())
                .addSlaveAddress(Arrays.stream(redissonProperties.getSlaveAddresses().split(","))
                        .map(addr -> "redis://" + addr)
                        .toArray(String[]::new))
                .setDatabase(redissonProperties.getDatabase())
                .setPassword(redissonProperties.getPassword())
                .setTimeout(redissonProperties.getTimeout());
        return Redisson.create(config);
    }
}
