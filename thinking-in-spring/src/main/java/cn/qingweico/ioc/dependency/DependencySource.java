package cn.qingweico.ioc.dependency;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

/**
 * @author zqw
 * @date 2021/11/18
 */
public class DependencySource {

    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    // 依赖注入发生在 postProcessProperties 早于 @PostConstruct
    // AutowiredAnnotationBeanPostProcessor#postProcessProperties
    @PostConstruct
    public void injection() {
        System.out.println(beanFactory == applicationContext);
        System.out.println(beanFactory == applicationContext.getAutowireCapableBeanFactory());
        System.out.println(resourceLoader == applicationContext);
        System.out.println(applicationEventPublisher == applicationContext);
    }

    @PostConstruct
    public void lookup() {
        getBean(BeanFactory.class);
        getBean(ApplicationContext.class);
        getBean(ApplicationEventPublisher.class);
        getBean(ResourceLoader.class);
    }

    public <T> T getBean(Class<T> beaType) {
        try {
            return beanFactory.getBean(beaType);
        } catch (BeansException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DependencySource.class);
        context.refresh();
        context.close();
    }
}
