package cn.qingweico.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * 使用 Spring 的 @Async 注解时, 如果没有配置线程池时使用 {@link SimpleAsyncTaskExecutor} 可能会导致OOM(频繁创建线程)
 *
 * @author zqw
 * @date 2025/2/8
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomUncaughtExceptionHandler();
    }


    static class CustomUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(@NonNull Throwable ex, Method method, @NonNull Object... params) {
            log.error("AsyncExecutor Exception >>> Class: '{}'; Method: {}; params: {}", method.getDeclaringClass(), method.getName(), Arrays.stream(params).toArray(), ex);
        }
    }

}
