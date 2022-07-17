package cn.qingweico.environment;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * {@link PropertySourcesPlaceholderConfigurer}
 * {@link PropertyPlaceholderConfigurer}
 * @author zqw
 * @date 2022/6/30
 */
public class PropertyPlaceholder {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/placeholders-resolver.xml");
        User user = context.getBean("user", User.class);
        System.out.println(user);
        context.close();
    }
}
