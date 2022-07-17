package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author zqw
 * @date 2021/11/8
 */
@Slf4j
public class DependencyInjection {
    static final BeanFactory beanFactory = new ClassPathXmlApplicationContext("META-INF/injection-common-context.xml");

    public static void main(String[] args) {
        // 自定义Bean
        UserRepository userRepository = beanFactory.getBean("userRepository", UserRepository.class);
        log.info("util:list: {}", userRepository.getUsers());
        userRepository = beanFactory.getBean("autoWiringUserRepository", UserRepository.class);
        log.info("auto-wiring: {}", userRepository.getUsers());

        // 容器内置依赖(非Spring Bean对象); 依赖注入
        log.info("userRepository.getBeanFactory(): {}", userRepository.getBeanFactory());

        // false
        log.info("beanFactory == userRepository.getBeanFactory(): {}", userRepository.getBeanFactory() == beanFactory);
        // Exception: no such Bean; 依赖查找
        // System.out.println(beanFactory.getBean(BeanFactory.class));

        ObjectFactory<ApplicationContext> objectFactory = userRepository.getObjectFactory();
        // true
        log.info("beanFactory == objectFactory.getObject(): {}", beanFactory == objectFactory.getObject());

        // 容器内置Bean
        log.info("beanFactory.getBean(Environment.class): {}", beanFactory.getBean(Environment.class));
    }
}
