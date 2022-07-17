package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.security.PrivateKey;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class AnnotationFieldDependency {
    @Autowired
    private UserHolder userHolder;
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AnnotationFieldDependency.class);

        UserHolder.loadUser(context);
        context.refresh();
        AnnotationFieldDependency annotationFieldDependency = context.getBean(AnnotationFieldDependency.class);
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
