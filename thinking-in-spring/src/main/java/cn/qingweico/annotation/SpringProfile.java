package cn.qingweico.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * {@link Profile} 示例
 *
 * @author zqw
 * @date 2022/9/24
 */
public class SpringProfile {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringProfile.class);
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setDefaultProfiles("odd");
        environment.addActiveProfile("even");
        context.refresh();
        Integer number = context.getBean("number", Integer.class);
        System.out.println(number);
        context.close();
    }

    @Bean(name = "number")
    @Profile(value = "odd")
    public int odd() {
        return 1;
    }

    @Bean(name = "number")
    @Profile(value = "even")
    @Conditional(EventCondition.class)
    public int even() {
        return 2;
    }
}
