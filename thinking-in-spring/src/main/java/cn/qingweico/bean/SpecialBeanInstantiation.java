package cn.qingweico.bean;

import cn.qingweico.bean.factory.DefaultUserFactory;
import cn.qingweico.bean.factory.UserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ServiceLoader;

/**
 * --------------- 特殊方式实例化 Bean ---------------
 * {@link ServiceLoader}
 *
 * @author zqw
 * @date 2021/11/11
 */
@Slf4j
public class SpecialBeanInstantiation {
    static ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/bean-special-instantiation-context.xml");

    public static void main(String[] args) {
        // ServiceLoaderFactoryBean
        // 使用 ApplicationContext 和 ServiceLoader 两种实现方式一样;区别就是:
        // 如果有多个则会输出一个
        @SuppressWarnings("unchecked") ServiceLoader<UserFactory> serviceLoader = context.getBean("userFactoryServiceLoader", ServiceLoader.class);
        // 加载 META-INF.services;如果有多个则会全部输出
        ServiceLoader<UserFactory> service = ServiceLoader.load(UserFactory.class, Thread.currentThread().getContextClassLoader());
        displayServiceLoader(serviceLoader, "ApplicationContext");
        displayServiceLoader(service, "ServiceLoader");

        // AutowireCapableBeanFactory
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        // TODO InitializingBean#afterPropertiesSet(): userFactory init... why?
        UserFactory userFactory = beanFactory.createBean(DefaultUserFactory.class);
        // TODO @Value 读不到值
        log.info("{}: {}", "AutowireCapableBeanFactory", userFactory.createUser());
    }

    public static void displayServiceLoader(ServiceLoader<UserFactory> serviceLoader, String name) {
        for (UserFactory userFactory : serviceLoader) {
            log.info("{}: {}", name, userFactory.createUser());
        }
    }
}
