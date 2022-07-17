package cn.qingweico.event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author zqw
 * @date 2022/1/25
 */
public class InjectingApplicationEventPublisher implements ApplicationEventPublisherAware,
        ApplicationContextAware, BeanPostProcessor {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        applicationEventPublisher.publishEvent(new ApplicationEventExtension("[@Autowired ApplicationEventPublisher]"));
        applicationContext.publishEvent(new ApplicationEventExtension("[@Autowired ApplicationContext]"));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        applicationEventPublisher.publishEvent(new ApplicationEventExtension("[ApplicationEventPublisherAware callback]"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.publishEvent(new ApplicationEventExtension("[ApplicationContextAware callback]"));

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(InjectingApplicationEventPublisher.class);
        applicationContext.addApplicationListener(new ApplicationListenerExtension<ApplicationEventExtension>());
        applicationContext.refresh();
        applicationContext.close();
    }
}
