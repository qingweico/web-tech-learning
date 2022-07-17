package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class AnnotationMethodDependency {

    private UserHolder userHolder;
    @Autowired
    private void init(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AnnotationMethodDependency.class);
        UserHolder.loadUser(context);
        context.refresh();
        AnnotationMethodDependency annotationFieldDependency = context.getBean(AnnotationMethodDependency.class);
        UserHolder userHolder = annotationFieldDependency.userHolder;
        System.out.println(userHolder);
        context.close();
    }
    @Bean
    public UserHolder userHolder(User user) {
        UserHolder userHolder = new UserHolder();
        userHolder.setUser(user);
        return userHolder;
    }
}
