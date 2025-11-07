package cn.qingweico.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Spring 应用上下文状态监听器, 主要用于跟踪 Spring 容器
 * 的生命周期事件(如容器刷新完成、容器关闭)
 *
 * @author zqw
 * @date 2025/7/26
 */
public class SpringContextListener implements SmartApplicationListener {
    static final Map<BeanFactory, SpringContextListener> CACHE = new ConcurrentHashMap<>();

    private static final Log log = LogFactory.getLog(SpringContextListener.class);

    final AtomicBoolean refreshed;

    final AtomicBoolean closed;

    public SpringContextListener() {
        this.refreshed = new AtomicBoolean();
        this.closed = new AtomicBoolean();
    }

    SpringContextListener(AtomicBoolean refreshed, AtomicBoolean closed) {
        this.refreshed = refreshed;
        this.closed = closed;
    }

    public static SpringContextListener getBean(BeanFactory beanFactory) {
        return CACHE.getOrDefault(beanFactory, new SpringContextListener());
    }

    @Override
    public boolean supportsEventType(@NotNull Class<? extends ApplicationEvent> eventType) {
        return ContextClosedEvent.class.isAssignableFrom(eventType)
                || ContextRefreshedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent
                || event instanceof ContextClosedEvent) {
            if (log.isDebugEnabled()) {
                log.debug("Context refreshed or closed [" + event + "]");
            }
            ApplicationContextEvent contextEvent = (ApplicationContextEvent) event;
            ApplicationContext context = contextEvent.getApplicationContext();
            BeanFactory beanFactory = context;
            if (context instanceof ConfigurableApplicationContext) {
                beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
            }
            SpringContextListener listener = CACHE.getOrDefault(beanFactory, this);
            listener.refreshed.compareAndSet(false,
                    event instanceof ContextRefreshedEvent);
            listener.closed.compareAndSet(false, event instanceof ContextClosedEvent);
            CACHE.put(beanFactory, listener);
        }
    }

    public boolean isUnusable() {
        return !this.refreshed.get() || this.closed.get();
    }

    public static void reset(BeanFactory beanFactory) {
        CACHE.remove(beanFactory);
    }
}
