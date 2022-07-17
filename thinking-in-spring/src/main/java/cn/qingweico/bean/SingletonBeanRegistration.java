package cn.qingweico.bean;

import cn.qingweico.bean.factory.DefaultUserFactory;
import cn.qingweico.bean.factory.UserFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * --------------- 外部单体对象注册 Spring Bean ---------------
 *
 * @author zqw
 * @date 2021/11/12
 */
public class SingletonBeanRegistration {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // 创建一个外部对象
        UserFactory userFactory = new DefaultUserFactory();
        SingletonBeanRegistry beanFactory = applicationContext.getBeanFactory();
        // 注册外部单例对象
        beanFactory.registerSingleton("userFactory", userFactory);
        applicationContext.refresh();
        UserFactory userFactoryByLookup = applicationContext.getBean("userFactory", UserFactory.class);
        // true
        System.out.println(userFactory == userFactoryByLookup);
        applicationContext.close();
    }
}
