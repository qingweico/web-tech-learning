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
        environment.setDefaultProfiles("dev");
        environment.addActiveProfile("tst");
        context.refresh();
        String dev = context.getBean("dev", String.class);
        System.out.println(dev);
        context.close();
    }

    @Bean(name = "dev")
    @Profile(value = "dev")
    public String dev() {
        return "dev";
    }

    @Bean(name = "tst")
    @Profile(value = "tst")
    @Conditional(EventCondition.class)
    public String tst() {
        return "tst";
    }
}
