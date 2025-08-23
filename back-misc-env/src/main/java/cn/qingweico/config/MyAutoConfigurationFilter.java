package cn.qingweico.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * {@code AutoConfigurationImportFilter} 是 Spring Boot 自动配置机制中的一个重要接口
 * 其允许在自动配置类被加载之前进行过滤和筛选, 通过实现 {@link AutoConfigurationImportFilter }接口,
 * 可以根据特定条件决定是否应该导入某个自动配置类
 *
 * @author zqw
 * @date 2025/8/22
 */
public class MyAutoConfigurationFilter implements AutoConfigurationImportFilter, EnvironmentAware {
    private Environment environment;

    @Override
    public void setEnvironment(@NotNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean[] match(String[] autoConfigurationClasses,
                           AutoConfigurationMetadata autoConfigurationMetadata) {

        boolean[] matches = new boolean[autoConfigurationClasses.length];

        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            String autoConfigurationClass = autoConfigurationClasses[i];

            if (autoConfigurationClass == null) {
                matches[i] = false;
                continue;
            }

            matches[i] = shouldInclude(autoConfigurationClass);
        }

        return matches;
    }

    private boolean shouldInclude(String autoConfigurationClass) {

        if (autoConfigurationClass.contains("DataSource") &&
                !environment.getProperty("spring.datasource.enabled", Boolean.class, true)) {
            return false;
        }

        return !autoConfigurationClass.contains("Redis") ||
                environment.containsProperty("spring.redis.host");
    }

    @SuppressWarnings("unused")
    private boolean isAutoConfigurationAllowed(String autoConfigurationClass) {

        if (autoConfigurationClass.contains("Jpa") &&
                !isClassPresent("javax.persistence.Entity")) {
            return false;
        }

        return !autoConfigurationClass.contains("Mongo") ||
                isClassPresent("com.mongodb.client.MongoClient");
    }

    private boolean isClassPresent(String className) {
        try {
            Class.forName(className, false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
