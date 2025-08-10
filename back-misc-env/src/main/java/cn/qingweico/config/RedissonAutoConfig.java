package cn.qingweico.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author zqw
 * @date 2025/8/9
 */
@AutoConfiguration
@Import(RedissonAutoConfiguration.class)
public class RedissonAutoConfig {
}
