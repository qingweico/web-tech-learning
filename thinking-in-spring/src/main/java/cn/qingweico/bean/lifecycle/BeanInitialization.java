package cn.qingweico.bean.lifecycle;

import cn.qingweico.ioc.injection.UserHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * @author zqw
 * @date 2021/12/18
 */
public class BeanInitialization {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessorImpl());
        // 添加 CommonAnnotationBeanPostProcessor 解决 @PostConstruct @PreDestroy问题
        beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        UserHolder.loadUser(beanFactory);
        // SmartInitializingSingleton 通常在 ApplicationContext 环境中使用
        beanFactory.preInstantiateSingletons();
        UserHolder userHolder = beanFactory.getBean("userHolder", UserHolder.class);
        System.out.println(userHolder);
    }
}
