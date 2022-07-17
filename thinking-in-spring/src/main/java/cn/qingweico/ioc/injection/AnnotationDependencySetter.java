package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class AnnotationDependencySetter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AnnotationDependencySetter.class);
        UserHolder.loadUser(context);
        context.refresh();
        UserHolder userHolder = context.getBean(UserHolder.class);
        System.out.println(userHolder);
        context.close();
    }
    @Bean
    public UserHolder userHolder(User user) {
        UserHolder userHolder = new UserHolder();
        userHolder.setUser(user);
        return userHolder;
        // constructor
        // return new UserHolder(user);
    }
}
