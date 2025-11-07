package cn.qingweico.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

/**
 * 为 RedisTemplate 和 StringRedisTemplate 提供配置
 *
 * @author zqw
 * @date 2025/8/20
 */
@Slf4j
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private Integer port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.database}")
    private Integer database;
    @Value("${redis.pool.maxTotal:null}")
    private Integer maxTotal;
    @Value("${redis.pool.maxIdle:null}")
    private Integer maxIdle;
    @Value("${redis.pool.minIdle:null}")
    private Integer minIdle;
    @Value("${redis.pool.testOnBorrow:null}")
    private Boolean testOnBorrow;


    @Bean("objectPoolConfig")
    public GenericObjectPoolConfig<Void> objectPoolConfiga() {
        GenericObjectPoolConfig<Void> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }

    @Bean("lettuceClientConfiguration")
    public LettuceClientConfiguration lettuceClientConfiguration(@Autowired GenericObjectPoolConfig<Void> poolConfig) {
        // 创建Lettuce组件的连接池客户端配置对象
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
    }

    @Bean("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(@Autowired LettuceClientConfiguration lettuceClientConfiguration) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        if (StringUtils.hasText(password)) {
            redisConfig.setPassword(RedisPassword.of(password));
        }
        redisConfig.setDatabase(database);
        return new LettuceConnectionFactory(redisConfig, lettuceClientConfiguration);
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(@Autowired RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // RedisTemplate 会使用 Lettuce 作为底层驱动
        // 当然命令还是使用 Spring 提供的 API(如opsForValue、opsForHash等等)
        // 但不能直接使用Lettuce命令, 除非主动配置 Lettuce Client
        // 就像 RedisClient client = RedisClient.create("xxx"); client.connect().sync();client.connect().async();
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
