package cn.qingweico.ioc.lookup;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import javax.inject.Inject;

/**
 * @author zqw
 * @date 2022/3/12
 */
public class ObjectFactoryLazyLookup {
    @Inject
    private ObjectFactory<User> userObjectFactory;

    @Inject
    private ObjectProvider<User> userObjectProvider;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ObjectFactoryLazyLookup.class);
        context.refresh();
        ObjectFactoryLazyLookup ofll = context.getBean(ObjectFactoryLazyLookup.class);
        // 代理对象
        ObjectFactory<User> userObjectFactory = ofll.userObjectFactory;
        ObjectProvider<User> userObjectProvider = ofll.userObjectProvider;
        System.out.println("userObjectFactory == userObjectProvider: " + (userObjectFactory == userObjectProvider));
        System.out.println("userObjectFactory.getClass() == userObjectProvider.getClass(): " +
                (userObjectFactory.getClass() == userObjectProvider.getClass()));
        // 实际对象
        System.out.println("[user = " + userObjectFactory.getObject() + "]");
        System.out.println("[user = " + userObjectProvider.getObject() + "]");
        System.out.println("[user = " + context.getBean(User.class) + "]");
        context.close();
    }

    @Bean
    @Lazy
    public static User user() {
        User user = new User();
        user.setId(1L);
        user.setName("li");
        return user;
    }
}
