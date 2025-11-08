package cn.qingweico.config;

import cn.qingweico.concurrent.pool.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutorService;

/**
 * @author zqw
 * @date 2025/11/8
 */
@Configuration
public class CustomizableThreadPoolConfig {
    @Bean
    @Primary
    public ExecutorService customizableThreadPool() {
        return ThreadPoolBuilder.builder(100)
                .corePoolSize(10)
                .maxPoolSize(100)
                .preStartAllCore(true)
                .allowCoreThreadTimeOut(true)
                .build();
    }
}
