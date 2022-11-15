package cn.qingweico.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zqw
 * @date 2022/1/24
 * @see ApplicationListener
 */
@EnableAsync
public class ApplicationListenerExample implements ApplicationEventPublisherAware {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationListenerExample.class);
        applicationContext.register(ContextRefreshedEventListener.class);
        applicationContext.register(AsyncConfig.class);
        // 基于 Spring 接口向 Spring 应用上下文注册事件
        applicationContext.addApplicationListener(ApplicationListenerExample::printf);
        applicationContext.refresh();
        applicationContext.start();
        applicationContext.close();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        applicationEventPublisher.publishEvent(new ApplicationEvent("ApplicationEvent message") {
        });
        applicationEventPublisher.publishEvent("Object message");
    }

    static class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.println("Spring Bean: ContextRefreshedEventListener: " + event);
        }
    }

    // 基于 Spring 注解向 Spring 应用上下文注册事件

    @EventListener
    @Order(1)
    public void onApplicationEvent(ApplicationEvent event) {
        printf("@EventListener: " + event);
    }

    // 可以分开; 支持多 ApplicationEvent 类型;
    // @Order中value越小优先级越高

    @EventListener
    @Order(2)
    public void onApplicationEventOrder2(ContextRefreshedEvent event) {
        printf("@EventListener - ContextRefreshedEvent - @Order(2): " + event);
    }

    @EventListener
    @Order(1)
    public void onApplicationEventOrder1(ContextRefreshedEvent event) {
        printf("@EventListener - ContextRefreshedEvent - @Order(1): " + event);
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextStartedEvent event) {
        printf("@EventListener - ContextStartedEvent: " + event);
    }

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event) {
        printf("@EventListener - ContextClosedEvent: " + event);
    }

    private static void printf(Object o) {
        System.out.printf("[Thread: %s]: %s%n", Thread.currentThread().getName(), o);
    }
}
