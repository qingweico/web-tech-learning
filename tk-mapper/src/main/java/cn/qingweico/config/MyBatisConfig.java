package cn.qingweico.config;

import cn.qingweico.interceptor.PkGeneratorInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public Interceptor pkGeneratorInterceptor() {
        return new PkGeneratorInterceptor();
    }
}
