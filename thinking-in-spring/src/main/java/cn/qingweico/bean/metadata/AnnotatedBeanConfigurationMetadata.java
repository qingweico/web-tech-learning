package cn.qingweico.bean.metadata;

import cn.qingweico.enums.City;
import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zqw
 * @date 2021/12/20
 */
@ImportResource("classpath:META-INF/lookup-context.xml")
@Import({User.class})
@PropertySource("classpath:META-INF/user.properties")
@PropertySources({@PropertySource("classpath:META-INF/user.properties"),
        @PropertySource("classpath:META-INF/application.properties")})
public class AnnotatedBeanConfigurationMetadata {

    // user.name 在 Java Properties 存在, 会覆盖掉配置文件中的配置

    @Bean
    public User externalUser(@Value("${user.id}") Long id,
                             @Value("${user.name}") String name,
                             @Value("${user.city}") City city) {
        User user = new User();
        user.setId(id);
        user.setCity(city);
        user.setName(name);
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotatedBeanConfigurationMetadata.class);
        System.out.println(applicationContext.getEnvironment().getPropertySources());
        Map<String, Object> map = new HashMap<>();
        map.put("user.name", "li");
        org.springframework.core.env.PropertySource<Map<String, Object>> propertySource = new MapPropertySource("superior", map);
        // 手动覆盖掉 Java Properties 中的 user.name
        applicationContext.getEnvironment().getPropertySources().addFirst(propertySource);
        applicationContext.refresh();
        Map<String, User> userMap = applicationContext.getBeansOfType(User.class);
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            System.out.printf("Bean name : %s; bean : %s%n", entry.getKey(), entry.getValue());
        }
        System.out.println(applicationContext.getEnvironment().getPropertySources());
        applicationContext.close();
    }
}
