package cn.qingweico.config;

import cn.qingweico.interceptor.UserInterceptor;
import com.sun.istack.internal.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zqw
 * @date 2024/6/22
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor())
                .addPathPatterns("/user/**");
    }

}
