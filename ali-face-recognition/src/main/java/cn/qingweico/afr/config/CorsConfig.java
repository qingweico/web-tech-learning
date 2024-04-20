package cn.qingweico.afr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zqw
 * @date 2024/1/27
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * 页面跨域访问Controller过滤
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "DELETE", "PUT")
                .allowedOrigins("*");
    }
}
