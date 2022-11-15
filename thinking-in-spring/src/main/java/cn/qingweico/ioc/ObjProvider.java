package cn.qingweico.ioc;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 通过 {@link ObjectProvider} 进行依赖查找
 *
 * @author zqw
 * @date 2021/11/12
 */
public class ObjProvider {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ObjProvider.class);
        context.refresh();
        lookupByObjectProvider(context);
        lookupByStream(context);
        lookupIfAvailable(context);
        context.close();
    }

    private static void lookupByStream(AnnotationConfigApplicationContext context) {
        ObjectProvider<String> objectProperty = context.getBeanProvider(String.class);
        System.out.print("lookupByStream: ");
        objectProperty.stream().forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    @Bean
    @Primary
    public String hello() {
        return "hello";
    }

    @Bean
    public String world() {
        return "world";
    }

    private static void lookupByObjectProvider(AnnotationConfigApplicationContext context) {
        // ObjectProvider 继承至 ObjectFactory
        ObjectProvider<String> objectProperty = context.getBeanProvider(String.class);
        System.out.println("lookupByObjectProvider: " + objectProperty.getObject());
    }

    private static void lookupIfAvailable(AnnotationConfigApplicationContext context) {
        ObjectProvider<User> userObjectProvider = context.getBeanProvider(User.class);
        User user = userObjectProvider.getIfAvailable(User::createUser);
        System.out.println(user);
    }
}
