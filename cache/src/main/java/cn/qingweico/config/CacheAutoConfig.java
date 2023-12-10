package cn.qingweico.config;

import cn.qingweico.client.RedisClient;
import cn.qingweico.client.RedisProperties;
import cn.qingweico.properties.CacheRedisProperties;
import cn.qingweico.serializer.AbstractRedisSerializer;
import cn.qingweico.serializer.StringRedisSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2023/11/6
 */
@Configuration
@EnableConfigurationProperties({CacheRedisProperties.class})
public class CacheAutoConfig {

    @Bean
    public RedisProperties redisProperties(CacheRedisProperties cacheRedisProperties) {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setDatabase(cacheRedisProperties.getDatabase());
        redisProperties.setHost(cacheRedisProperties.getHost());
        redisProperties.setPassword(StringUtils.isBlank(cacheRedisProperties.getPassword()) ? null : cacheRedisProperties.getPassword());
        redisProperties.setPort(cacheRedisProperties.getPort());
        redisProperties.setSerializer(cacheRedisProperties.getSerializer());
        return redisProperties;
    }

    @Bean
    @ConditionalOnMissingBean(RedisClient.class)
    public RedisClient redisClient(RedisProperties redisProperties) throws Exception {
        AbstractRedisSerializer valueRedisSerializer = (AbstractRedisSerializer) Class.forName(redisProperties.getSerializer()).newInstance();
        StringRedisSerializer keyRedisSerializer = new StringRedisSerializer();
        RedisClient redisClient = RedisClient.getInstance(redisProperties);
        redisClient.setKeySerializer(keyRedisSerializer);
        redisClient.setValueSerializer(valueRedisSerializer);
        return redisClient;
    }
}
