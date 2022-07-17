package cn.qingweico.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.TaskExecutor;

/**
 * @author zqw
 * @date 2022/1/25
 */
public class AsyncEventHandler {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.addApplicationListener(new ApplicationListenerExtension<ApplicationEventExtension>());
        applicationContext.addApplicationListener((ApplicationListener<ApplicationEventExtension>) event -> {
            throw new RuntimeException("exception event");
        });
        applicationContext.register(AsyncConfig.class);
        applicationContext.refresh();

        ApplicationEventMulticaster applicationEventMulticaster = applicationContext.
                getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                        ApplicationEventMulticaster.class);

        TaskExecutor taskExecutor = applicationContext.getBean("taskExecutor", TaskExecutor.class);

        if (applicationEventMulticaster instanceof SimpleApplicationEventMulticaster) {
            SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = (SimpleApplicationEventMulticaster)
                    applicationEventMulticaster;
            simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor);
            simpleApplicationEventMulticaster.setErrorHandler(e -> System.err.println(e.getMessage()));
        }
        applicationContext.publishEvent(new ApplicationEventExtension("async Spring event"));
        applicationContext.close();
    }
}
