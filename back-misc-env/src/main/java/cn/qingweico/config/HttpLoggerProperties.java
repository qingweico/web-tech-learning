package cn.qingweico.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2025/8/29
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "http.logger")
public class HttpLoggerProperties {
    private boolean head = false;
    private MediaType[] mediaTypes = new MediaType[0];
}
