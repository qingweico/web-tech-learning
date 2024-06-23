package cn.qingweico.config;

import cn.qingweico.aop.RateLimiterAop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2024/6/22
 */
@Slf4j
@Configuration
public class RateLimiterAopConfig {

    @Bean
    public RateLimiterAop rateLimiter(){
        return new RateLimiterAop();
    }
}
