package cn.qingweico.env;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/2
 */
public class DefaultEnvironmentPostProcess implements EnvironmentPostProcessor, Ordered {
    public static final int DEFAULT_ORDER = Integer.MAX_VALUE - 10000;

    Logger logger = LoggerFactory.getLogger(DefaultEnvironmentPostProcess.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {


        String sourceName = "default-env";
        if (environment.getPropertySources().contains(sourceName)) {
            environment.getPropertySources().remove(sourceName);
        }

        if (!environment.getPropertySources().contains("bootstrap")) {
            Map<String, Object> map = new HashMap<>();
            //Banner来自于 https://www.bootschool.net/ascii 字体ansi-shadow
            map.put("spring.main.banner-mode", "log");
            environment.getPropertySources().addLast(new MapPropertySource(sourceName, map));
        }
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
