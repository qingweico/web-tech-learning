package cn.qingweico.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zqw
 * @date 2025/8/20
 */
@Slf4j
// @Configuration
@ConditionalOnProperty({"redis.sentinel.ips", "redis.sentinel.master"})
public class RedisConfig {
    @Value("${redis.sentinel.master:null}")
    private String master;
    @Value("${redis.sentinel.password:null}")
    private String password;
    @Value("${redis.sentinel.ips:null}")
    private String[] ips;

    @Value("${redis.sentinel.ips:null}")
    private String nodes;
    @Value("${redis.pool.maxTotal:null}")
    private Integer maxTotal;
    @Value("${redis.pool.maxIdle:null}")
    private Integer maxIdle;
    @Value("${redis.pool.minIdle:null}")
    private Integer minIdle;
    @Value("${redis.pool.testOnBorrow:null}")
    private Boolean testOnBorrow;

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config cfg = new Config();
        cfg.setCodec(new JsonJacksonCodec());
        for (int i = 0; i < ips.length; i++) {
            ips[i] = "redis://" + ips[i];
        }
        SentinelServersConfig config = cfg.useSentinelServers();
        config.addSentinelAddress(ips);
        config.setScanInterval(2000);
        config.setPassword(password);
        config.setMasterName(master);
        config.setReadMode(ReadMode.MASTER);
        return Redisson.create(cfg);
    }


    @Bean("redisSentinelConfiguration")
    public RedisSentinelConfiguration getRedisSentinelConfiguration() {
        Set<String> redisNodes = new HashSet<>(Arrays.asList(nodes.split(",")));
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration(master, redisNodes);
        configuration.setPassword(password);
        return configuration;
    }

    @Bean("objectPoolConfig")
    public GenericObjectPoolConfig<Void> getObjectPoolConfig() {
        GenericObjectPoolConfig<Void> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }

    @Bean("lettuceClientConfiguration")
    public LettuceClientConfiguration getLettuceClientConfiguration(@Autowired GenericObjectPoolConfig<Void> poolConfig) {
        // 创建Lettuce组件的连接池客户端配置对象
        return LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();
    }

    @Bean("redisConnectionFactory")
    public RedisConnectionFactory getConnectionFactory(@Autowired RedisSentinelConfiguration redisSentinelConfiguration, @Autowired LettuceClientConfiguration lettuceClientConfiguration) {
        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(@Autowired RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 数据的key通过字符串存储
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 保存的value为对象
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        // 数据的key通过字符串存储
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 保存的value为对象
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate getStringRedisTemplate(@Autowired RedisConnectionFactory connectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
