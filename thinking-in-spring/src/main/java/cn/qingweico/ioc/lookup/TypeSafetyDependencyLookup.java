package cn.qingweico.ioc.lookup;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zqw
 * @date 2021/11/14
 */
public class TypeSafetyDependencyLookup {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(TypeSafetyDependencyLookup.class);
        context.refresh();
        lookup(context);
        lookupByObjectFactory(context);
        lookupByObjectProvider(context);
        lookupByListableBeanFactory(context);
        lookupByObjectProviderStream(context);
        context.close();
    }

    private static void lookupByObjectProviderStream(BeanFactory context) {

        try {
            ObjectProvider<User> userObjectProvider = context.getBeanProvider(User.class);
            userObjectProvider.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void lookupByListableBeanFactory(ListableBeanFactory context) {
        try {
            System.out.println(context.getBeansOfType(User.class));
        } catch (BeansException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void lookup(BeanFactory beanFactory) {
        try {
            beanFactory.getBean(User.class);
        } catch (BeansException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void lookupByObjectFactory(BeanFactory beanFactory) {
        try {
            ObjectFactory<User> userObjectFactory = beanFactory.getBeanProvider(User.class);
            System.out.println(userObjectFactory.getObject());
        } catch (BeansException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void lookupByObjectProvider(BeanFactory beanFactory) {
        try {
            ObjectProvider<User> userObjectProvider = beanFactory.getBeanProvider(User.class);
            System.out.println(userObjectProvider.getIfAvailable());
        } catch (BeansException e) {
            System.out.println(e.getMessage());
        }
    }
}
