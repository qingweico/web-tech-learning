package cn.qingweico.ioc.lookup;

import cn.qingweico.ioc.annotation.Super;
import cn.qingweico.ioc.domain.User;
import cn.qingweico.resource.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * 依赖查找
 *
 * @author zqw
 * @date 2021/11/7
 */
@Slf4j
public class DependencyLookup {
    static BeanFactory context = new ClassPathXmlApplicationContext("META-INF/lookup-context.xml");

    public static void main(String[] args) {
        lookupRealtime();
        lookupInLazy();
        lookupByType();
        lookupCollectionByType(context);
        lookupByAnnotationType(context);

    }

    public static void lookupRealtime() {
        User user = context.getBean("user", User.class);
        log.info("realTime: {}", user);
    }

    @SuppressWarnings("unchecked")
    public static void lookupInLazy() {
        // FactoryBean 和 ObjectFactory 的区别
        ObjectFactory<User> objectFactory = context.getBean("objectFactory", ObjectFactory.class);
        User user = objectFactory.getObject();
        log.info("lazy: {}", user);
    }

    public static void lookupByType() {
        // 按照类型查找单个对象
        User user = context.getBean(User.class);
        log.info("lookupByType[Single Type]: {}", user);
    }

    public static void lookupCollectionByType(BeanFactory beanFactory) {
        // 按照类型查找集合对象
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            // id为key,ioc中的对象为value
            Map<String, User> map = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("lookupByType[Collections Type]: ");
            MapUtils.toPrint(map);
        }
    }

    @SuppressWarnings("unchecked, rawtypes")
    public static void lookupByAnnotationType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = (Map) listableBeanFactory.getBeansWithAnnotation(Super.class);
            log.info("lookupByType[@Super Annotation]: {}", users);
        }
    }
}
