package cn.qingweico.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2025/11/8
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) throws BeansException {
        CONTEXT = context;
    }

    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> tClass) {
        return CONTEXT.getBean(beanName, tClass);
    }

    public static <T> T getBean(Class<T> tClass) {
        return CONTEXT.getBean(tClass);
    }

    public static ApplicationContext getApplicationContext() {
        return CONTEXT;
    }
}
