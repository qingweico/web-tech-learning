package cn.qingweico.bean.metadata;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zqw
 * @date 2021/12/21
 */
@PropertySource(name = "yamlPropertySource",
        value = "classpath:META-INF/user.yml",
        factory = YamlPropertySourceFactory.class)
public class YamlPropertySourceBasedAnnotated {

    @Bean
    public User user(@Value("${user.id}") Long id,
                     @Value("${user.name}") String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(YamlPropertySourceBasedAnnotated.class);
        context.refresh();
        User user = context.getBean("user", User.class);
        System.out.println(user);
        context.close();
    }
}
