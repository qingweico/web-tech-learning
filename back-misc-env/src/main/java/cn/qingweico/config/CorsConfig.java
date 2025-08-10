package cn.qingweico.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2025/8/2
 */

@Data
@Configuration
@ConfigurationProperties("http.cors")
public class CorsConfig {
    private boolean enable;

    private String pathPattern = "/**";

    private String[] allowedOrigins = new String[]{"*"};

    private String[] allowedMethods = new String[]{"POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH"};

    private String[] allowedHeaders = new String[0];

    private String[] exposeHeaders = new String[0];

    private long maxAge = 3600;

    private boolean allowCredentials = true;
}
