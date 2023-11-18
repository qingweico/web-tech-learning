package cn.qingweico.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2023/11/11
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String basePackage;

    private String title;

    private String name;

    private String url;

    private String email;

    private String version;

    private Boolean enable;

    private String description;
}
