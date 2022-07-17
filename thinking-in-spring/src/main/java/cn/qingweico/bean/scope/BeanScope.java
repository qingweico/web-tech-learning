package cn.qingweico.bean.scope;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zqw
 * @date 2021/11/20
 */
public class BeanScope implements DisposableBean {


    @Resource(name = "singletonUser")
    private User si1;
    @Resource(name = "singletonUser")
    private User si2;
    @Resource(name = "prototypeUser")
    private User proto1;
    @Resource(name = "prototypeUser")
    private User proto2;
    @Resource(name = "prototypeUser")
    private User proto3;
    @Autowired
    private Map<String, User> userMap;
    @Autowired
    private ConfigurableListableBeanFactory beanFactory;


    @Bean
    public User singletonUser() {
        return createUser();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public User prototypeUser() {
        return createUser();
    }

    private User createUser() {
        User user = new User();
        user.setId(System.nanoTime());
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BeanScope.class);
        context.addBeanFactoryPostProcessor(beanFactory -> beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        }));
        context.refresh();
        // singleton 无论是依赖查找还是依赖注入 均为同一个对象
        beanScopeByLookup(context);
        beanScopeByLookup(context);
        beanScopeByLookup(context);

        // prototype  无论是依赖查找还是依赖注入 均为新建的对象
        beanScopeByInjection(context);
        context.close();
    }

    private static void beanScopeByInjection(BeanFactory beanFactory) {
        BeanScope cls = beanFactory.getBean(BeanScope.class);
        System.out.println("################Injection################");
        System.out.println(cls.si1);
        System.out.println(cls.si2);
        System.out.println(cls.proto1);
        System.out.println(cls.proto2);
        System.out.println(cls.proto3);
        System.out.println(cls.userMap);
    }

    private static void beanScopeByLookup(BeanFactory beanFactory) {
        System.out.println("################Lookup################");
        // singleton 共享 同一个 Bean 对象
        User singleton = beanFactory.getBean("singletonUser", User.class);
        System.out.println("singleton: " + singleton);
        // prototype 每次依赖查找均生成一个新的对象
        User prototype = beanFactory.getBean("prototypeUser", User.class);
        System.out.println("prototype: " + prototype);
    }

    @Override
    public void destroy() {
        System.out.println("BeanScope Bean destroying...");
        this.proto1.destroy();
        this.proto2.destroy();
        this.proto3.destroy();

        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (beanDefinition.isPrototype()) {
                User user = entry.getValue();
                user.destroy();
            }
        }
        System.out.println("BeanScope Bean finish destroy...");
    }
}
