package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class LazyAnnotationDependency {

    // 非延迟注入
    @Autowired
    private User user;
    // 延迟注入
    @Autowired
    private ObjectProvider<User> userObjectProvider;
    @Autowired
    private ObjectFactory<Set<User>> users;
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LazyAnnotationDependency.class);
        UserHolder.loadUser(context);
        context.refresh();
        LazyAnnotationDependency cls = context.getBean(LazyAnnotationDependency.class);
        System.out.println(cls.user);
        System.out.println(cls.userObjectProvider.getObject());
        System.out.println(cls.users.getObject());
        cls.userObjectProvider.forEach(System.out::println);
        context.close();
    }
}
