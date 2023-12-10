package cn.qingweico.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zqw
 * @date 2023/11/6
 */
@Data
@ConfigurationProperties(prefix = "cache.redis")
public class CacheRedisProperties {
    String host;
    String password;
    Integer database;
    Integer port;
    /**
     * 序列化方式
     */
    String serializer;
}
