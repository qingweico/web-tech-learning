package cn.qingweico.ioc.dependency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zqw
 * @date 2021/11/20
 */
@Configuration
@PropertySource(value = "classpath:META-INF/user.properties", encoding = "UTF-8")
public class ExternalConfigurationDependencySource {
    @Value("${user.id}")
    private Long id;
    @Value("${usr.name}")
    private String name;


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ExternalConfigurationDependencySource.class);
        context.refresh();
        ExternalConfigurationDependencySource cls = context.getBean(ExternalConfigurationDependencySource.class);
        System.out.println(cls.id);
        System.out.println(cls.name);
        context.close();
    }
}
