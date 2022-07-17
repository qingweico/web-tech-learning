package cn.qingweico.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author zqw
 * @date 2022/1/25
 */
public class ApplicationListenerExtension<E extends ApplicationEvent> implements ApplicationListener<E> {
    @Override
    public void onApplicationEvent(E event) {
        System.out.printf("[Thread: %s] [Event: %s] %n", Thread.currentThread().getName(),
                event);
    }
}
